package ru.pascalcode.tasktracker.bot.updatehandler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;
import ru.pascalcode.tasktracker.bot.PrefixEmoji;

import java.util.List;

import static ru.pascalcode.tasktracker.bot.Buttons.BACK_BTN;

@Component
public class ToDeleteListUpdateHandler extends AbstractUpdateHandler {
    protected ToDeleteListUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        answer.setText("""
                Select the task you want to delete.
                ATTENTION! Selected tasks will no longer appear in reports.""");
    }

    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = getTaskToShowKeyboardRowList(user, PrefixEmoji.DELETE);
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(BACK_BTN))));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
