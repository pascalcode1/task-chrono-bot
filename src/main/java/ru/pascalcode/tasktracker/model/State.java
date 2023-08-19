package ru.pascalcode.tasktracker.model;

public enum State {
    HOME(0, "Home"),
    SETTINGS(1, "Settings"),
    COMPLETE(2, "Complete"),
    DELETE(3, "Delete"),
    STATIC_TASKS(4, "Static tasks");

    final Integer id;
    final String name;

    State(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
