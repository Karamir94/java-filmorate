package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.List;
import java.util.Optional;

public interface RatingMPARepository {

    Optional<RatingMPA> getById(int id);

    List<RatingMPA> getAll();

    RatingMPA createNewRatingMPA(RatingMPA rating);
}
