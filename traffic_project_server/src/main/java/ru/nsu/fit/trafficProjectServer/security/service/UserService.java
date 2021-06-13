package ru.nsu.fit.trafficProjectServer.security.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.fit.trafficProjectServer.security.model.Role;
import ru.nsu.fit.trafficProjectServer.security.model.User;
import ru.nsu.fit.trafficProjectServer.security.repository.RoleRepository;
import ru.nsu.fit.trafficProjectServer.security.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
  @PersistenceContext
  private EntityManager em;
  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }

    return user;
  }

  public User findUserById(Long userId) {
    Optional<User> userFromDb = userRepository.findById(userId);
    return userFromDb.orElse(new User());
  }

  public List<User> allUsers() {
    return userRepository.findAll();
  }

  public boolean saveUser(User user) {
    User userFromDB = userRepository.findByUsername(user.getUsername());

    if (userFromDB != null) {
      return false;
    }
    Role role = new Role(1L, "USER");
    roleRepository.save(role);

    user.setRoles(Collections.singleton(role));
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return true;
  }

  public boolean deleteUser(Long userId) {
    if (userRepository.findById(userId).isPresent()) {
      userRepository.deleteById(userId);
      return true;
    }
    return false;
  }
}
