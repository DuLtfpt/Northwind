package com.northwind.gateway.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private String Path;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy HH:mm:ss"
    )
    private LocalDateTime timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(HttpStatus status, String message, String path, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        Path = path;
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return Path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
