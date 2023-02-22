package ru.pascalcode.tasktracker.bot.updatehandler.setting;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

@Component
public class SetMinWeekHoursUpdateHandler extends AbstractSettingsUpdateHandler {
    protected SetMinWeekHoursUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        String weekHours = update.getMessage().getText().replaceFirst("\uD83D\uDD54", "");
        if (!SettingsUtils.containsOnlyDigit(weekHours)) {
            answer.setText("Oops");
        } else {
            user.setMinWeekHours(Integer.valueOf(weekHours));
            userService.saveUser(user);
            setSettingsTextAnswer(answer, user);
        }
    }

}
