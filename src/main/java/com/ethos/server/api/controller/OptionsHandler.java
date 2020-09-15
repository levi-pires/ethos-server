package com.ethos.server.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/*")
public class OptionsHandler {

  @RequestMapping(method = RequestMethod.OPTIONS)
  public ResponseEntity<Void> returnOptions() {
    var headers = new HttpHeaders();
    headers.add("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT");
    headers.add("Access-Control-Allow-Headers", "content-type");

    return ResponseEntity.noContent().headers(headers).build();
  }
}
