package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@Getter
@Setter
public class UsersRepository { // // класс для хранения базы пользователей
    private long id;
    private HashMap<Long, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger("UsersRepository");

    public long generateId() {
        return ++id;
    }

    public void create(User user) throws UserAlreadyExistException {
        if (users.containsValue(user)) {
            log.debug("Данный пользователь уже зарегестрирован");
            throw new UserAlreadyExistException();

        } else {
            user.setId(generateId());
            users.put(user.getId(), user);
            log.debug("Текущее количество пользователей: {}", users.size());
        }
    }

    public User update(User user) throws ResponseStatusException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return users.get(user.getId());
    }
}
