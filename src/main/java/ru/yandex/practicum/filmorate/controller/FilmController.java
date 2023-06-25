package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmsRepository;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger("FilmController");

    private final FilmsRepository films = new FilmsRepository();

    @GetMapping()
    public List<Film> getUsers() {
        log.info("Получен GET запрос");
        return films.getAll();
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) throws UserAlreadyExistException {
        log.info("Получен POST запрос");
        films.create(film);
        return film;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) throws ResponseStatusException {
        log.info("Получен PUT запрос");
        films.update(film);
        return film;
    }
}
