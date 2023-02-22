package ru.pascalcode.tasktracker.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;

@Data
@AllArgsConstructor
public class TaskLogDto {

    private String name;

    private Time time;

    private Double decimalTime;

    @Override
    public String toString() {
        return name + ":   " + time + "   (" + decimalTime + ")";
    }
}
