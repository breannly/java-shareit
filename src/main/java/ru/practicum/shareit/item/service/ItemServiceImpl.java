package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.entity.GeneratorId;
import ru.practicum.shareit.entity.ValidateService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepo itemRepo;
    private final ItemMapper itemMapper;
    private final GeneratorId generatorId;
    private final ValidateService validateService;

    @Override
    public List<ItemDto> getAllById(Long userId) {
        return itemRepo.getAllById(userId).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto save(Long userId, ItemDto itemDto) {
        validateService.checkUserExists(userId);
        Item item = itemMapper.toItem(generatorId.generate(), userId, itemDto);
        return itemMapper.toDto(itemRepo.save(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        validateService.checkUserExists(userId);
        validateService.checkItemExists(itemId);
        validateService.checkItemOwner(itemId, userId);
        Item item = itemMapper.toItem(itemId, userId, itemDto);
        return itemMapper.toDto(itemRepo.update(item));
    }

    @Override
    public ItemDto getById(Long itemId, Long userId) {
        validateService.checkUserExists(userId);
        validateService.checkItemExists(itemId);
        return itemMapper.toDto(itemRepo.getById(itemId));
    }

    @Override
    public List<ItemDto> search(String text, Long userId) {
        validateService.checkUserExists(userId);
        return itemRepo.search(text).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }
}
