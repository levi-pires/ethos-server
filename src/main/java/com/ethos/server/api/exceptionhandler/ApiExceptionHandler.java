package com.ethos.server.api.exceptionhandler;

import com.ethos.server.domain.exception.ApiException;
import com.ethos.server.domain.exception.WrongPasswordException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
  @Autowired
  private MessageSource messageSource;

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Object> handleApiException(
    ApiException ex,
    WebRequest request
  ) {
    var status = HttpStatus.BAD_REQUEST;

    var exception = new ExceptionToHandle();
    exception.setTitle(ex.getMessage());
    exception.setStatus(status.value());

    var header = new HttpHeaders();
    header.setAccessControlAllowOrigin("*");

    return handleExceptionInternal(ex, exception, header, status, request);
  }

  @ExceptionHandler(WrongPasswordException.class)
  public ResponseEntity<Object> handleWrongPasswordException(
    ApiException ex,
    WebRequest request
  ) {
    var status = HttpStatus.UNAUTHORIZED;

    var exception = new ExceptionToHandle();
    exception.setTitle(ex.getMessage());
    exception.setStatus(status.value());

    var header = new HttpHeaders();
    header.setAccessControlAllowOrigin("*");

    return handleExceptionInternal(ex, exception, header, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex,
    HttpHeaders headers,
    HttpStatus status,
    WebRequest request
  ) {
    var fields = new ArrayList<ExceptionToHandle.Field>();

    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      String name = ((FieldError) error).getField();
      String message = messageSource.getMessage(
        error,
        LocaleContextHolder.getLocale()
      );

      fields.add(new ExceptionToHandle.Field(name, message));
    }

    var exception = new ExceptionToHandle();
    exception.setStatus(status.value());
    exception.setTitle(
      "Um ou mais campos estão inválidos. " +
      "Faça o preenchimento correto e tente novamente"
    );
    exception.setFields(fields);

    headers.setAccessControlAllowOrigin("*");

    return super.handleExceptionInternal(
      ex,
      exception,
      headers,
      status,
      request
    );
  }
}
