package ru.pascalcode.tasktracker.bot.updatehandler;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.PrefixEmoji;
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
        List<KeyboardRow> keyboard = getTaskToShowKeyboardRowList(user);
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
        return userService.getUser(update.getMessage().getFrom());
    }

    protected List<KeyboardRow> getTaskToShowKeyboardRowList(User user) {
        return getTaskToShowKeyboardRowList(user, "");
    }

    protected List<KeyboardRow> getStaticTasksKeyboardRowList(User user) {
        List<Task> taskList = taskService.getStaticTasksToShowOnButtonBar(user);
        return getKeyboardRows(taskList, PrefixEmoji.DELETE);
    }

    protected List<KeyboardRow> getTaskToShowKeyboardRowList(User user, String prefix) {
        List<Task> taskList = taskService.getTasksToShowOnButtonBar(user);
        List<Task> staticTaskList = new ArrayList<>();
        if (StringUtils.isEmpty(prefix)) {
            staticTaskList = taskService.getStaticTasksToShowOnButtonBar(user);
        }
        List<KeyboardRow> keyboardRows = getKeyboardRows(taskList, prefix);
        keyboardRows.addAll(getKeyboardRows(staticTaskList, prefix));
        return keyboardRows;
    }

    private List<KeyboardRow> getKeyboardRows(List<Task> tasks, String prefix) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String button = prefix + task.getName();
            keyboardRow.add(button);
            if (keyboardRow.size() > 1 || i == tasks.size() - 1) {
                keyboardRows.add(keyboardRow);
                keyboardRow = new KeyboardRow();
            }
        }
        return keyboardRows;
    }
}
