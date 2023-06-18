package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;
    @NonNull
    @Email
    private String email; // электронная почта не может быть пустой и должна содержать символ @
    @NonNull
    @NotBlank
    private String login; // логин не может быть пустым и содержать пробелы;
    private String name; // имя для отображения может быть пустым — в таком случае будет использован логин;
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday; // дата рождения не может быть в будущем

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
