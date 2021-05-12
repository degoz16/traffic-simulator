package ru.nsu.fit.traffic.interfaces.network;

public interface Connection {
  void pushMap(int num, String filepath);
  String getMapFromServer(int num);
  void pushGlobalMap(String filepath);
  String getGlobalMapFromServer();
}
