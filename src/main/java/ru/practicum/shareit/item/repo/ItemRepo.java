package ru.practicum.shareit.item.repo;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepo {

    List<Item> getAllById(Long userId);

    Item save(Item item);

    Item update(Item item);

    Item getById(Long itemId);

    List<Item> search(String text);

    boolean checkItemExists(Long itemId);
}
