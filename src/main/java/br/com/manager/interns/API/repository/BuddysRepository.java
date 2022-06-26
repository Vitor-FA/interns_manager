package br.com.manager.interns.API.repository;

import br.com.manager.interns.API.domains.BuddysDomain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuddysRepository extends JpaRepository<BuddysDomain, UUID> {

}
