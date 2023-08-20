package ru.pascalcode.tasktracker.bot;

import java.util.List;

public class StringHelper {

    static final List<String> commands = List.of("start", "about", "complete", "delete", "settings", "statictasks");

    public static boolean isCommand(String text) {
        if (!text.startsWith("/")) {
            return false;
        }
        return commands.contains(text.substring(1));
    }
}
