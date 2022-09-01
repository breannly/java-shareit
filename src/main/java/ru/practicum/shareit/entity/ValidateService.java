package ru.practicum.shareit.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ObjectNotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.repo.ItemRepo;
import ru.practicum.shareit.user.repo.UserRepo;

@Service
@RequiredArgsConstructor
public class ValidateService {

    private final UserRepo userRepo;
    private final ItemRepo itemRepo;

    public void checkEmailExists(String email) {
        if (userRepo.checkEmailExists(email)) {
            throw new ValidationException("Email is already in use");
        }
    }

    public void checkUserExists(Long userId) {
        if (!userRepo.checkUserExists(userId)) {
            throw new ObjectNotFoundException("User doesn't exists");
        }
    }

    public void checkItemExists(Long itemId) {
        if (!itemRepo.checkItemExists(itemId)) {
            throw new ObjectNotFoundException("Item doesn't exists");
        }
    }

    public void checkItemOwner(Long itemId, Long userId) {
        if (!itemRepo.getById(itemId).getOwnerId().equals(userId)) {
            throw new ObjectNotFoundException("Item has another owner");
        }
    }
}
