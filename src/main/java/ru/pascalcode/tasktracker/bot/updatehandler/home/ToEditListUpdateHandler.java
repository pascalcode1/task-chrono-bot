package ru.pascalcode.tasktracker.bot.updatehandler.home;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.Emoji;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.State;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static ru.pascalcode.tasktracker.bot.Buttons.BACK_BTN;
import static ru.pascalcode.tasktracker.bot.Emoji.EDIT;

@Component
public class ToEditListUpdateHandler extends AbstractUpdateHandler {
    protected ToEditListUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        user.setState(State.EDIT_TASKS);
        userService.saveUser(user);
        answer.setText("""
                Send the task you want to rename.
                The last 20 tasks are shown. If you want to rename a different task, just send its name.""");
    }

    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(BACK_BTN))));
        keyboard.addAll(getLastTaskList(user, EDIT));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
