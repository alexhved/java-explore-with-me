package ru.practicum.ewm.error;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class ErrorResponse {
    private Object[] errors;
    private HttpStatus status;
    private String reason;
    private String message;
    private String timestamp;
}
