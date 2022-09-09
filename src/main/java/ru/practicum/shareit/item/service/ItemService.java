package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface ItemService {

    List<ItemInfoDto> getAllById(Long userId);

    ItemDto save(Long userId, ItemDto itemDto);

    CommentInfoDto saveComment(CommentDto commentDto, Long userId, Long itemId);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemInfoDto getById(Long itemId, Long userId);

    List<ItemDto> search(String text, Long userId);
}
