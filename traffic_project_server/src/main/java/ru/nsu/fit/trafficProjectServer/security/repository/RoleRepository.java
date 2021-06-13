package ru.nsu.fit.trafficProjectServer.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.trafficProjectServer.security.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
