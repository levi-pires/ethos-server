package com.ethos.server.domain.service;

import com.ethos.server.domain.exception.ApiException;
import com.ethos.server.domain.model.LoginAttempts;
import com.ethos.server.domain.repo.LoginAttemptsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptsService {
  @Autowired
  private LoginAttemptsRepository attemptsrepository;

  public LoginAttempts create() {
    return attemptsrepository.save(new LoginAttempts());
  }

  public LoginAttempts add(Long id) {
    LoginAttempts attempt = attemptsrepository
      .findById(id)
      .orElseThrow(
        () -> new ApiException("Erro no processo de criação do 'attempt'.")
      );

    attempt.plusOne();

    return attemptsrepository.save(attempt);
  }

  public LoginAttempts reset(Long id) {
    LoginAttempts attempt = attemptsrepository
      .findById(id)
      .orElseThrow(
        () ->
          new ApiException(
            "Não foi encontrado nenhum LoginAttempt para este cliente." +
            " A operação 'reset' não pôde ser executada."
          )
      );

    attempt.setAmount(0);

    return attemptsrepository.save(attempt);
  }

  public Integer get(Long id) {
    LoginAttempts attempt = attemptsrepository
      .findById(id)
      .orElseThrow(
        () ->
          new ApiException(
            "Não foi encontrado nenhum LoginAttempt para este cliente." +
            " A operação 'get' não pôde ser executada."
          )
      );

    return attempt.getAmount();
  }

  public boolean exists(Long id) {
    return attemptsrepository.findById(id).isPresent();
  }
}
