package ru.pascalcode.tasktracker.bot.updatehandler.edittimerecords;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.State;
import ru.pascalcode.tasktracker.model.TaskLog;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static ru.pascalcode.tasktracker.bot.Buttons.BACK_BTN;
import static ru.pascalcode.tasktracker.bot.Buttons.DELETE_TIME_RECORD;
import static ru.pascalcode.tasktracker.bot.Emoji.EDIT;
import static ru.pascalcode.tasktracker.bot.Emoji.ID;

@Component
public class EditTimeRecordUpdateHandler extends AbstractUpdateHandler {
    protected EditTimeRecordUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        String text = update.getMessage().getText();
        Long taskLogId = Long.valueOf(text.substring(text.indexOf(ID) + 2));
        user.setState(State.EDIT_TIME_RECORD);
        user.setMeta(String.valueOf(taskLogId));
        userService.saveUser(user);
        answer.setText("Make a choose");
    }

    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        Long taskLogId = Long.valueOf(user.getMeta());
        TaskLog taskLog = taskLogService.findById(taskLogId);
        String start = taskLog.getStart().format(dateTimeFormatter);
        String stop = taskLog.getStop() == null ? "null" : taskLog.getStop().format(dateTimeFormatter);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(BACK_BTN))));
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(DELETE_TIME_RECORD))));
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(EDIT + "Start " + start),
                                             new KeyboardButton(EDIT + "Stop " + stop))));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
