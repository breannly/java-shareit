package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ItemRequestInfoDto {

    private final Long id;
    private final String description;
    private final LocalDateTime created;
    private final List<ItemInfoDto> items;

    @lombok.Getter
    @lombok.Setter
    @lombok.RequiredArgsConstructor
    public static class ItemInfoDto {

        private final Long id;
        private final String name;
        private final String description;
        private final Boolean available;
        private final Long requestId;
    }
}
