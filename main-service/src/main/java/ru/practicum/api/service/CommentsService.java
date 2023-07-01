package ru.practicum.api.service;

import ru.practicum.model.dto.comment.CommentDto;
import ru.practicum.model.dto.comment.NewCommentDto;

import java.util.List;

public interface CommentsService {

    CommentDto createComment(NewCommentDto newCommentDto, Long userId, Long eventId);

    CommentDto updateComment(NewCommentDto updateCommentDto, Long commentId, Long userId);

    void deleteComment(Long commentId, Long userId);

    List<CommentDto> findComment(Long userId, Long eventId);

    CommentDto findCommentById(Long userId, Long commentId);
}