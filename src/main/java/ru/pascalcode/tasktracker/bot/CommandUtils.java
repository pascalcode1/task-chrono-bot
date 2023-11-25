package ru.pascalcode.tasktracker.bot;

import java.util.List;

public class CommandUtils {

    static final List<String> commands = List.of("start", "delete_tasks", "settings", "static_tasks", "edit_tasks", "edit_time_records");

    public static boolean isCommand(String text) {
        if (!text.startsWith("/")) {
            return false;
        }
        return commands.contains(text.substring(1));
    }
}
