package br.com.manager.interns.API.handler;

import br.com.manager.interns.API.exception.AlreadyAssociatedException;
import br.com.manager.interns.API.exception.AssociationNotFoundException;
import br.com.manager.interns.API.exception.EmailAlreadyExistException;
import br.com.manager.interns.API.exception.ExceptionsDetails;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
  @ExceptionHandler({AlreadyAssociatedException.class, AssociationNotFoundException.class,
      EmailAlreadyExistException.class, ResourceNotFoundException.class})
  public ResponseEntity<ExceptionsDetails> handleExceptions(RuntimeException exception) {
    ExceptionsDetails details = ExceptionsDetails.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .message(exception.getMessage())
        .build();

    return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
  }
}
