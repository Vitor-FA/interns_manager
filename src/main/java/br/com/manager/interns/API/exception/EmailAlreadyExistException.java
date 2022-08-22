package br.com.manager.interns.API.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailAlreadyExistException extends RuntimeException{

  public EmailAlreadyExistException() {
    super("Email already in use");
  }
}
