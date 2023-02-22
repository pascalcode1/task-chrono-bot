package ru.pascalcode.tasktracker.bot.updatehandler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.util.List;

import static ru.pascalcode.tasktracker.bot.Buttons.BACK_BTN;
import static ru.pascalcode.tasktracker.bot.PrefixEmoji.DELETE;

@Component
public class DeleteUpdateHandler extends AbstractUpdateHandler {
    protected DeleteUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        String taskName = update.getMessage().getText().replaceFirst(DELETE,"");
        Task task = taskService.getTask(taskName, user);
        taskLogService.deleteTask(task);
        answer.setText("Задача \"" + taskName + "\" удалена");
    }

    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = getActiveTaskKeyboardRowList(user, DELETE);
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(BACK_BTN))));
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(false);
        return replyKeyboardMarkup;
    }
}
