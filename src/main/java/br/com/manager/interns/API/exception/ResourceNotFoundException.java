package br.com.manager.interns.API.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

  public ResourceNotFoundException(String resource) {
    super(resource + " with this ID was not found");
  }
}
