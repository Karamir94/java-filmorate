package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validator.RealiseDateConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Film {

    @EqualsAndHashCode.Exclude
    private long id;
    @NotBlank
    private String name; // название не может быть пустым
    @NonNull
    @Size(max = 200, message = "Максимальная длина описания 200 символов")
    @EqualsAndHashCode.Exclude
    private String description; // максимальная длина описания — 200 символов
    @NonNull
    @EqualsAndHashCode.Include
    @RealiseDateConstraint
    @JsonFormat(pattern = "yyyy-MM-dd") // custom annotation since 28.12.95
    private LocalDate releaseDate; // дата релиза — не раньше 28 декабря 1895 года
    @NonNull
    @Positive
    private int duration; // продолжительность фильма должна быть положительной

//    public void setReleaseDate(LocalDate releaseDate) throws ValidationException {
//        if (releaseDate.isAfter(LocalDate.of(1895, 12, 27))) {
//            this.releaseDate = releaseDate;
//        } else {
//            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
//        }
//    }
}