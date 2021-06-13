package ru.nsu.fit.traffic.config;

import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.model.map.TrafficMap;

public class ConnectionConfig {

  private ConnectionConfig() {
  }

  private static ConnectionConfig connectionConfig;
  private Connection connection;
  private Integer roomId;
  private Integer mapId;
  private TrafficMap trafficMap;

  public Integer getRoomId() {
    return roomId;
  }

  public void setRoomId(Integer roomId) {
    this.roomId = roomId;
  }

  public Integer getMapId() {
    return mapId;
  }

  public void setMapId(Integer mapId) {
    this.mapId = mapId;
  }

  public TrafficMap getTrafficMap() {
    return trafficMap;
  }

  public void setPartPath(TrafficMap trafficMap) {
    this.trafficMap = trafficMap;
  }

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
