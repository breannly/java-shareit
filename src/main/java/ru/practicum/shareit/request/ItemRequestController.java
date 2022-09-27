package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public RequestInfoDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestInfoDto> getRequestsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getRequestsByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestInfoDto> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(value = "from", defaultValue = "0")
                                                @PositiveOrZero int from,
                                                @RequestParam(value = "size", defaultValue = "10")
                                                @Positive int size) {
        return itemRequestService.getRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestInfoDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
