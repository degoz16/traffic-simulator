package ru.nsu.fit.trafficProjectServer.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.trafficProjectServer.security.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
}
