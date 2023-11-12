package ru.pascalcode.tasktracker.bot;

import java.util.List;

public class CommandUtils {

    static final List<String> commands = List.of("start", "delete", "settings", "statictasks");

    public static boolean isCommand(String text) {
        if (!text.startsWith("/")) {
            return false;
        }
        return commands.contains(text.substring(1));
    }
}
