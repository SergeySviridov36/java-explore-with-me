package ru.practicum.api.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.service.CommentsService;
import ru.practicum.model.dto.comment.CommentDto;
import ru.practicum.model.dto.comment.NewCommentDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentsController {

    private final CommentsService commentsService;

    @PostMapping
    public CommentDto createComment(@RequestBody NewCommentDto newCommentDto,
                                    @PathVariable Long userId,
                                    @RequestParam Long eventId) {
        log.debug("Получен запрос POST /users/{userId}/comments");
        return commentsService.createComment(newCommentDto, userId, eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@RequestBody NewCommentDto updateCommentDto,
                                    @PathVariable Long userId,
                                    @PathVariable Long commentId) {
        log.debug("Получен запрос PATH /users/{userId}/comments/{commentId}");
        return commentsService.updateComment(updateCommentDto, commentId, userId);
    }

    @GetMapping("/{commentId}")
    public CommentDto findCommentById(@PathVariable Long userId,
                                      @PathVariable Long commentId) {
        log.debug("Получен запрос GET /users/{userId}/comments/{commentId}");
        return commentsService.findCommentById(userId, commentId);
    }

    @GetMapping("/events/{eventId}")
    public List<CommentDto> findComment(@PathVariable Long userId,
                                        @PathVariable Long eventId) {
        log.debug("Получен запрос GET /users/{userId}/comments/events/{eventId}");
        return commentsService.findComment(userId, eventId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable(value = "userId") Long userId,
                              @PathVariable(value = "commentId") Long commentId) {
        commentsService.deleteComment(commentId, userId);
    }
}