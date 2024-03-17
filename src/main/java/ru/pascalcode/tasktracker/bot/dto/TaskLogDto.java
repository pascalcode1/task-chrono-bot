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

    public String getTime() {
        return ReportUtils.getTimeFromMillis(millis);
    }

    public String getDecimal() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        double hours = (double) millis / 3_600_000;
        double formattedHours = Double.parseDouble(decimalFormat.format(hours));
        String string = String.valueOf(formattedHours);
        if (string.substring(string.indexOf(".")).length() < 3) {
            string = string + 0;
        }
        return string;
    }
}
