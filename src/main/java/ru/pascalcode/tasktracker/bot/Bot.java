package ru.pascalcode.tasktracker.bot;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.lang3.StringUtils;
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

    private static final String BOT_TOKEN_VARIABLE_NAME = "BOT_TOKEN";
    private static final String BOT_NAME_VARIABLE_NAME = "BOT_NAME";

    private static Bot instance;

    private final Dotenv dotenv;
    private final UpdateHandlerFactory updateHandlerFactory;

    public Bot(Dotenv dotenv, UpdateHandlerFactory updateHandlerFactory) throws TelegramApiException {
        this.dotenv = dotenv;
        this.updateHandlerFactory = updateHandlerFactory;
        setCommands();
    }

    private void setCommands() throws TelegramApiException {
        SetMyCommands commands = SetMyCommands
                .builder()
                .command(new BotCommand(DELETE_TASKS, "Delete tasks"))
                .command(new BotCommand(EDIT_TASKS, "Edit tasks"))
                .command(new BotCommand(STATIC_TASKS, "Static tasks settings"))
                .command(new BotCommand(EDIT_TIME_RECORDS, "Edit time records"))
                .command(new BotCommand(SETTINGS, "Settings"))
                .build();
        this.execute(commands);
    }

    @Override
    public String getBotUsername() {
        return getVariable(BOT_NAME_VARIABLE_NAME);
    }

    @Override
    public String getBotToken() {
        return getVariable(BOT_TOKEN_VARIABLE_NAME);
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

    private String getVariable(String variable) {
        String systemEnvVariable = System.getenv(variable);
        return StringUtils.isNotBlank(systemEnvVariable)
             ? systemEnvVariable
             : dotenv.get(variable);
    }

}
