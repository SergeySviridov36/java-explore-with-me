package ru.practicum.api.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.model.dto.category.CategoryDto;
import ru.practicum.model.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    void deleteCategory(Long catId);

    List<CategoryDto> findCategory(PageRequest pageRequest);

    CategoryDto findCategoryById(Long catId);
}