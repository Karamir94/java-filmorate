package ru.yandex.practicum.filmorate.repository;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.repository.film.DBFilmRepository;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DBFilmRepositoryTest {

    @Autowired
    private DBFilmRepository filmRepository;

    @Test
    public void shouldCreateFilm() {
        Set<Genre> genres = new LinkedHashSet<>();
        genres.add(new Genre(1, null));

        Film film = Film.builder()
                .name("film1")
                .description("some description")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(80)
                .mpa(new RatingMPA(1, "G"))
                .build();
        film.setGenres(genres);

        Film film2 = filmRepository.create(film);

        assertThat(film2)
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "film1")
                .hasFieldOrPropertyWithValue("description", "some description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1999, 9, 9))
                .hasFieldOrPropertyWithValue("duration", 80);

        Integer mpaCatId = film2.getMpa().getId();

        assertThat(mpaCatId)
                .isEqualTo(1);
    }

    @Test
    public void testUpdateFilm() {
        Set<Genre> genres = new LinkedHashSet<>();
        genres.add(new Genre(1, null));
        genres.add(new Genre(2, null));

        Film film2 = Film.builder()
                .id(1L)
                .name("FILM2")
                .description("NEW_DESC")
                .releaseDate(LocalDate.of(1991, 2, 6))
                .duration(100)
                .mpa(new RatingMPA(2, "PG"))
                .build();
        film2.setGenres(genres);

        Film backedFilm = filmRepository.update(film2);

        assertThat(backedFilm)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "FILM2")
                .hasFieldOrPropertyWithValue("description", "NEW_DESC")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1991, 2, 6))
                .hasFieldOrPropertyWithValue("duration", 100);

        Integer mpaCatId = backedFilm.getMpa().getId();

        assertThat(mpaCatId)
                .isEqualTo(2);
    }

    @Test
    public void testGetAllFilms() {
        List<Film> filmList = filmRepository.getAll();

        assertThat(filmList.size())
                .isEqualTo(1);
    }
}
