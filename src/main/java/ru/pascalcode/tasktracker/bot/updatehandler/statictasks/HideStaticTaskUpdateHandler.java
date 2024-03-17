package ru.pascalcode.tasktracker.bot.updatehandler.statictasks;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.bot.Emoji;
import ru.pascalcode.tasktracker.bot.updatehandler.home.ToStaticTasksSettingsUpdateHandler;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

@Component
public class HideStaticTaskUpdateHandler extends ToStaticTasksSettingsUpdateHandler {

    private static final String TASK_REMOVED_FROM_STATIC_BAR = "The static task `%s` removed from button bar";

    protected HideStaticTaskUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    protected void handle(Update update, SendMessage answer, User user) {
        String taskName = update.getMessage().getText().replaceFirst(Emoji.DELETE,"");
        Task task = taskService.getTask(taskName, user);
        taskService.hideTask(task);
        answer.setText(String.format(TASK_REMOVED_FROM_STATIC_BAR, task.getName()));
    }
}
