package ru.pascalcode.tasktracker.bot.updatehandler.setting;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.util.List;

@Component
public class ChooseFirstDayOfWeekUpdateHandler extends AbstractSettingsUpdateHandler {
    protected ChooseFirstDayOfWeekUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        answer.setText("Choose the first day of the week.\n" +
                "Current setting: " + SettingsUtils.getNameOfDay(user.getFirstDayOfWeek()));
    }

    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = SettingsUtils.getWeekDayNames()
                                                  .stream()
                                                  .map(day -> new KeyboardRow(List.of(
                                                          new KeyboardButton("\uD83D\uDCC6" + day))))
                                                  .toList();
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
