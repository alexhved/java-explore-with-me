package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.error.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        CategoryEntity categoryEntity = categoryRepository.save(categoryMapper.toEntity(newCategoryDto));
        return categoryMapper.toDto(categoryEntity);
    }

    public void deleteCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new ResourceNotFoundException("Category not found");
        }
        categoryRepository.deleteById(catId);
    }

    public CategoryDto updateCategoryById(Long catId, NewCategoryDto newCategoryDto) {
        CategoryEntity categoryEntity = categoryRepository.save(categoryMapper.toEntity(catId, newCategoryDto));
        return categoryMapper.toDto(categoryEntity);
    }

    public List<CategoryDto> findAll(int from, int size) {
        Page<CategoryEntity> page = categoryRepository.findAll(PageRequest.of(from, size));
        return page.getContent().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto findById(Long catId) {
        CategoryEntity categoryEntity = categoryRepository.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Wrong id"));
        return categoryMapper.toDto(categoryEntity);
    }
}
