package ru.practicum.mapper;

import ru.practicum.model.dto.category.CategoryDto;
import ru.practicum.model.dto.category.NewCategoryDto;
import ru.practicum.model.Category;

public class CategoryMapper {

    public static CategoryDto inCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category inCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                null,
                newCategoryDto.getName()
        );
    }
}