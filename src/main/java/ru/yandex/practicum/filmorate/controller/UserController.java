package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UsersBase;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger("UserController");

    private final UsersBase users = new UsersBase();

    @GetMapping()
    public List<User> getUsers() {
        log.info("Получен GET запрос");
        return new ArrayList<>(users.getUsers().values());
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) throws UserAlreadyExistException {
        log.info("Получен POST запрос");
        user.setId(users.generateId());
        if (users.getUsers().containsKey(user.getId())) {
            log.debug("Данный пользователь уже зарегестрирован");
            throw new UserAlreadyExistException();
        } else {
            HashMap<Long, User> usersFromBase = users.getUsers();
            usersFromBase.put(user.getId(), user);
            users.setUsers(usersFromBase);
            log.debug("Текущее количество пользователей: {}", users.getUsers().size());
        }
        return users.getUsers().get(user.getId());
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) throws ResponseStatusException {
        log.info("Получен PUT запрос");
        HashMap<Long, User> usersFromBase = users.getUsers();
        if (usersFromBase.containsKey(user.getId())) {
            usersFromBase.put(user.getId(), user);
            users.setUsers(usersFromBase);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }
}
