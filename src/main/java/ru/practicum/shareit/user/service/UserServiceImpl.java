package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.entity.GeneratorId;
import ru.practicum.shareit.entity.ValidateService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepo;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final GeneratorId generatorId;
    private final ValidateService validateService;

    @Override
    public List<UserDto> getAll() {
        return userRepo.getAll().stream()
                .map(userMapper::toDto)
                .collect(toList());
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        validateService.checkEmailExists(user.getEmail());
        user.setId(generatorId.generate());
        return userMapper.toDto(userRepo.save(user));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User user = userMapper.toUser(userDto);
        validateService.checkUserExists(userId);
        if (user.getEmail() != null) {
            validateService.checkEmailExists(user.getEmail());
        }
        return userMapper.toDto(userRepo.update(userId, user));
    }

    @Override
    public UserDto getById(Long userId) {
        return userMapper.toDto(userRepo.getById(userId));
    }

    @Override
    public void delete(Long userId) {
        userRepo.delete(userId);
    }
}
