package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface ItemService {

    List<ItemInfoDto> getItems(Long userId, int from, int size);

    ItemDto saveItem(Long userId, ItemDto itemDto);

    CommentInfoDto saveComment(CommentDto commentDto, Long userId, Long itemId);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemInfoDto getItem(Long itemId, Long userId);

    List<ItemDto> searchItems(String text, Long userId, int from, int size);
}
