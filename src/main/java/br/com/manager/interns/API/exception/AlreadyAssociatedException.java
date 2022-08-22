package br.com.manager.interns.API.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AlreadyAssociatedException extends
    RuntimeException {

  public AlreadyAssociatedException(String relate, String related) {
    super("Already exists association between " + relate + " and " + related);
  }
}
