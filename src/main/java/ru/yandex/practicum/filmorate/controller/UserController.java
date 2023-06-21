package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UsersRepository;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger("UserController");

    private final UsersRepository users = new UsersRepository();

    @GetMapping()
    public List<User> getUsers() {
        log.info("Получен GET запрос");
        return users.getAll();
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) throws UserAlreadyExistException {
        log.info("Получен POST запрос");
        users.create(user);
        return user;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) throws ResponseStatusException {
        log.info("Получен PUT запрос");
        users.update(user);
        return user;
    }
}
