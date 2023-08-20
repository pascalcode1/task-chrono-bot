package ru.pascalcode.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByUserAndName(User user, String name);

    List<Task> findAllByUserAndShowOnButtonBarAndStaticTaskOrderByIdAsc(User user, boolean showOnButtonBar, boolean staticTask);

    List<Task> findAllByUser(User user);

    Task findByUserAndId(User user, Long id);
}
