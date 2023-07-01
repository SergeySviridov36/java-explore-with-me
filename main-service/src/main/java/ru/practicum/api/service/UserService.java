package ru.practicum.api.service;

import ru.practicum.model.dto.users.NewUserRequest;
import ru.practicum.model.dto.users.UsersDto;

import java.util.List;

public interface UserService {

    List<UsersDto> findAllUsers(List<Long> ids, Integer from, Integer size);

    UsersDto addUser(NewUserRequest userRequest);

    void deleteUser(long userId);
}