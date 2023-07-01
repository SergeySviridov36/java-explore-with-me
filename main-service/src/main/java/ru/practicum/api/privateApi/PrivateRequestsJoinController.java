package ru.practicum.api.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.service.ParticipationRequestService;
import ru.practicum.model.dto.event.ParticipationRequestDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateRequestsJoinController {

    private final ParticipationRequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable Long userId,
                                                              @RequestParam Long eventId) {
        log.debug("Получен запрос POST /users/{userId}/requests");
        return requestService.createParticipationRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> findParticipationRequest(@PathVariable Long userId) {
        log.debug("Получен запрос POST /users/{userId}/requests");
        return requestService.findParticipationRequest(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto revokeParticipationRequest(@PathVariable Long userId,
                                                              @PathVariable Long requestId) {
        log.debug("Получен запрос POST /users/{userId}/requests/{requestId}/cancel");
        return requestService.revokeParticipationRequest(userId, requestId);
    }
}