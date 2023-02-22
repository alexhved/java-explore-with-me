package ru.practicum.ewm.compilation;

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
@Slf4j
@Validated
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {

        CompilationDto compilationDto = compilationService.addCompilation(newCompilationDto);
        log.info("add compilation: {}", newCompilationDto);
        return new ResponseEntity<>(compilationDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> delCompilation(@Positive @PathVariable Long compId) {
        compilationService.delCompilation(compId);
        log.info("delete compilation with id: {}", compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> patchCompilation(@Positive @PathVariable Long compId,
                                                           @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        CompilationDto compilationDto = compilationService.patchCompilation(compId, updateCompilationRequest);
        log.info("patch compilation: {}", updateCompilationRequest);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }
}
