package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.entity.BaseEntity;

@Getter
@Setter
public class User extends BaseEntity {

    private String name;

    private String email;
}
