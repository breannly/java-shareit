package ru.practicum.shareit.item.repo;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepoInMemoryImpl implements ItemRepo {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public List<Item> getAllById(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item save(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item foundItem = items.get(item.getId());

        if (item.getName() != null) {
            foundItem.setName(item.getName());
        }
        if (item.getAvailable() != null) {
            foundItem.setAvailable(item.getAvailable());
        }
        if (item.getDescription() != null) {
            foundItem.setDescription(item.getDescription());
        }
        items.put(item.getId(), foundItem);

        return foundItem;
    }

    @Override
    public Item getById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return items.values().stream()
                    .filter(item -> item.getName() != null && item.getDescription() != null)
                    .filter(item -> stringStr(item.getName(), text) || stringStr(item.getDescription(), text))
                    .filter(Item::getAvailable)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean checkItemExists(Long itemId) {
        return items.containsKey(itemId);
    }

    //  temporary implementation of substring-to-string search
    private boolean stringStr(String string, String substring) {
        for (int i = 0; i < string.length() - substring.length(); i++) {
            int j;
            for (j = 0; j < substring.length(); j++) {
                if (substring.toLowerCase().charAt(j) != string.toLowerCase().charAt(i + j)) {
                    break;
                }
            }
            if (j == substring.length()) {
                return true;
            }
        }
        return false;
    }
}
