package ru.pascalcode.tasktracker.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pascalcode.tasktracker.model.User;
import ru.pascalcode.tasktracker.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
