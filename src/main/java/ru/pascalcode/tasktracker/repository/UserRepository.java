package ru.pascalcode.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pascalcode.tasktracker.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByTelegramId(Long telegramId);
}
