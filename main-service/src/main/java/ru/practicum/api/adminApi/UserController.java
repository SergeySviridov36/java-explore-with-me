package ru.practicum.api.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.users.NewUserRequest;
import ru.practicum.model.dto.users.UsersDto;
import ru.practicum.api.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<UsersDto> findAllUsers(@RequestParam(required = false) List<Long> ids,
                                       @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                       @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.debug("Получен запрос GET /admin/users");
        return userService.findAllUsers(ids, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public UsersDto addUser(@Valid @RequestBody NewUserRequest userRequest) {
        log.debug("Получен запрос POST /admin/users");
        return userService.addUser(userRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") long userId) {
        log.debug("Получен запрос DELETE /admin/users/{userId}");
        userService.deleteUser(userId);
    }
}