package org.smoliagin.template.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handlerException (EntityNotFoundException exception) {
        ErrorInfo data = new ErrorInfo();
        data.setCode(HttpStatus.NOT_FOUND.value());
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handlerException(BadRequestException exception){
        ErrorInfo data = new ErrorInfo();
        data.setCode(HttpStatus.BAD_REQUEST.value());
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handlerException(BusinessLogicException exception) {
        ErrorInfo data = new ErrorInfo();
        data.setCode(444);
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
