package ru.pascalcode.tasktracker.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramId;
    private String userName;
    private String firstName;
    private String lastName;
    private Integer minWeekHours;
    private Integer firstDayOfWeek;
    private Boolean weekHoursStat;
    private State state;
    private String meta;

    public User(Long telegramId) {
        this.telegramId = telegramId;
        setDefaults();
    }

    public User(Long telegramId, String userName, String firstName, String lastName) {
        this.telegramId = telegramId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        setDefaults();
    }

    private void setDefaults() {
        this.minWeekHours = 40;
        this.firstDayOfWeek = 1;
        this.weekHoursStat = false;
        this.state = State.HOME;
    }
}
