package ru.pascalcode.tasktracker;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.pascalcode.tasktracker.bot.Bot;

@SpringBootApplication
public class TaskTrackerBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerBotApplication.class, args);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(Bot.get());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
