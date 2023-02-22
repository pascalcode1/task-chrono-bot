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
        Task task = taskRepository.findByUserAndName(user, name);
        if (task == null) {
            return taskRepository.save(new Task(name, user));
        }
        if (Boolean.TRUE.equals(task.getArchived())) {
            task.setArchived(false);
            taskRepository.save(task);
        }
        return task;
    }

    public List<Task> getActiveTasks(User user) {
        return taskRepository.findAllByUserAndArchived(user, false);
    }

    public List<Task> getAllTask(User user) {
        return taskRepository.findAllByUser(user);
    }

    public Task archiveTask(Task task) {
        task.setArchived(true);
        return taskRepository.save(task);
    }
}
