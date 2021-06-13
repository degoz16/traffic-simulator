package ru.nsu.fit.trafficProjectServer.security.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.trafficProjectServer.security.model.User;
import ru.nsu.fit.trafficProjectServer.security.service.UserService;

@RestController
public class AdminController {
  @Autowired
  private UserService userService;

  @GetMapping(path="/admin", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<User> userList() {
    return userService.allUsers();

  }

  @PostMapping("/admin")
  public ResponseEntity<Resource> deleteUser(@RequestParam Long userId,
                                             @RequestParam String action
  ) {
    if (action.equals("delete")) {
      userService.deleteUser(userId);
    }

    return ResponseEntity.ok().build();
  }
}