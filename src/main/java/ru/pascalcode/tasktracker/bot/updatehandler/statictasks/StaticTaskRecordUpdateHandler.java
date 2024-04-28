package ru.pascalcode.tasktracker.bot.updatehandler.statictasks;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.bot.updatehandler.home.ToStaticTasksSettingsUpdateHandler;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

@Component
public class StaticTaskRecordUpdateHandler extends ToStaticTasksSettingsUpdateHandler {

    private static final String TASK_ADDED_TO_STATIC_BAR = "The task `%s` added to button bar as static /%d";

    public StaticTaskRecordUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    @Override
    @Transactional
    protected void handle(Update update, SendMessage answer, User user) {
        String taskName = update.getMessage().getText();
        Task task = taskService.getStaticTask(taskName, user);
        answer.setText(String.format(TASK_ADDED_TO_STATIC_BAR, task.getName(), task.getUserTaskId()));
    }
}
