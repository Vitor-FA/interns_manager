package br.com.manager.interns.API.repository;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.domains.LeadDomain;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<LeadDomain, UUID> {

  Optional<LeadDomain> findByEmail(String email);
}
