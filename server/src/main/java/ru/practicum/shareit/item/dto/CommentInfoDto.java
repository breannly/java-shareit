package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentInfoDto {

    private final Long id;
    private final String text;
    private final String authorName;
    private final LocalDateTime created;
}
