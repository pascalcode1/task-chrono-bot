package ru.pascalcode.tasktracker.bot.updatehandler.home.report;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.time.LocalDateTime;

@Component
public class YesterdayReportUpdateHandler extends AbstractReportUpdateHandler {

    public YesterdayReportUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        LocalDateTime reportDay = LocalDateTime.now().minusDays(1);
        setReportToAnswer(user, reportDay, answer);
    }
}
