package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationHandler(final ConstraintViolationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Validation error")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();

        log.warn(errorResponse.toString());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundExceptionHandler(final ResourceNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("Resource not found")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();

        log.warn(errorResponse.toString());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse illegalArgumentExceptionHandler(final IllegalArgumentException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("Illegal Argument")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();

        log.warn(errorResponse.toString());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse eventDateExceptionHandler(final DateTimeException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Not Valid Event Date")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();

        log.warn(errorResponse.toString());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse dataIntegrityHandler(final DataIntegrityViolationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Database error")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();

        log.warn(errorResponse.toString());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse resourceAccessExceptionHandler(final ResourceAccessException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .reason("No Access")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();

        log.warn(errorResponse.toString());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse servletExceptionHAndler(final MissingRequestValueException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("MissingRequestValueException")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();
        log.warn(e.getMessage());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidHandler(final MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("MethodArgumentNotValidException")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();
        log.warn(e.getMessage());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("Oops, an unexpected error has occurred")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();

        log.warn(errorResponse.toString());
        return errorResponse;
    }
}
