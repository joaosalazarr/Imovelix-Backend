package com.api.imovelix.exceptions;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        return buildValidationResponse(ex, status, request);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException ex, WebRequest request) {
        return buildValidationResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
        ConstraintViolationException ex,
        WebRequest request
    ) {
        List<FieldErrorResponse> fieldErrors = ex.getConstraintViolations()
            .stream()
            .map(violation -> new FieldErrorResponse(
                violation.getPropertyPath().toString(),
                violation.getMessage(),
                violation.getInvalidValue()
            ))
            .toList();

        return ResponseEntity.badRequest().body(ApiErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Validation failed",
            path(request),
            fieldErrors
        ));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatusException(
        ResponseStatusException ex,
        WebRequest request
    ) {
        HttpStatusCode statusCode = ex.getStatusCode();
        String error = statusCode instanceof HttpStatus httpStatus
            ? httpStatus.getReasonPhrase()
            : "Request failed";

        return ResponseEntity.status(statusCode).body(ApiErrorResponse.of(
            statusCode.value(),
            error,
            ex.getReason(),
            path(request)
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Unexpected error",
            path(request)
        ));
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
        HandlerMethodValidationException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        List<FieldErrorResponse> fieldErrors = ex.getParameterValidationResults()
            .stream()
            .flatMap(result -> result.getResolvableErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                    result.getMethodParameter().getParameterName(),
                    error.getDefaultMessage(),
                    null
                )))
            .toList();

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            "Validation failed",
            "Validation failed",
            path(request),
            fieldErrors
        );

        return ResponseEntity.status(status).headers(headers).body(body);
    }

    private ResponseEntity<Object> buildValidationResponse(BindException ex, HttpStatusCode status, WebRequest request) {
        List<FieldErrorResponse> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
            .toList();

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            "Validation failed",
            "Validation failed",
            path(request),
            fieldErrors
        );

        return ResponseEntity.status(status).body(body);
    }

    private String path(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }

        return "";
    }
}
