package ru.pascalcode.tasktracker.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.pascalcode.tasktracker.bot.updatehandler.home.report.ReportUtils;

import java.text.DecimalFormat;

@Data
@AllArgsConstructor
public class TaskLogDto {

    private String name;

    private Long millis;

    private Long taskId;

    @Override
    public String toString() {
        return name + ":   " + getTime() + "   (" + getDecimal() + ")   " + (taskId != 0 ? "/" + taskId : "");
    }

    private String getTime() {
        return ReportUtils.getTimeFromMillis(millis);
    }

    private Double getDecimal() {
        return Double.parseDouble(new DecimalFormat("#0.00").format((double) millis / 3_600_000));
    }
}
