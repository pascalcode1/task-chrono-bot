package ru.pascalcode.tasktracker.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByUserAndName(User user, String name);

    List<Task> findAllByUserAndStaticTaskOrderByIdAsc(User user, boolean staticTask);

    List<Task> findAllByUser(User user);

    Task findByUserAndUserTaskId(User user, Long userTaskId);

    List<Task> findByUser(User user, PageRequest pageRequest);

    @Query(nativeQuery = true, value = """
            select max(t.user_task_id)
            from task t
            where t.user_id = :userId""")
    Long findMaxUserTaskId(Long userId);
}
