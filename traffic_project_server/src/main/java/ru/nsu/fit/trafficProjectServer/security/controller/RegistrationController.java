package ru.nsu.fit.trafficProjectServer.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.trafficProjectServer.security.model.User;
import ru.nsu.fit.trafficProjectServer.security.service.UserService;

@RestController
public class RegistrationController {
  @Autowired
  private UserService userService;

  @PostMapping("/registration")
  public ResponseEntity<User> addUser(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam String passConfirm) {

    if (!password.equals(passConfirm)){
     return ResponseEntity.status(400).build();
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(password);

    if (!userService.saveUser(user)) {
      return ResponseEntity.status(409).build();
    }

    return ResponseEntity.ok().build();
  }
}
