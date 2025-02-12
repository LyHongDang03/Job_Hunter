package com.example.Job_Hunter.utill.exception;

import com.example.Job_Hunter.domain.response.RestResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInvalidException.class
    })
    public ResponseEntity<RestResponse<Object>> idInvalidException(Exception exception) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(exception.getMessage());
        restResponse.setMessage("Exception occured...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(exception.getBody().getDetail());
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        restResponse.setMessage(errors.size() > 1 ? errors : errors.get(0));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
    @ExceptionHandler(value = {
           StorageException.class,
    })
    public ResponseEntity<RestResponse<Object>> uploadFileEx(Exception exception) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(exception.getMessage());
        restResponse.setMessage("exception uploadFileEx...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(value = {
            PermissionEx.class,
    })
    public ResponseEntity<RestResponse<Object>> uploadFileEx(PermissionEx exception) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
        restResponse.setError(exception.getMessage());
        restResponse.setMessage("PermissionEx occured...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
}
