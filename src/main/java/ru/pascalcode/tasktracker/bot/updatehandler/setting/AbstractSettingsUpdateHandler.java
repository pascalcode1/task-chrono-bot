package ru.pascalcode.tasktracker.bot.updatehandler.setting;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static ru.pascalcode.tasktracker.bot.Buttons.*;
import static ru.pascalcode.tasktracker.bot.Buttons.MIN_WEEK_HOURS_BTN;

public abstract class AbstractSettingsUpdateHandler extends AbstractUpdateHandler {

    private static final String CURRENT_SETTINGS = "Текущие настройки бота";
    private static final String WEEK_PROGRESS_ON = "✅ прогресс за неделю включен в отчет";
    private static final String WEEK_PROGRESS_OFF = "❌ прогресс за неделю не включен в отчет";
    private static final String FIRST_DAY_OF_WEEK = "\uD83D\uDCC6 Первый день недели: %s";
    private static final String WIN_WEEK_HOURS = "\uD83D\uDD54 Норматив часов в неделю: %s";

    protected AbstractSettingsUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    protected void setSettingsTextAnswer(SendMessage answer, User user) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add(CURRENT_SETTINGS);
        stringJoiner.add(Boolean.TRUE.equals(user.getWeekHoursStat()) ? WEEK_PROGRESS_ON : WEEK_PROGRESS_OFF);
        stringJoiner.add(String.format(FIRST_DAY_OF_WEEK, SettingsUtils.getNameOfDay(user.getFirstDayOfWeek())));
        stringJoiner.add(String.format(WIN_WEEK_HOURS, user.getMinWeekHours()));
        answer.setText(stringJoiner.toString());
    }



    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        if (Boolean.TRUE.equals(user.getWeekHoursStat())) {
            keyboard.add(new KeyboardRow(List.of(new KeyboardButton(WEEK_HOURS_STATE_OFF_BTN))));
        } else {
            keyboard.add(new KeyboardRow(List.of(new KeyboardButton(WEEK_HOURS_STATE_ON_BTN))));
        }
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(FIRST_DAY_OF_WEEK_BTN))));
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(MIN_WEEK_HOURS_BTN))));
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(BACK_BTN))));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
