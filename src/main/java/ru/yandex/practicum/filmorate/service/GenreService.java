package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> findAllGenres() {
        return genreRepository.getAll();
    }

    public Genre getById(int id) {
        checkId(id);
        return genreRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Жанра с id " + id + " нет в базе"));
    }

    private void checkId(long userId) {
        if (userId <= 0) {
            throw new NotFoundException("id must be positive");
        }
    }

    public List<Genre> getFilmGenres(int filmId) {
        checkId(filmId);
        return genreRepository.getFilmGenre(filmId);
    }

    public Genre createGenre(Genre genre) {
        return genreRepository.createNewGenre(genre);
    }
}
