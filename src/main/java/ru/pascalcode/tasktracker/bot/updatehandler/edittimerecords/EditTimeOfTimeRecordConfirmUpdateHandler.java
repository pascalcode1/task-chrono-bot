package ru.pascalcode.tasktracker.bot.updatehandler.edittimerecords;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.State;
import ru.pascalcode.tasktracker.model.TaskLog;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.time.LocalDateTime;

@Component
public class EditTimeOfTimeRecordConfirmUpdateHandler extends AbstractUpdateHandler {
    protected EditTimeOfTimeRecordConfirmUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        String time = update.getMessage().getText();
        String meta = user.getMeta();
        Long taskLogId = Long.parseLong(meta.substring(0, meta.indexOf(" ")));
        String timePoint = meta.substring(meta.indexOf(" ") + 1);
        TaskLog taskLog = taskLogService.findById(taskLogId);

        try {
            if (timePoint.equals("Start")) {
                taskLog.setStart(LocalDateTime.parse(time, dateTimeFormatter));
            } else if (timePoint.equals("Stop")) {
                taskLog.setStop(LocalDateTime.parse(time, dateTimeFormatter));
            }
            taskLogService.saveTaskLog(taskLog);
        } catch (Exception e) {
            answer.setText("Wrong format. Send time in format dd-MM-yyyy HH:mm");
            return;
        } finally {
            user.setState(State.HOME);
            user.setMeta(null);
            userService.saveUser(user);
        }
        answer.setText("New time of record has been submitted");
    }
}
