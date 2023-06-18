package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private long id;
    @NotBlank
    private String name; // название не может быть пустым
    @Size(max = 200)
    private String description; // максимальная длина описания — 200 символов
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate; // дата релиза — не раньше 28 декабря 1895 года
    @Positive
    private int duration; // продолжительность фильма должна быть положительной

    public void setReleaseDate(Date releaseDate) throws ValidationException {
        if (releaseDate.after(new Date(-4, 0, -4))) {
            this.releaseDate = releaseDate;
        } else {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}