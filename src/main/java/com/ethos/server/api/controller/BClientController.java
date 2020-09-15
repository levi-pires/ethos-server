package com.ethos.server.api.controller;

import com.ethos.server.App;
import com.ethos.server.api.model.EClientOutput;
import com.ethos.server.domain.exception.WrongPasswordException;
import com.ethos.server.domain.model.EClient;
import com.ethos.server.domain.repo.EClientRepository;
import com.ethos.server.domain.service.BClientService;
import com.ethos.server.domain.service.LoginAttemptsService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class BClientController {
  private final String adminPassword = App.getPasswd();

  @Autowired
  private ModelMapper modelmapper;

  @Autowired
  private EClientRepository eclientrepository;

  @Autowired
  private BClientService bclientservice;

  @Autowired
  private LoginAttemptsService loginAttemptsService;

  @GetMapping("/{password}")
  public ResponseEntity<List<EClientOutput>> listAll(
    @PathVariable String password
  ) {
    checkAdminPasswd(password);

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(toOutputList(eclientrepository.findAll()));
  }

  @PostMapping("/{password}")
  public ResponseEntity<EClientOutput> add(
    @Valid @RequestBody EClient client,
    @PathVariable String password
  ) {
    checkAdminPasswd(password);

    return toOutputResponse(bclientservice.save(client));
  }

  @PutMapping("/{id}/{password}")
  public ResponseEntity<EClientOutput> put(
    @Valid @PathVariable Long id,
    @PathVariable String password,
    @RequestBody EClient client
  ) {
    checkAdminPasswd(password);

    return toOutputResponse(bclientservice.update(client, id));
  }

  @DeleteMapping("/{id}/{password}")
  public ResponseEntity<Void> delete(
    @Valid @PathVariable Long id,
    @PathVariable String password
  ) {
    checkAdminPasswd(password);

    return bclientservice.delete(id);
  }

  private EClientOutput toOutput(EClient client) {
    return modelmapper.map(client, EClientOutput.class);
  }

  private ResponseEntity<EClientOutput> toOutputResponse(
    ResponseEntity<EClient> res
  ) {
    return ResponseEntity
      .status(res.getStatusCode())
      .body(toOutput(res.getBody()));
  }

  private List<EClientOutput> toOutputList(List<EClient> list) {
    return list
      .stream()
      .map(client -> toOutput(client))
      .collect(Collectors.toList());
  }

  private void checkAdminPasswd(String password) {
    if (loginAttemptsService.exists(1L)) {
      if (loginAttemptsService.get(1L) == 5) throw new WrongPasswordException(
        "O limite de tentativas de login foi atingido"
      );
    }

    if (!adminPassword.equals(password)) {
      loginAttemptsService.add(1L);
      throw new WrongPasswordException(
        "A senha de administrador inserida está incorreta. " +
        (5 - loginAttemptsService.get(1L)) +
        " tentativas restantes."
      );
    } else if (loginAttemptsService.exists(1L)) {
      loginAttemptsService.reset(1L);
    }
  }
}
