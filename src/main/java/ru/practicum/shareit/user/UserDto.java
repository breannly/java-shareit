package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(groups = {Create.class})
    private String name;

    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    private String email;
}
