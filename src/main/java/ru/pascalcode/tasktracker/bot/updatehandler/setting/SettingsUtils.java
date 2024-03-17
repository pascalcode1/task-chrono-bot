package ru.pascalcode.tasktracker.bot.updatehandler.setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsUtils {
    private static final Map<Integer, String> weekMap = new HashMap<>();

    static {
        weekMap.put(1, "Monday");
        weekMap.put(2, "Tuesday");
        weekMap.put(3, "Wednesday");
        weekMap.put(4, "Thursday");
        weekMap.put(5, "Friday");
        weekMap.put(6, "Saturday");
        weekMap.put(7, "Sunday");
    }

    public static String getNameOfDay(int dayNumber) {
        if (dayNumber < 1 || dayNumber > 7) {
            throw new RuntimeException("dayNumber must be in range from 1 to 7");
        }
        return weekMap.get(dayNumber);
    }

    public static List<String> getWeekDayNames() {
        return weekMap.values()
                      .stream()
                      .toList();
    }

    public static int getDayIndexByName(String dayName) {
        validateDayOfWeek(dayName);
        return weekMap.entrySet()
                      .stream()
                      .filter(day -> day.getValue().equals(dayName))
                      .map(Map.Entry::getKey)
                      .findFirst()
                      .get();
    }

    private static void validateDayOfWeek(String dayName) {
        if (!weekMap.values().stream().toList().contains(dayName)) {
            throw new RuntimeException("Unknown day of week: \"" + dayName + "\"");
        }
    }

    public static boolean containsOnlyDigit(String text) {
        return !text.isEmpty() && !text.contains("[a-zA-Z]+");
    }
}
