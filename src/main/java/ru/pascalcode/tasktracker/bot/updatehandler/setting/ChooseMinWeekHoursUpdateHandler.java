package ru.pascalcode.tasktracker.bot.updatehandler.setting;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChooseMinWeekHoursUpdateHandler extends AbstractSettingsUpdateHandler {
    protected ChooseMinWeekHoursUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        answer.setText("Выберите сколько часов в неделю хотите работать.\n" +
                "Текущий норматив : " + user.getMinWeekHours() + " в неделю");
    }

    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        for (int i = 1; i <= 85; i++) {
            keyboardRow.add("\uD83D\uDD54"+ i);
            if (i % 5 == 0) {
                keyboard.add(keyboardRow);
                keyboardRow = new KeyboardRow();
            }
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
