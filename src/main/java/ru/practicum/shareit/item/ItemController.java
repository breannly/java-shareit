package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemInfoDto> getAllById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        return itemService.getAllById(userId, from, size);
    }

    @PostMapping
    public ItemDto save(@RequestHeader("X-Sharer-User-Id") Long userId,
                        @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        return itemService.save(userId, itemDto);
    }

    @PostMapping("/{item_id}/comment")
    public CommentInfoDto saveComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Valid @RequestBody CommentDto commentDto,
                                      @PathVariable("item_id") Long itemId) {
        return itemService.saveComment(commentDto, userId, itemId);
    }

    @PatchMapping("/{item_id}")
    public ItemDto update(@PathVariable("item_id") Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{item_id}")
    public ItemInfoDto getById(@PathVariable("item_id") Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text", required = false) String text,
                                @RequestHeader("X-Sharer-User-Id") Long userId,
                                @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        return itemService.search(text, userId, from, size);
    }
}
