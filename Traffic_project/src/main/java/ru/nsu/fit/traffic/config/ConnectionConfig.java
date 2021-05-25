package ru.nsu.fit.traffic.config;

import ru.nsu.fit.traffic.interfaces.network.Connection;

public class ConnectionConfig {


  private ConnectionConfig() {
  }

  private static ConnectionConfig connectionConfig;
  private Connection connection;

  public static ConnectionConfig getConnectionConfig() {
    if (connectionConfig == null) {
      connectionConfig = new ConnectionConfig();
    }
    return connectionConfig;
  }

  public Connection getConnection() {
    return connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }
}
