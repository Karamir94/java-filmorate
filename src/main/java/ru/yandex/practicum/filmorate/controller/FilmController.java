package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsBase;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger("FilmController");

    private final FilmsBase films = new FilmsBase();

    @GetMapping()
    public List<Film> getUsers() {
        log.info("Получен GET запрос");
        return new ArrayList<>(films.getFilms().values());
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) throws UserAlreadyExistException {
        film.setId(films.generateId());
        log.info("Получен POST запрос");
        if (films.getFilms().containsKey(film.getId())) {
            log.debug("Данный фильм уже зарегестрирован");
            throw new UserAlreadyExistException();
        } else {
            HashMap<Long, Film> filmsFromBase = films.getFilms();
            filmsFromBase.put(film.getId(), film);
            films.setFilms(filmsFromBase);
            log.debug("Текущее количество фильмов: {}", films.getFilms().size());
        }
        return films.getFilms().get(film.getId());
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) throws ResponseStatusException {
        log.info("Получен PUT запрос");
        HashMap<Long, Film> filmsFromBase = films.getFilms();
        if (filmsFromBase.containsKey(film.getId())) {
            filmsFromBase.put(film.getId(), film);
            films.setFilms(filmsFromBase);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return film;
    }
}
