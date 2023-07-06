package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info("Получен GET запрос");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        log.info("Получен GET запрос");
        return filmService.get(id);
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен POST запрос");
        filmService.create(film);
        return film;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен PUT запрос");
        filmService.update(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Получен PUT запрос addLike");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Получен DELETE запрос deleteLike");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        log.info("Получен GET запрос getTopFilms");
        return filmService.getTopFilms(count);
    }
}
