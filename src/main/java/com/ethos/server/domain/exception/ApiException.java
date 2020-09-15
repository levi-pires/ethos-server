package com.ethos.server.domain.exception;

public class ApiException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public ApiException(String exception) {
    super(exception);
  }
}
