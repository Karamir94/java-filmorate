package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest extends User {

    private User user;
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeEach
    public void createUser() {
        user = new User();
        user.setEmail("xx@xx.ru");
        user.setLogin("xxx");
    }

    @Test
    public void shouldSetEmail() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "email");
    }

    @Test
    public void shouldErrorSetEmail() {
        user.setEmail("xxxx.ru");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "email should contain @");
    }

    @Test
    public void shouldSetLogin() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "login");
    }

    @Test
    public void shouldErrorSetLogin() {
        user.setLogin("  ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "login should be not blank");
    }

    @Test
    public void shouldSetName() {
        user.setName("xxx");

        assertEquals("xxx", user.getName(), "name");
    }

    @Test
    public void shouldSetNameEqualsLogin() {
        assertEquals(user.getLogin(), user.getName(), "name not equals to login");
    }

    @Test
    public void shouldSetBirthDay() {
        user.setBirthday(LocalDate.of(1999, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "BirthDay");
    }

    @Test
    public void shouldErrorSetBirthDay() {
        user.setBirthday(LocalDate.of(2999, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "BirthDay");
    }
}
