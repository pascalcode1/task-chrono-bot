package ru.pascalcode.tasktracker.bot.updatehandler.report;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.model.TaskLog;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.time.LocalDateTime;

@Component
public class TodayReportUpdateHandler extends AbstractReportUpdateHandler {

    public TodayReportUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        TaskLog taskLog = taskLogService.getUncompletedTask(user);
        if (taskLog != null) {
            answer.setText("Действие недоступно. Сперва возьмите перерыв :)");
        } else {
            LocalDateTime reportDay = LocalDateTime.now();
            setReportToAnswer(user, reportDay, answer);
        }
    }
}
