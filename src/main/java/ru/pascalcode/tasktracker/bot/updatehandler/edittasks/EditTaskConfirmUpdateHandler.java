package ru.pascalcode.tasktracker.bot.updatehandler.edittasks;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.State;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static ru.pascalcode.tasktracker.bot.Buttons.BACK_BTN;
import static ru.pascalcode.tasktracker.bot.Emoji.EDIT;

@Component
public class EditTaskConfirmUpdateHandler extends AbstractUpdateHandler {
    protected EditTaskConfirmUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        String newTaskName = update.getMessage().getText();
        Task task = taskService.getTaskById(Long.parseLong(user.getMeta()));
        if (task == null) {
            user.setState(State.HOME);
            user.setMeta(null);
            userService.saveUser(user);
            answer.setText("There is no task with name `" + newTaskName + "`");
        } else {
            Task existedTaskWithSameName = taskService.getTask(newTaskName, user);
            if (existedTaskWithSameName != null) {
                answer.setText("The task with name `" + newTaskName + "` already exists. Please, choose another task name.");
            } else {
                taskService.renameTask(user, newTaskName);
                user.setState(State.HOME);
                user.setMeta(null);
                userService.saveUser(user);
                answer.setText("Name of task `" + task.getName() + "` was changed to `" + newTaskName + "`");
            }
        }
    }

    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(BACK_BTN))));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
