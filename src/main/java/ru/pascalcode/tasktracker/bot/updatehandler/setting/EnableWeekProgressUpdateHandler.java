package ru.pascalcode.tasktracker.bot.updatehandler.setting;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

@Component
public class EnableWeekProgressUpdateHandler extends AbstractSettingsUpdateHandler {
    protected EnableWeekProgressUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        user.setWeekHoursStat(true);
        userService.saveUser(user);
        setSettingsTextAnswer(answer, user);
    }

}
