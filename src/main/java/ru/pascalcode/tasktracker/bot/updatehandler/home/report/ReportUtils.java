package ru.pascalcode.tasktracker.bot.updatehandler.home.report;

import ru.pascalcode.tasktracker.bot.dto.TaskLogDto;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.TaskLog;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ReportUtils {

    public static List<TaskLogDto> toDtoList(List<TaskLog> taskLogList) {
        return taskLogList.stream()
                .collect(Collectors.groupingBy(TaskLog::getTask))
                .entrySet()
                .stream()
                .map(entry -> getTimeForTaskByTaskLog(entry.getKey(), entry.getValue()))
                .toList();
    }

    public static TaskLogDto getTimeForTaskByTaskLog(Task task, List<TaskLog> taskLogList) {
        validateTaskLogList(task.getName(), taskLogList);
        long summaryMillis = getTotalMillis(taskLogList);
        return new TaskLogDto(task.getName(), summaryMillis, task.getId());
    }

    public static String getTimeFromMillis(long millis) {
        int hour = (int) (millis / 3600000) % 60;
        int min = (int) (millis / 60000) % 60;
        return hour + ":" + (String.valueOf(min).length() == 1 ? "0" + min : String.valueOf(min));
    }

    public static long getTotalMillis(List<TaskLog> taskLogList) {
        return taskLogList.stream().map(tl -> {
                    long startMilli = tl.getStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    long stopMilli = tl.getStop().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    return stopMilli - startMilli;
                })
                .collect(Collectors.summarizingLong(Long::longValue))
                .getSum();
    }

    private static void validateTaskLogList(String taskName, List<TaskLog> taskLogList) {
        boolean isValid = taskLogList.stream()
                .filter(taskLog -> !taskLog.getTask().getName().equals(taskName))
                .toList().isEmpty();
        if (!isValid) {
            throw new RuntimeException("\"taskLogList\" mast include only tasks with task name \"taskName\""); //TODO WATT??
        }
    }

    public static TaskLogDto getTotal(List<TaskLogDto> taskLogDtoList) {
        long totalMillis = taskLogDtoList.stream()
                .map(TaskLogDto::getMillis)
                .collect(Collectors.summarizingLong(Long::longValue))
                .getSum();
        return new TaskLogDto("\nTotal", totalMillis, 0L);
    }

}
