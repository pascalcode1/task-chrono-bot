package ru.pascalcode.tasktracker.bot.updatehandler.home;

import jakarta.transaction.Transactional;
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

import static ru.pascalcode.tasktracker.bot.Buttons.*;

@Component
public class TaskRecordUpdateHandler extends AbstractUpdateHandler {

    private static final String TASK_IN_PROGRESS = "👨‍💻 The task `%s` is in progress /%d";

    public TaskRecordUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    @Transactional
    protected void handle(Update update, SendMessage answer, User user) {
        String taskName = update.getMessage()
                                .getText()
                                .replace("`", "'");
        Task task = taskService.getOrCreateTask(taskName, user);
        taskLogService.addTaskLogRecord(task);
        answer.setText(String.format(TASK_IN_PROGRESS, task.getName(), task.getUserTaskId()));
    }

    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> tasksToShow = getStaticTasksKeyboardRowList(user);
        boolean hasUncompleted = taskLogService.getUncompletedTask(user) != null;
        tasksToShow.add(new KeyboardRow(List.of(new KeyboardButton(hasUncompleted ? BREAK_BTN : TODAY_REPORT_BTN),
                                                new KeyboardButton(YESTERDAY_REPORT_BTN))));
        replyKeyboardMarkup.setKeyboard(tasksToShow);
        return replyKeyboardMarkup;
    }
}
