package br.com.pecem.localsurf.domain.repository;

import br.com.pecem.localsurf.domain.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles,Long> {
}
