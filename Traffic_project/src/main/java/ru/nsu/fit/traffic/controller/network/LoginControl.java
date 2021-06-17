package ru.nsu.fit.traffic.controller.network;

import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.interfaces.control.AuthController;

public class LoginControl implements AuthController {

  private ConnectionConfig connectionConfig = ConnectionConfig.getConnectionConfig();

  @Override
  public boolean login(String username, String pass) {
    connectionConfig.setUsername(username);
    connectionConfig.setPassword(pass);
    return connectionConfig.getConnection().login();
  }

  @Override
  public boolean signUp(String username, String pass, String passConfirm) {
    return connectionConfig.getConnection().registration(username, pass, passConfirm);
  }
}
