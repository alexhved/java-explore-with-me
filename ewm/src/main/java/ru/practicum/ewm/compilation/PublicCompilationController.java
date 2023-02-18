package ru.practicum.ewm.compilation;

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
@Validated
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> findById(@Positive @PathVariable Long compId) {
        CompilationDto compilationDto = compilationService.findById(compId);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CompilationDto>> findWithParams
            (
                    @RequestParam(required = false) Boolean pinned,
                    @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                    @Positive @RequestParam(required = false, defaultValue = "10") Integer size
            ) {
        List<CompilationDto> compilationDtoList = compilationService.findWithParams(pinned, from, size);
        return new ResponseEntity<>(compilationDtoList, HttpStatus.OK);
    }
}
