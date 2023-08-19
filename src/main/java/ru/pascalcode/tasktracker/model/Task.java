package ru.pascalcode.tasktracker.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Boolean showOnButtonBar;

    private Boolean staticTask;

    public Task(User user, String name) {
        this.user = user;
        this.name = name;
        this.showOnButtonBar = user.getAddNewTasksToButtonBar();
        this.staticTask = false;
    }

    public Task(User user, String name, Boolean showOnButtonBar, Boolean staticTask) {
        this.user = user;
        this.name = name;
        this.showOnButtonBar = showOnButtonBar;
        this.staticTask = staticTask;
    }
}
