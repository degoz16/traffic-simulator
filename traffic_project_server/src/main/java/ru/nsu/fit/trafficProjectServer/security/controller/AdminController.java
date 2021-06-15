package ru.nsu.fit.trafficProjectServer.security.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.trafficProjectServer.security.model.User;
import ru.nsu.fit.trafficProjectServer.security.service.UserService;

@RestController
@RequestMapping("admin")
public class AdminController {
  @Autowired
  private UserService userService;

  @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
  public List<User> userList() {
    return userService.allUsers();

  }

  @PostMapping
  public ResponseEntity<Resource> changeUser(@RequestParam Long userId,
                                             @RequestParam String action
  ) {
    if (action.equals("delete")) {
      userService.deleteUser(userId);
    }

    return ResponseEntity.ok().build();
  }
}