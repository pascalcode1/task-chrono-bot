package ru.pascalcode.tasktracker.bot.updatehandler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

@Component
public class StartUpdateHandler extends AbstractUpdateHandler {
    protected StartUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        answer.setText("Привет! Я буду помогать тебе вести учёт времени, потраченного на задачи. " +
                "Просто отправляй мне номера тасок или названия задач, которые сейчас в работе и сообщи, когда задачи на сегодня будут закончены.\n" +
                "Когда потребуется узнать время, потраченное на задачи, запроси отчёт.\n" +
                "Продуктивного существования!");
    }

}
