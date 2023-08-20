package ru.pascalcode.tasktracker.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.repository.TaskRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    public Task getTask(String name, User user) {
        Task task = getTaskIdReceived(user, name);
        if (task == null) {
            task = taskRepository.findByUserAndName(user, name);
        }
        if (task == null) {
            return taskRepository.save(new Task(user, name));
        }
        if (Boolean.TRUE.equals(user.getAddNewTasksToButtonBar()) && Boolean.FALSE.equals(task.getShowOnButtonBar())) {
            task.setShowOnButtonBar(true);
            taskRepository.save(task);
        }
        return task;
    }

    @Transactional
    public Task getStaticTask(String name, User user) {
        Task task = taskRepository.findByUserAndName(user, name);
        if (task == null) {
            return taskRepository.save(new Task(user, name, true, true));
        }
        if (Boolean.FALSE.equals(task.getShowOnButtonBar()) || Boolean.FALSE.equals(task.getStaticTask())) {
            task.setShowOnButtonBar(true);
            task.setStaticTask(true);
            taskRepository.save(task);
        }
        return task;
    }

    public List<Task> getStaticTasksToShowOnButtonBar(User user) {
        return taskRepository.findAllByUserAndShowOnButtonBarAndStaticTaskOrderByIdAsc(user, true, true);
    }

    public List<Task> getTasksToShowOnButtonBar(User user) {
        return taskRepository.findAllByUserAndShowOnButtonBarAndStaticTaskOrderByIdAsc(user, true, false);
    }

    public List<Task> getAllTask(User user) {
        return taskRepository.findAllByUser(user);
    }

    public Task hideTask(Task task) {
        task.setShowOnButtonBar(false);
        return taskRepository.save(task);
    }

    public Task getTaskIdReceived(User user, String text) {
        if (!text.startsWith("/")) {
            return null;
        }
        String cropped = text.substring(1);
        if (!cropped.matches("\\d+")) {
            return null;
        }
        return taskRepository.findByUserAndId(user, Long.parseLong(cropped));
    }
}
