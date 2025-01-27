package ru.pascalcode.tasktracker.bot.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.pascalcode.tasktracker.bot.updatehandler.home.report.ReportUtils;
import ru.pascalcode.tasktracker.model.Task;

import java.text.DecimalFormat;

@Data
@AllArgsConstructor
public class TaskLogDto implements Comparable<TaskLogDto>{

    private String name;

    private Long millis;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

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

    @Override
    public int compareTo(@NotNull TaskLogDto o) {
        return (int) (this.getMillis() - o.getMillis());
    }
}
