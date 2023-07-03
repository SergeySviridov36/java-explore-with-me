package ru.practicum.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.repository.CommentRepository;
import ru.practicum.api.repository.EventRepository;
import ru.practicum.api.repository.UsersRepository;
import ru.practicum.api.service.CommentsService;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.model.dto.comment.CommentDto;
import ru.practicum.model.dto.comment.NewCommentDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final EventRepository eventRepository;
    private final UsersRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDto createComment(NewCommentDto newCommentDto, Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        return CommentMapper.inCommentDto(commentRepository.save(CommentMapper.inComment(user, event, newCommentDto)));
    }


    @Override
    public CommentDto updateComment(NewCommentDto updateCommentDto, Long commentId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        comment.setText(updateCommentDto.getText());
        return CommentMapper.inCommentDto(commentRepository.save(comment));
    }


    @Override
    public void deleteComment(Long commentId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!commentRepository.existsById(commentId))
            throw new NotFoundException("Комментарий не найден");
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> findComment(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!eventRepository.existsById(eventId))
            throw new NotFoundException("Событие не найдено");
        return commentRepository.findAllByEventId(eventId)
                .stream()
                .map(CommentMapper::inCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto findCommentById(Long userId, Long commentId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return CommentMapper.inCommentDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден")));
    }
}