package ru.pascalcode.tasktracker.bot.updatehandler.home;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
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

import static ru.pascalcode.tasktracker.bot.Buttons.BACK_BTN;
import static ru.pascalcode.tasktracker.bot.Emoji.DELETE;

@Component
public class ToDeleteListUpdateHandler extends AbstractUpdateHandler {
    protected ToDeleteListUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        user.setState(State.DELETE);
        userService.saveUser(user);
        answer.setText("""
                Select the task you want to delete.
                The last 20 tasks are shown. If you want to delete a different task, just send its name.
                ATTENTION! Selected tasks will be deleted permanently and will no longer appear in reports.""");
    }

    @Override
    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(User user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(BACK_BTN))));
        keyboard.addAll(getLastTaskList(user, DELETE));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
