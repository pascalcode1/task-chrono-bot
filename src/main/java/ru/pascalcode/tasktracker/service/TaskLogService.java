package ru.pascalcode.tasktracker.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.TaskLog;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.repository.TaskLogRepository;
import ru.pascalcode.tasktracker.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskLogService {

    private final TaskLogRepository taskLogRepository;

    private final TaskRepository taskRepository;

    public TaskLog saveTaskLog(TaskLog taskLog) {
        return taskLogRepository.save(taskLog);
    }

    @Transactional
    public TaskLog addTaskLogRecord(Task task) {
        //TODO добавить проверку на null
        //TODO продумать вариант если записей будет не одна
        TaskLog taskLog = getUncompletedTask(task.getUser());
        if (taskLog != null) {
            taskLog.setStop(LocalDateTime.now());
            taskLogRepository.save(taskLog);
        }
        TaskLog newTaskLog = new TaskLog(task, LocalDateTime.now());
        return taskLogRepository.save(newTaskLog);
    }

    public TaskLog getUncompletedTask(User user) {
        return taskLogRepository.getUncompletedTask(user.getId());
    }

    public List<TaskLog> getReport(User user, LocalDateTime day) {
        LocalDateTime start = day.with(LocalTime.MIN);
        LocalDateTime stop = day.with(LocalTime.MAX);
        return taskLogRepository.getReportByPeriod(user.getId(), start, stop);
    }

    public List<TaskLog> getReport(User user, LocalDateTime start, LocalDateTime stop) {
        List<TaskLog> reportByPeriod = taskLogRepository.getReportByPeriod(user.getId(), start, stop);
        reportByPeriod.forEach(tl -> {
            if (tl.getStop() == null) {
                tl.setStop(LocalDateTime.now());
            }
        });
        return reportByPeriod;
    }

    public List<TaskLog> findAllByTaskToday(Task task) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.with(LocalTime.MIN);
        LocalDateTime stop = now.with(LocalTime.MAX);
        return taskLogRepository.findAllByTaskIdAndPeriod(task.getId(), start, stop);
    }

    @Transactional
    public void deleteTask(Task task) {
        taskLogRepository.deleteAllByTask(task);
        taskRepository.delete(task);
    }
}
