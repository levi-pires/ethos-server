package com.ethos.server.domain.exception;

public class WrongPasswordException extends ApiException {
  private static final long serialVersionUID = 1L;

  public WrongPasswordException(String exception) {
    super(exception);
  }
}
