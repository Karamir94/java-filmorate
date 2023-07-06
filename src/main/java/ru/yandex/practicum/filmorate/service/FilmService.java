package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository, UserRepository userRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
    }

    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    public void create(Film film) {
        filmRepository.create(film);
    }

    public void update(Film film) {
        filmRepository.update(film);
    }

    public Film get(long filmId) {
        checkId(filmId);
        Film film = filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Film № %d not found", filmId)));

        return film;
    }

    public void addLike(long filmId, long userId) {
        checkId(userId);
        checkId(filmId);

        Film film = get(filmId);
        Set<Long> likes = film.getLikes();

        likes.add(userId);
        film.setLikes(likes);
    }

    public void deleteLike(long filmId, long userId) {
        checkId(userId);
        checkId(filmId);

        Film film = get(filmId);
        Set<Long> likes = film.getLikes();

        likes.remove(userId);
        film.setLikes(likes);
    }

    private void checkId(long userId) {
        if (userId <= 0) {
            throw new NotFoundException("id must be positive");
        }
    }

    public List<Film> getTopFilms(int size) {
        return filmRepository.getAll().stream()
                .sorted((p0, p1) -> compare(p0, p1))
                .limit(size)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        int result = p1.getLikesAmount() - p0.getLikesAmount();
        return result;
    }
}
