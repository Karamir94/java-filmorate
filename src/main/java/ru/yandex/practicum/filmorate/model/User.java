package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {

    @EqualsAndHashCode.Exclude
    private long id;
    @NotBlank
    @Email
    private String email; // электронная почта не может быть пустой и должна содержать символ @
    @NotBlank
    private String login; // логин не может быть пустым и содержать пробелы;
    @EqualsAndHashCode.Exclude
    private String name; // имя для отображения может быть пустым — в таком случае будет использован логин;
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday; // дата рождения не может быть в будущем
    private boolean isAddedFriend; // true для подтвержденной заявки

    public void setName(String name) {
        if (name.isBlank() || name.isEmpty()) {
            this.name = login;
        } else {
            this.name = name;
        }
    }

    public String getName() {
        if (name == null) {
            name = login;
        }
        return name;
    }
}
