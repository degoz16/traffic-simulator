package ru.nsu.fit.traffic.interfaces.network;

import java.util.List;

public interface Connection {
  Integer createRoom(String filePath);
  List<Double> getRooms();

  void pushMap(int num, int roomId,  String filepath);
  String getMapFromServer(int num, int roomId) throws Exception;
  String getGlobalMapFromServer(int roomId) throws Exception;
}
