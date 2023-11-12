package ru.pascalcode.tasktracker.model;

public enum State {
    HOME(0, "Home"),
    SETTINGS(1, "Settings"),
    DELETE(2, "Delete"),
    STATIC_TASKS(3, "Static tasks"),
    EDIT(4, "Edit"),
    EDIT_CONFIRM(5, "Confirm editing");

    final Integer id;
    final String name;

    State(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
