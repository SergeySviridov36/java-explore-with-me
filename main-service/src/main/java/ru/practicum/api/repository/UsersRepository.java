package ru.practicum.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.User;

public interface UsersRepository extends JpaRepository<User, Long> {
}