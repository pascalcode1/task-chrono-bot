package ru.pascalcode.tasktracker.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.repository.TaskRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Task getOrCreateTask(String name, User user) {
        Task task = getTaskIdReceived(user, name);
        if (task == null) {
            task = taskRepository.findByUserAndName(user, name);
        }
        if (task == null) {
            return saveTask(new Task(user, name, createUserTaskId(user)));
        }
        return task;
    }

    @Transactional
    public Task getTask(String name, User user) {
        return taskRepository.findByUserAndName(user, name);
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).get();
    }

    @Transactional
    public Task getStaticTask(String name, User user) {
        Task task = taskRepository.findByUserAndName(user, name);
        if (task == null) {
            return saveTask(new Task(user, name, createUserTaskId(user), true));
        }
        if (Boolean.FALSE.equals(task.getStaticTask())) {
            task.setStaticTask(true);
            saveTask(task);
        }
        return task;
    }

    public List<Task> getStaticTasks(User user) {
        return taskRepository.findAllByUserAndStaticTaskOrderByIdAsc(user, true);
    }

    public List<Task> getLastTasksList(User user) {
        return taskRepository.findByUser(user, PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id")));
    }

    public List<Task> getAllTask(User user) {
        return taskRepository.findAllByUser(user);
    }

    public Task hideTask(Task task) {
        task.setStaticTask(false);
        return saveTask(task);
    }

    public Task getTaskIdReceived(User user, String text) {
        if (!text.startsWith("/")) {
            return null;
        }
        String cropped = text.substring(1);
        if (!cropped.matches("\\d+")) {
            return null;
        }
        return taskRepository.findByUserAndUserTaskId(user, Long.parseLong(cropped));
    }

    public void renameTask(User user, String newTaskName) {
        taskRepository.findById(Long.valueOf(user.getMeta()))
                      .ifPresent(task -> {
                                     task.setName(newTaskName);
                                     saveTask(task);
                                 });
    }

    private Long createUserTaskId(User user) {
        Long maxUserTaskId = taskRepository.findMaxUserTaskId(user.getId());
        return maxUserTaskId == null ? 0 : ++maxUserTaskId;
    }
}
