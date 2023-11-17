package ru.pascalcode.tasktracker.bot;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pascalcode.tasktracker.bot.updatehandler.UpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.UpdateHandlerFactory;

import static ru.pascalcode.tasktracker.bot.Commands.*;

@Component
public final class Bot extends TelegramLongPollingBot implements InitializingBean {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String BOT_NAME = dotenv.get("BOT_NAME");
    private static final String BOT_TOKEN = dotenv.get("BOT_TOKEN");
    private static Bot instance;
    private final UpdateHandlerFactory updateHandlerFactory;

    public Bot(UpdateHandlerFactory updateHandlerFactory) throws TelegramApiException {
        this.updateHandlerFactory = updateHandlerFactory;
        setCommands();
    }

    private void setCommands() throws TelegramApiException {
        SetMyCommands commands = SetMyCommands
                .builder()
                .command(new BotCommand(DELETE, "Delete tasks"))
                .command(new BotCommand(EDIT, "Edit tasks"))
                .command(new BotCommand(STATIC_TASKS, "Static tasks settings"))
                .command(new BotCommand(SETTINGS, "Settings"))
                .build();
        this.execute(commands);
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        UpdateHandler updateHandler = updateHandlerFactory.getUpdateHandler(update);
        SendMessage answer = updateHandler.execute(update);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void afterPropertiesSet() {
        instance = this;
    }

    public static Bot get() {
        return instance;
    }


}
