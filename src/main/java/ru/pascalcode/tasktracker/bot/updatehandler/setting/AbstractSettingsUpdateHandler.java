package ru.pascalcode.tasktracker.bot.updatehandler.setting;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.State;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static ru.pascalcode.tasktracker.bot.Buttons.*;

public abstract class AbstractSettingsUpdateHandler extends AbstractUpdateHandler {

    private static final String CURRENT_SETTINGS_MESSAGE = "Current settings of the bot";
    private static final String WEEK_PROGRESS_ON_MESSAGE = "✅ Weekly progress included to report";
    private static final String WEEK_PROGRESS_OFF_MESSAGE = "❌ Weekly progress excluded from report";
    private static final String ADD_NEW_TASKS_TO_BAR_ON_MESSAGE = "✅ New tasks will add to button bar";
    private static final String ADD_NEW_TASKS_TO_BAR_OFF_MESSAGE = "❌ New tasks will not add to button bar";
    private static final String FIRST_DAY_OF_WEEK_MESSAGE = "📆 First day of week: %s";
    private static final String WIN_WEEK_HOURS_MESSAGE = "🕔 Standard hours per week: %s";

    protected AbstractSettingsUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    protected void setSettingsTextAnswer(SendMessage answer, User user) {
        user.setState(State.SETTINGS);
        userService.saveUser(user);
        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add(CURRENT_SETTINGS_MESSAGE);
        stringJoiner.add(Boolean.TRUE.equals(user.getWeekHoursStat()) ? WEEK_PROGRESS_ON_MESSAGE : WEEK_PROGRESS_OFF_MESSAGE);
        stringJoiner.add(Boolean.TRUE.equals(user.getAddNewTasksToButtonBar()) ? ADD_NEW_TASKS_TO_BAR_ON_MESSAGE : ADD_NEW_TASKS_TO_BAR_OFF_MESSAGE);
        stringJoiner.add(String.format(FIRST_DAY_OF_WEEK_MESSAGE, SettingsUtils.getNameOfDay(user.getFirstDayOfWeek())));
        stringJoiner.add(String.format(WIN_WEEK_HOURS_MESSAGE, user.getMinWeekHours()));
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
        if (Boolean.TRUE.equals(user.getAddNewTasksToButtonBar())) {
            keyboard.add(new KeyboardRow(List.of(new KeyboardButton(ADD_NEW_TASKS_TO_BAR_OFF_BTN))));
        } else {
            keyboard.add(new KeyboardRow(List.of(new KeyboardButton(ADD_NEW_TASKS_TO_BAR_ON_BTN))));
        }
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(FIRST_DAY_OF_WEEK_BTN), new KeyboardButton(MIN_WEEK_HOURS_BTN))));
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(BACK_BTN))));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
