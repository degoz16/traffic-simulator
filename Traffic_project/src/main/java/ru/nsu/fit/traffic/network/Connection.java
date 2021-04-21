package ru.nsu.fit.traffic.network;

public interface Connection {
  void pushMap();
  void pushGlobalMap();
  void getGlobalMapFromServer();

}
