package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(toList());
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User updatedUser = userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("User doesn't exists"));
        fillUser(updatedUser, userDto);
        return UserMapper.toDto(userRepository.save(updatedUser));
    }

    private void fillUser(User user, UserDto userDto) {
        String name = userDto.getName();
        if (name != null) {
            user.setName(name);
        }
        String email = userDto.getEmail();
        if (email != null) {
            user.setEmail(email);
        }
    }

    @Override
    public UserDto getById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("User doesn't exists"));
        return UserMapper.toDto(user);
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
