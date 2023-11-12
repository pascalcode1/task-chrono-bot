package ru.pascalcode.tasktracker.bot.updatehandler.home;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.dto.TaskLogDto;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.home.report.ReportUtils;
import ru.pascalcode.tasktracker.model.TaskLog;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.pascalcode.tasktracker.bot.Buttons.TODAY_REPORT_BTN;
import static ru.pascalcode.tasktracker.bot.Buttons.YESTERDAY_REPORT_BTN;

@Component
public class BreakUpdateHandler extends AbstractUpdateHandler {

    private static final String BREAK = "The timer is on pause\n %s";

    private static final String NO_TASK_TO_BRAKE = "There's no task to break 🤷";

    protected BreakUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        TaskLog taskLog = taskLogService.getUncompletedTask(user);
        if (taskLog == null) {
            answer.setText(NO_TASK_TO_BRAKE);
            return;
        }
        taskLog.setStop(LocalDateTime.now());
        taskLogService.saveTaskLog(taskLog);
        List<TaskLog> taskLogList = taskLogService.findAllByTaskToday(taskLog.getTask());
        TaskLogDto taskLogDto = ReportUtils.getTimeForTaskByTaskLog(taskLog.getTask(), taskLogList);
        answer.setText(String.format(BREAK, taskLogDto));
    }

    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = getTaskToShowKeyboardRowList(user);
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(TODAY_REPORT_BTN), new KeyboardButton(YESTERDAY_REPORT_BTN))));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
