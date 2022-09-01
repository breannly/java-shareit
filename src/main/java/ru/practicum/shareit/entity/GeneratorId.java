package ru.practicum.shareit.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GeneratorId {

    private Long id = 0L;

    public Long generate() {
        return ++id;
    }
}
