package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ItemRequestServiceImplTest {

    ItemRequestService itemRequestService;
    ItemRequestRepository itemRequestRepository;
    UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository);
    }

    @Test
    void shouldReturnRequestInfoDtoUsingAddRequestWithRightParameters() {
        ItemRequestDto itemRequestDto = new ItemRequestDto("test");

        User user = new User(1L, "test", "test@gmail.com");

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto, user);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.save(Mockito.any()))
                .thenReturn(itemRequest);

        RequestInfoDto request = itemRequestService.addRequest(user.getId(), itemRequestDto);

        Assertions.assertNotNull(request);
        Assertions.assertEquals(itemRequestDto.getDescription(), request.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenWrongUserId() {
        ItemRequestDto itemRequestDto = new ItemRequestDto("test");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> itemRequestService.addRequest(1L, itemRequestDto)
        );
        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldReturnListUsingGetRequestsByUserWithRightParameters() {
        List<ItemRequest> requests = List.of(
                new ItemRequest(1L, "test", LocalDateTime.now(), null, new ArrayList<>())
        );
        User user = new User(1L, "test", "test@gmail.com");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.findItemRequestsByRequestorId(Mockito.anyLong()))
                .thenReturn(requests);

        List<ItemRequestInfoDto> infoDto = itemRequestService.getRequestsByUser(1L);

        Assertions.assertNotNull(infoDto);
        Assertions.assertEquals(1, infoDto.size());
    }

    @Test
    void shouldThrowExceptionUsingGetRequestsByUserWithWrongUserId() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> itemRequestService.getRequestsByUser(1L)
        );
        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionUsingRequestByIdWithRightParameters() {
        User user = new User(1L, "test", "test@gmail.com");
        ItemRequest request = new ItemRequest(1L, "test", LocalDateTime.now(), user, new ArrayList<>());

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.findById(request.getId()))
                .thenReturn(Optional.of(request));

        ItemRequestInfoDto infoDto = itemRequestService.getRequestById(user.getId(), request.getId());
        Assertions.assertNotNull(infoDto);
    }

    @Test
    void shouldThrowExceptionUsingRequestByIdWithWrongUserId() {
        User user = new User(1L, "test", "test@gmail.com");
        ItemRequest request = new ItemRequest(1L, "test", LocalDateTime.now(), user, null);

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> itemRequestService.getRequestById(user.getId(), request.getId()));

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldReturnRequestUsingRequestByIdWithWrongRequestId() {
        User user = new User(1L, "test", "test@gmail.com");
        ItemRequest request = new ItemRequest(1L, "test", LocalDateTime.now(), user, null);

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.findById(request.getId()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> itemRequestService.getRequestById(user.getId(), request.getId()));

        Assertions.assertEquals("Request not found", exception.getMessage());
    }
}