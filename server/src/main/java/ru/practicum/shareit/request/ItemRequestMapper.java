package ru.practicum.shareit.request;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto,
                                               User requestor) {
        ItemRequest request = new ItemRequest();
        request.setDescription(itemRequestDto.getDescription());
        request.setRequestor(requestor);
        return request;
    }

    public static ItemRequestInfoDto mapToItemRequestInfoDto(ItemRequest request) {
        return new ItemRequestInfoDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                mapToItemInfoDto(request.getItems())
        );
    }

    public static ItemRequestInfoDto.ItemInfoDto mapToItemInfoDto(Item item) {
        return new ItemRequestInfoDto.ItemInfoDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest().getId()
        );
    }

    public static List<ItemRequestInfoDto.ItemInfoDto> mapToItemInfoDto(List<Item> items) {
        return items
                .stream()
                .map(ItemRequestMapper::mapToItemInfoDto)
                .collect(Collectors.toList());
    }

    public static RequestInfoDto mapToRequestInfoDto(ItemRequest itemRequest) {
        return new RequestInfoDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }
}
