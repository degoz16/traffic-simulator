package ru.nsu.fit.traffic.interfaces.network;

import java.util.List;

public interface Connection {
  Integer createRoom(String filePath, String roomName);
  List<Double> getRooms();
  void pushMap(int num, int roomId,  String filepath);
  String getMapFromServer(int num, int roomId, boolean block) throws Exception;
  String getGlobalMapFromServer(int roomId);
  boolean dropBlock(int roomId, int mapId);
  List<Long> blockedMaps(int roomid);
}
