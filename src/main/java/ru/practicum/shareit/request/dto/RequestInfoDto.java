package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestInfoDto {

    private final Long id;
    private final String description;
    private final LocalDateTime created;
}
