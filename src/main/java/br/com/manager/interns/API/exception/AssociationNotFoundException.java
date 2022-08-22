package br.com.manager.interns.API.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssociationNotFoundException extends RuntimeException {

  public AssociationNotFoundException(String relate, String related) {
    super("Association between " + relate + " and " + related + " was not found");
  }
}
