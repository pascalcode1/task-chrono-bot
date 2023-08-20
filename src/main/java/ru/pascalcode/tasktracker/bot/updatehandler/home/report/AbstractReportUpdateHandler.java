package ru.pascalcode.tasktracker.bot.updatehandler.home.report;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.pascalcode.tasktracker.bot.dto.TaskLogDto;
import ru.pascalcode.tasktracker.bot.updatehandler.AbstractUpdateHandler;
import ru.pascalcode.tasktracker.model.TaskLog;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.service.TaskLogService;
import ru.pascalcode.tasktracker.service.TaskService;
import ru.pascalcode.tasktracker.service.UserService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

public abstract class AbstractReportUpdateHandler extends AbstractUpdateHandler {

    private static final String WEEK_PROGRESS = "%s hours out of %s this week (%s%%)";

    protected AbstractReportUpdateHandler(UserService userService, TaskService taskService, TaskLogService taskLogService) {
        super(userService, taskService, taskLogService);
    }

    protected void setReportToAnswer(User user, LocalDateTime reportDay, SendMessage answer) {
        List<TaskLog> taskLogList = taskLogService.getReport(user, reportDay);
        List<TaskLogDto> taskLogDtoList = ReportUtils.toDtoList(taskLogList);
        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add(reportDay.format(DateTimeFormatter.ofPattern("d MMMM yyyy")) + " report\n");
        for (TaskLogDto taskLogDto : taskLogDtoList) {
            stringJoiner.add(taskLogDto.toString());
        }
        stringJoiner.add(ReportUtils.getTotal(taskLogDtoList).toString());
        if (Boolean.TRUE.equals(user.getWeekHoursStat())) {
            stringJoiner.add(getProgressForWeek(user, reportDay));
        }
        answer.setText(stringJoiner.toString());
    }

    public String getProgressForWeek(User user, LocalDateTime reportDay) {
        int firstDayOfWeek = user.getFirstDayOfWeek();
        int plannedHours = user.getMinWeekHours();
        int currentDayOfWeek = reportDay.getDayOfWeek().getValue();
        int dayDiff = currentDayOfWeek >= firstDayOfWeek ?
                currentDayOfWeek - firstDayOfWeek :
                currentDayOfWeek + firstDayOfWeek - 1;
        LocalDateTime start = reportDay.minusDays(dayDiff).with(LocalTime.MIN);
        LocalDateTime stop = reportDay.with(LocalTime.MAX);
        List<TaskLog> taskLogList = taskLogService.getReport(user, start, stop);
        long totalMillis = ReportUtils.getTotalMillis(taskLogList);
        int percent = (int) totalMillis / (plannedHours * 36000);
        String timeFromMillis = ReportUtils.getTimeFromMillis(totalMillis);
        return String.format(WEEK_PROGRESS, timeFromMillis, plannedHours, percent);
    }

}
