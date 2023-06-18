package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmTest {

    Film film;

    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeEach
    public void createUser() {
        film = new Film();
        film.setName("xxx");
        film.setDuration(80);
    }

    @Test
    public void shouldSetName() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "name");
    }

    @Test
    public void shouldErrorSetName() {
        film.setName("  ");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "name");
    }

    @Test
    public void shouldSetDescription() {
        film.setDescription("xxx");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "max description = 200 symbols");
    }

    @Test
    public void shouldErrorSetDescription() {
        film.setDescription("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "max description = 200 symbols");
    }

    @Test
    public void shouldSetReleaseDate() {
        film.setReleaseDate(new Date(99, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "ReleaseDate after 1895.12.28");
    }

    @Test
    public void shouldSetReleaseDate1() {
        film.setReleaseDate(new Date(-4, 0, -3));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "ReleaseDate after 1895.12.28");
    }

    @Test
    public void shouldErrorReleaseDate() {
        assertThrows(ValidationException.class, () -> {
            film.setReleaseDate(new Date(-99, 1, 1));
        }, "ReleaseDate after 1895.12.28");
    }

    @Test
    public void shouldSetDuration() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "Duration must be positive");
    }

    @Test
    public void shouldErrorSetDuration() {
        film.setDuration(-80);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Duration must be positive");
    }
}
