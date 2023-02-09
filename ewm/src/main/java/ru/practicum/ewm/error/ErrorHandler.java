package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.utils.Const;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final static DateTimeFormatter DTF = Const.DTF;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationHandler (final ConstraintViolationException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Validation error")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundExceptionHandler(final ResourceNotFoundException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("Resource not found")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse illegalArgumentExceptionHandler(final IllegalArgumentException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("Illegal Argument")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse EventDateExceptionHandler(final DateTimeException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("Not Valid Event Date")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("Oops, an unexpected error has occurred")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();
    }
}
