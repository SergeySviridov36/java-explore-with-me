package ru.practicum.mapper;

import ru.practicum.model.dto.users.NewUserRequest;
import ru.practicum.model.dto.users.UserShortDto;
import ru.practicum.model.dto.users.UsersDto;
import ru.practicum.model.User;

public class UserMapper {

    public static UsersDto inUserDto(User user) {
        return new UsersDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static UserShortDto inUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static User inUser(NewUserRequest userRequest) {
        return new User(
                null,
                userRequest.getName(),
                userRequest.getEmail()
        );
    }
}