package com.ethos.server.api.controller;

import com.ethos.server.api.model.EClientOutput;
import com.ethos.server.cryptx.DigestPassword;
import com.ethos.server.domain.exception.WrongPasswordException;
import com.ethos.server.domain.model.EClient;
import com.ethos.server.domain.repo.EClientRepository;
import com.ethos.server.domain.service.LoginAttemptsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class EClientLoginController {
  @Autowired
  private ModelMapper modelmapper;

  @Autowired
  private DigestPassword digestPassword;

  @Autowired
  private LoginAttemptsService loginAttemptService;

  @Autowired
  private EClientRepository eclientrepository;

  @GetMapping("/{user}/{userPassword}")
  public ResponseEntity<EClientOutput> eLogin(
    @PathVariable String user,
    @PathVariable String userPassword
  ) {
    if (user.equals("admin")) return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .build();

    EClient client = eclientrepository.findByUser(user);

    if (client == null) return ResponseEntity.notFound().build();

    Long attemptId = client.getAttempt().getId();

    if (
      loginAttemptService.get(attemptId) == 4
    ) throw new WrongPasswordException(
      "O limite de tentativas do login foi atingido."
    );

    String key = digestPassword.digest(userPassword);

    if (!key.equals(client.getPassword())) {
      loginAttemptService.add(attemptId);
      throw new WrongPasswordException(
        "Senha de usuário incorreta. Você tem " +
        (5 - loginAttemptService.get(attemptId)) +
        " tentativas restantes."
      );
    }

    if (loginAttemptService.get(attemptId) > 0) loginAttemptService.reset(
      attemptId
    );

    return toOutputResponse(ResponseEntity.ok(client));
  }

  private ResponseEntity<EClientOutput> toOutputResponse(
    ResponseEntity<EClient> res
  ) {
    return ResponseEntity
      .status(res.getStatusCode())
      .body(modelmapper.map(res.getBody(), EClientOutput.class));
  }
}
