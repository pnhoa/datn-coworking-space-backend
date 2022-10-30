package com.datn.coworkingspace.exception;

import com.datn.coworkingspace.dto.MessageResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.ParseException;
import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request){

        MessageResponse messageResponse = new MessageResponse(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        MessageResponse messageResponse = new MessageResponse(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex){
        MessageResponse messageResponse = new MessageResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleTokenRefreshException(TokenRefreshException ex){
        MessageResponse messageResponse = new MessageResponse(ex.getMessage(), HttpStatus.FORBIDDEN, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex){
        MessageResponse messageResponse = new MessageResponse(ex.getMessage(), HttpStatus.FORBIDDEN, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex){
        MessageResponse messageResponse = new MessageResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Object> handleParseException(ParseException ex){
        MessageResponse messageResponse = new MessageResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex){
        MessageResponse messageResponse = new MessageResponse("Maximum upload size exceeded, the request was rejected because its size exceeds the configured maximum.", HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex){
        MessageResponse messageResponse = new MessageResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }


}
