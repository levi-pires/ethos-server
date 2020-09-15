package com.ethos.server.domain.service;

import com.ethos.server.cryptx.DigestPassword;
import com.ethos.server.domain.exception.ApiException;
import com.ethos.server.domain.model.EClient;
import com.ethos.server.domain.repo.EClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BClientService {
  @Autowired
  private DigestPassword digestPassword;

  @Autowired
  private EClientRepository eclientrepository;

  @Autowired
  private LoginAttemptsService loginAttemptsService;

  public ResponseEntity<EClient> save(EClient client) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(eclientrepository.save(setupEClient(client)));
  }

  public ResponseEntity<EClient> update(EClient client, Long id) {
    if (!eclientrepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }

    client.setId(id);

    return ResponseEntity.ok(eclientrepository.save(setupEClient(client)));
  }

  public ResponseEntity<Void> delete(Long id) {
    if (eclientrepository.existsById(id)) {
      eclientrepository.deleteById(id);
    } else return ResponseEntity.notFound().build();

    return ResponseEntity.noContent().build();
  }

  private EClient setupEClient(EClient client) {
    EClient existentClient = eclientrepository.findByUser(client.getUser());

    if (existentClient != null && !existentClient.equals(client)) {
      throw new ApiException("Já existe uma conta com este user");
    }

    if (client.getPassword() == null) {
      if (existentClient.getPassword() != null) client.setPassword(
        existentClient.getPassword()
      ); else throw new ApiException("Erro no setup do cliente");
    } else client.setPassword(digestPassword.digest(client.getPassword()));

    if (existentClient == null) {
      client.setAttempt(loginAttemptsService.create());
    } else client.setAttempt(existentClient.getAttempt());

    return client;
  }
}
