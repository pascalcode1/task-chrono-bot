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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.pascalcode.tasktracker.bot.Buttons.*;
import static ru.pascalcode.tasktracker.bot.Emoji.*;

public abstract class AbstractUpdateHandler implements UpdateHandler {

    protected final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

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
        ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup(user);
        if (replyKeyboardMarkup != null) {
            replyKeyboardMarkup.setResizeKeyboard(true);
        }
        answer.setReplyMarkup(replyKeyboardMarkup);
        answer.setChatId(chatId.toString());
        answer.enableMarkdown(true);
        return answer;
    }

    protected abstract void handle(Update update, SendMessage answer, User user);

    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = getStaticTasksKeyboardRowList(user);
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

    protected List<KeyboardRow> getStaticTasksKeyboardRowList(User user) {
        return getStaticTasksKeyboardRowList(user, "");
    }

    protected List<KeyboardRow> getStaticTasksKeyboardRowList(User user, String prefix) {
        List<Task> staticTaskList = taskService.getStaticTasks(user);
        return getKeyboardRows(staticTaskList, prefix);
    }

    protected List<KeyboardRow> getLastTaskList(User user, String prefix) {
        List<Task> taskToDelete = taskService.getLastTasksList(user);
        return getKeyboardRows(taskToDelete, prefix);
    }

    protected List<KeyboardRow> getTimeRecordsForEditing(User user, String prefix) {
        List<TaskLog> taskLogs = taskLogService.getLastTaskLogList(user);
        return getTimeRecordsKeyboardRows(taskLogs, prefix);
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

    private List<KeyboardRow> getTimeRecordsKeyboardRows(List<TaskLog> taskLogs, String prefix) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (int i = 0; i < taskLogs.size(); i++) {
            TaskLog taskLog = taskLogs.get(i);
            Task task = taskLog.getTask();
            String start = taskLog.getStart().format(dateTimeFormatter);
            String stop = taskLog.getStop() == null ? "null" : taskLog.getStop().format(dateTimeFormatter);
            String button = prefix + task.getName() + TIME_START + start + TIME_STOP + stop + ID + taskLog.getId();
            keyboardRows.add(new KeyboardRow(List.of(new KeyboardButton(button))));
        }
        return keyboardRows;
    }
}
