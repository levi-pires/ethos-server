package com.ethos.server.domain.exception;

public class EntityNotFoundException extends ApiException {
  private static final long serialVersionUID = 1L;

  public EntityNotFoundException(String exception) {
    super(exception);
  }
}
