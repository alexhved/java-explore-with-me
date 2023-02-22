package ru.practicum.ewm.category;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Validated
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                                     @Positive @RequestParam(required = false, defaultValue = "10") int size) {

        List<CategoryDto> categoryDtoList = categoryService.findAll(from, size);
        return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> findById(@Positive @PathVariable Long catId) {
        CategoryDto categoryDto = categoryService.findById(catId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

}
