package com.ethos.server.core;

import com.ethos.server.cryptx.DigestPassword;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DigestPasswordConfig {

  @Bean
  public DigestPassword digestpassword() {
    return new DigestPassword();
  }
}
