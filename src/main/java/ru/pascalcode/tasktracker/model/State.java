package ru.pascalcode.tasktracker.model;

public enum State {
    HOME(0, "Home"),
    SETTINGS(1, "Settings"),
    DELETE(2, "Delete tasks"),
    STATIC_TASKS(3, "Static tasks"),
    EDIT_TASKS(4, "Edit"),
    EDIT_CONFIRM(5, "Confirm editing"),
    EDIT_TIME_RECORDS_LIST(6, "Edit time records list"),
    EDIT_TIME_RECORD(7, "Edit time record"),
    EDIT_TIME_RECORD_CONFIRM(8, "Confirm time editing of time record");

    final Integer id;
    final String name;

    State(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
