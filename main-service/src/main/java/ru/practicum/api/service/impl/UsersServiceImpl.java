package ru.practicum.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.repository.UsersRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.dto.users.NewUserRequest;
import ru.practicum.model.dto.users.UsersDto;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.api.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersServiceImpl implements UserService {

    private final UsersRepository userRepository;

    @Override
    public List<UsersDto> findAllUsers(List<Long> ids, Integer from, Integer size) {
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .map(UserMapper::inUserDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findAllById(ids).stream()
                .map(UserMapper::inUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsersDto addUser(NewUserRequest userRequest) {
        User user = userRepository.save(UserMapper.inUser(userRequest));
        return UserMapper.inUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        if (!userRepository.existsById(userId))
            throw new NotFoundException("Пользователь не найден");
        userRepository.deleteById(userId);
    }
}