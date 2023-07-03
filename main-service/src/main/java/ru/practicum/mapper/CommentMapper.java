package ru.practicum.mapper;

import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.model.dto.comment.CommentDto;
import ru.practicum.model.dto.comment.NewCommentDto;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment inComment(User user, Event event, NewCommentDto commentDto) {
        return Comment
                .builder()
                .text(commentDto.getText())
                .created(LocalDateTime.now())
                .user(user)
                .event(event)
                .build();
    }

    public static CommentDto inCommentDto(Comment comment) {
        return CommentDto
                .builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .userName(comment.getUser().getName())
                .eventId(comment.getEvent().getId())
                .build();
    }
}