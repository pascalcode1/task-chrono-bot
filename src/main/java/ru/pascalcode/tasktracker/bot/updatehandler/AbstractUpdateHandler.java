package ru.pascalcode.tasktracker.bot.updatehandler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.TaskLog;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static ru.pascalcode.tasktracker.bot.Buttons.*;

public abstract class AbstractUpdateHandler implements UpdateHandler {

    private static final int TASK_ROW_LIMIT = 3;

    protected final UserService userService;

    protected final TaskService taskService;

    protected final TaskLogService taskLogService;

    protected AbstractUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        this.userService = userService;
        this.taskService = taskService;
        this.taskLogService = taskLogService;
    }

    @Override
    public SendMessage execute(Update update) {
        User user = getUser(update);
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        SendMessage answer = new SendMessage();
        handle(update, answer, user);
        answer.setReplyMarkup(getReplyKeyboardMarkup(user));
        answer.setChatId(chatId.toString());
        return answer;
    }

    protected abstract void handle(Update update, SendMessage answer, User user);

    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = getActiveTaskKeyboardRowList(user);
        if (keyboard.isEmpty()) {
            List<Task> allTaskForUser = taskService.getAllTask(user);
            if (allTaskForUser.isEmpty()) {
                return null;
            }
        }

        KeyboardRow row = new KeyboardRow();
        TaskLog uncompletedTask = taskLogService.getUncompletedTask(user);
        if (uncompletedTask != null) {
            row.add(new KeyboardButton(BREAK_BTN));
        } else {
            row.add(new KeyboardButton(TODAY_REPORT_BTN));
        }
        row.add(new KeyboardButton(YESTERDAY_REPORT_BTN));
        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    protected User getUser(Update update) {
        org.telegram.telegrambots.meta.api.objects.User from = update.getMessage().getFrom();
        User user = userService.getUser(from.getId());
        if (user != null) {
            return user;
        }
        user = new User(from.getId(), from.getUserName(), from.getFirstName(), from.getLastName());
        return userService.saveUser(user);
    }

    protected List<KeyboardRow> getActiveTaskKeyboardRowList(User user) {
        return getActiveTaskKeyboardRowList(user, "");
    }

    protected List<KeyboardRow> getActiveTaskKeyboardRowList(User user, String prefix) {
        List<Task> taskList = taskService.getActiveTasks(user);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            String button = prefix + task.getName();
            if (row.isEmpty()) {
                row.add(button);
                if (i == taskList.size() - 1 || taskList.size() <= TASK_ROW_LIMIT) {
                    keyboard.add(row);
                    row = new KeyboardRow();
                }
            } else if (taskList.size() > TASK_ROW_LIMIT) {
                row.add(button);
                keyboard.add(row);
                row = new KeyboardRow();
            }
        }
        return keyboard;
    }
}
