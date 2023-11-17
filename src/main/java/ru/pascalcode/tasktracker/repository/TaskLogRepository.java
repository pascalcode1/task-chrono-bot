package ru.pascalcode.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.pascalcode.tasktracker.model.Task;
import ru.pascalcode.tasktracker.model.TaskLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {

    @Query(nativeQuery = true, value = """
            select tl.id,
            tl.task_id,
            tl.start,
            tl.stop
            from task_log tl
            join task t on tl.task_id = t.id
            join users u on t.user_id = u.id
            where tl.start >= :from
            and tl.start <= :to
            and u.id = :userId""")
    List<TaskLog> getReportByPeriod(Long userId, LocalDateTime from, LocalDateTime to);

    @Query(nativeQuery = true, value = """
            select tl.id,
            tl.task_id,
            tl.start,
            tl.stop
            from task_log tl
            join task t on tl.task_id = t.id
            where t.user_id = :userId
            and tl.stop is null""")
    TaskLog getUncompletedTask(Long userId);

    @Query(nativeQuery = true, value = """
            select tl.id,
            tl.task_id,
            tl.start,
            tl.stop
            from task_log tl
            where tl.start >= :start
            and tl.stop <= :stop
            and tl.task_id = :taskId""")
    List<TaskLog> findAllByTaskIdAndPeriod(long taskId, LocalDateTime start, LocalDateTime stop);

    void deleteAllByTaskId(long taskId);

    void deleteAllByTask(Task task);
}
