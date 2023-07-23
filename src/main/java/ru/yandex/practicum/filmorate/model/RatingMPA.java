package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@Builder
public class RatingMPA {
    @Positive
    private int id;
    private String name;
}
