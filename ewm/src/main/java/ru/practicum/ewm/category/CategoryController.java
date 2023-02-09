package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        CategoryDto categoryDto = categoryService.addCategory(newCategoryDto);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@Positive @PathVariable Long catId,
                                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {

        CategoryDto categoryDto = categoryService.updateCategoryById(catId, newCategoryDto);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@Positive @PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
