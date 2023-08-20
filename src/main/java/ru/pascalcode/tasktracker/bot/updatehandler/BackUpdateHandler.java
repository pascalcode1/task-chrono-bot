package ru.pascalcode.tasktracker.bot.updatehandler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.updatehandler.home.report.AbstractReportUpdateHandler;
import ru.pascalcode.tasktracker.model.State;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.TaskLog;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.pascalcode.tasktracker.bot.Buttons.*;

@Component
public class BackUpdateHandler extends AbstractReportUpdateHandler { //TODO need to change

    private static final String TASK_IN_PROGRESS = "The task \"%s\" in progress /%d";
    protected BackUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        user.setState(State.HOME);
        userService.saveUser(user);
        TaskLog uncompletedTask = taskLogService.getUncompletedTask(user);
        if (uncompletedTask != null) {
            Task task = uncompletedTask.getTask();
            answer.setText(String.format(TASK_IN_PROGRESS, task.getName(), task.getId()));
        } else {
            LocalDateTime reportDay = LocalDateTime.now();
            setReportToAnswer(user, reportDay, answer);
        }
    }

    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> tasksToShow = getTaskToShowKeyboardRowList(user);
        boolean hasUncompleted = taskLogService.getUncompletedTask(user) != null;
        tasksToShow.add(new KeyboardRow(List.of(new KeyboardButton(hasUncompleted ? BREAK_BTN : TODAY_REPORT_BTN),
                                                new KeyboardButton(YESTERDAY_REPORT_BTN))));
        replyKeyboardMarkup.setKeyboard(tasksToShow);
        return replyKeyboardMarkup;
    }

}
