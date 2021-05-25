package ru.nsu.fit.traffic.interfaces.network;

import java.util.List;

public interface Connection {
  Integer createRoom(String filePath);
  List<Double> getRooms();

  void pushMap(int num, String filepath, int roomId);
  String getMapFromServer(int num, int roomId);
  String getGlobalMapFromServer(int roomId);
}
