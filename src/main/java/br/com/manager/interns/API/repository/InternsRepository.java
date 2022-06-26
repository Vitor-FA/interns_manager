package br.com.manager.interns.API.repository;

import br.com.manager.interns.API.domains.InternsDomain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternsRepository extends JpaRepository<InternsDomain, UUID> {

}
