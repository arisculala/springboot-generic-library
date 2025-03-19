package com.generic.handlers;

import com.generic.dto.SuccessErrorResponseDTO;
import com.generic.exceptions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleBadRequest(BadRequestException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleUnauthorized(UnauthorizedException ex) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleForbidden(ForbiddenException ex) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleNotFound(NotFoundException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleInternalServer(InternalServerException ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleConflict(ConflictException ex) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(PayloadTooLargeException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handlePayloadTooLarge(PayloadTooLargeException ex) {
        return buildResponseEntity(HttpStatus.PAYLOAD_TOO_LARGE, ex.getMessage());
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleTooManyRequests(TooManyRequestsException ex) {
        return buildResponseEntity(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
    }

    @ExceptionHandler(PreconditionFailedException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handlePreconditionFailed(PreconditionFailedException ex) {
        return buildResponseEntity(HttpStatus.PRECONDITION_FAILED, ex.getMessage());
    }

    @ExceptionHandler(UnsupportedMediaTypeException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleUnsupportedMediaType(UnsupportedMediaTypeException ex) {
        return buildResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleUnprocessableEntity(UnprocessableEntityException ex) {
        return buildResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleServiceUnavailable(ServiceUnavailableException ex) {
        return buildResponseEntity(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SuccessErrorResponseDTO> handleGenericException(Exception ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<SuccessErrorResponseDTO> buildResponseEntity(HttpStatus status, String message) {
        return new ResponseEntity<>(new SuccessErrorResponseDTO(status, message), status);
    }
}
