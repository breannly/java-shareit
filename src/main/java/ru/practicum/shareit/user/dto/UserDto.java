package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.entity.BaseEntity;
import ru.practicum.shareit.entity.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto extends BaseEntity {
    @NotBlank(groups = {Create.class})
    private String name;

    @Email(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String email;
}
