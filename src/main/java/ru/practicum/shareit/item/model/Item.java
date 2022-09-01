package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.entity.BaseEntity;

@Getter
@Setter
public class Item extends BaseEntity {

    private Long ownerId;

    private String name;

    private String description;

    private Boolean available;
}
