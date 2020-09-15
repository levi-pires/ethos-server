package com.ethos.server.domain.repo;

import com.ethos.server.domain.model.LoginAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptsRepository
  extends JpaRepository<LoginAttempts, Long> {}
