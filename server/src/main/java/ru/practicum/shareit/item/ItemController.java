package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemInfoDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestParam(value = "from") int from,
                                      @RequestParam(value = "size") int size) {
        return itemService.getItems(userId, from, size);
    }

    @PostMapping
    public ItemDto saveItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @RequestBody ItemDto itemDto) {
        return itemService.saveItem(userId, itemDto);
    }

    @PostMapping("/{item_id}/comment")
    public CommentInfoDto saveComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestBody CommentDto commentDto,
                                      @PathVariable("item_id") Long itemId) {
        return itemService.saveComment(commentDto, userId, itemId);
    }

    @PatchMapping("/{item_id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("item_id") Long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{item_id}")
    public ItemInfoDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable("item_id") Long itemId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam(name = "text", required = false) String text,
                                     @RequestParam(value = "from") int from,
                                     @RequestParam(value = "size") int size) {
        return itemService.searchItems(text, userId, from, size);
    }
}
