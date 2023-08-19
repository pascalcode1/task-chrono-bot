package ru.pascalcode.tasktracker.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.model.State;
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

    public State getStateOfUser(Update update) {
        return getUser(update.getMessage().getFrom().getId()).getState();
    }
}
