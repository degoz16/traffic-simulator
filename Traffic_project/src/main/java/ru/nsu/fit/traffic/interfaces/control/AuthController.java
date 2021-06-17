package ru.nsu.fit.traffic.interfaces.control;

public interface AuthController {
  boolean login(String username, String pass);
  boolean signUp(String username, String pass, String passConfirm);
}
