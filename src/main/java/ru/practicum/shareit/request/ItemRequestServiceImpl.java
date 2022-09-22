package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public RequestInfoDto addRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User not found"));
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto, user);
        return ItemRequestMapper.mapToRequestInfoDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestInfoDto> getRequestsByUser(Long userId) {
        userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User not found"));
        return itemRequestRepository.findItemRequestsByRequestorId(userId)
                .stream()
                .map(ItemRequestMapper::mapToItemRequestInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestInfoDto> getRequests(Long userId, int from, int size) {
        userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User not found"));
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return itemRequestRepository.findRequests(userId, pageRequest)
                .stream()
                .map(ItemRequestMapper::mapToItemRequestInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestInfoDto getRequestById(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("User not found"));
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(()
                -> new ObjectNotFoundException("Request not found"));
        return ItemRequestMapper.mapToItemRequestInfoDto(request);
    }
}
