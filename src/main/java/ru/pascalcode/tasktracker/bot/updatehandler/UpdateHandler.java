package ru.pascalcode.tasktracker.bot.updatehandler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {

    SendMessage execute(Update update);
}
