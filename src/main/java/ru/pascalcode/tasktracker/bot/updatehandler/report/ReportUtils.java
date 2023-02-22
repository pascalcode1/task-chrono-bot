package ru.pascalcode.tasktracker.bot.updatehandler.report;

import ru.pascalcode.tasktracker.bot.dto.TaskLogDto;
import ru.pascalcode.tasktracker.model.TaskLog;

import java.sql.Time;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ReportUtils {

    public static List<TaskLogDto> toDtoList(List<TaskLog> taskLogList) {
        return taskLogList.stream()
                .collect(Collectors.groupingBy(tl -> tl.getTask().getName()))
                .entrySet()
                .stream()
                .map(entry -> getTimeForTaskByTaskLog(entry.getKey(), entry.getValue()))
                .toList();
    }

    public static TaskLogDto getTimeForTaskByTaskLog(String taskName, List<TaskLog> taskLogList) {
        validateTaskLogList(taskName, taskLogList);
        long summaryMillis = getTotalMillis(taskLogList);
        double timeDecimal = Double.parseDouble(new DecimalFormat("#0.00").format((double) summaryMillis / 3_600_000));
        //TODO не понимаю почему Time прибавляет мне 6 часов, позже разберусь.
        return new TaskLogDto(taskName, new Time(summaryMillis - 21_600_000), timeDecimal);
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
                .map(taskLogDto -> taskLogDto.getTime().getTime() + 21_600_000)
                .collect(Collectors.summarizingLong(Long::longValue))
                .getSum();
        double timeDecimal = Double.parseDouble(new DecimalFormat("#0.00").format((double) totalMillis / 3_600_000));
        return new TaskLogDto("\nВсего", new Time(totalMillis - 21_600_000), timeDecimal);
    }

}
