package ru.pascalcode.tasktracker.bot.updatehandler.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.TaskLog;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.util.List;

import static ru.pascalcode.tasktracker.bot.Buttons.BREAK_BTN;
import static ru.pascalcode.tasktracker.bot.Buttons.YESTERDAY_REPORT_BTN;

@Component
public class TaskRecordUpdateHandler extends AbstractUpdateHandler {

    private static final String TASK_IN_PROGRESS = "The task \"%s\" is in progress";

    public TaskRecordUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    @Transactional
    protected void handle(Update update, SendMessage answer, User user) {
        String taskName = update.getMessage().getText();
        Task task = taskService.getTask(taskName, user);
        taskLogService.addTaskLogRecord(task);
        answer.setText(String.format(TASK_IN_PROGRESS, task.getName()));
        answer.setReplyMarkup(getReplyKeyboardMarkup(user));
    }

    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = getActiveTaskKeyboardRowList(user);
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(BREAK_BTN), new KeyboardButton(YESTERDAY_REPORT_BTN))));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
