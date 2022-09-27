package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;

import java.util.List;

public interface ItemRequestService {

    RequestInfoDto addRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestInfoDto> getRequestsByUser(Long userId);

    List<ItemRequestInfoDto> getRequests(Long userId, int from, int size);

    ItemRequestInfoDto getRequestById(Long userId, Long requestId);
}
