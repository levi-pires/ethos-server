package com.ethos.server.domain.repo;

import com.ethos.server.domain.model.EClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EClientRepository extends JpaRepository<EClient, Long> {
  EClient findByUser(String user);
}
