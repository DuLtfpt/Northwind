package com.northwind.orderservice.exception;

import jakarta.servlet.ServletException;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler({
            BadRequestException.class,
            Exception.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now());
    }

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlerForbiddenException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorResponse(
                HttpStatus.FORBIDDEN,
                "Bad credentials",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundException(NotFoundException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now());
    }

    @ExceptionHandler({
            IOException.class,
            ServletException.class,
            BadCredentialsException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handlerUnauthorizedException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity handleWebClientException(WebClientResponseException ex){
        return ResponseEntity.badRequest().body(ex.getResponseBodyAs(ErrorResponse.class));
    }
}