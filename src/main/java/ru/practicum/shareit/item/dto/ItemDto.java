package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.common.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class ItemDto {

    private final Long id;

    @NotBlank(groups = {Create.class})
    private final String name;

    @NotBlank(groups = {Create.class})
    private final String description;

    @NotNull(groups = {Create.class})
    private final Boolean available;

    private final Long requestId;
}
