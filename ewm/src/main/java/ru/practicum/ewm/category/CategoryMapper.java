package ru.practicum.ewm.category;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper {
    CategoryEntity toEntity(NewCategoryDto newCategoryDto);

    @Mapping(target = "id", source = "catId")
    CategoryEntity toEntity(long catId, NewCategoryDto newCategoryDto);

    CategoryDto toDto(CategoryEntity categoryEntity);

}
