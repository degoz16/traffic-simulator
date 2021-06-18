package ru.nsu.fit.traffic.network.stub;

import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.map.TrafficMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConnectionStub implements Connection {

  private final double scale;
  private final String serverFolder = "/server_stub/";
  private int currRoomsCnt;
  private List<Integer> roomsRegCnt = new ArrayList<>();
  public ConnectionStub(double scale) {
    File theDir = new File(serverFolder);
    if (!theDir.exists()){
      theDir.mkdirs();
    }
    currRoomsCnt = 0;
    this.scale = scale;
  }

  @Override
  public Integer createRoom(String filePath, String roomName) {
    File theDir = new File(serverFolder + "room_" + currRoomsCnt);
    if (!theDir.exists()){
      theDir.mkdirs();
    }
    RegionsMap map = GlobalMapEditOpManager.loadRegMap(filePath);
    if (map == null) {
      throw new RuntimeException("Reg map load exception");
    }
    roomsRegCnt.add(map.getRegionCount());
    GlobalMapEditOpManager.saveRegMap(theDir.getAbsolutePath() + "/global_map.tsp", map);
    for (int i = 0; i < map.getRegionCount(); i++) {
      TrafficMap m = new TrafficMap(i, map.getRegion(i), scale);
      EditOperationsManager.saveMap(theDir.getAbsolutePath() + "/map_" + i + ".tsp", m);
    }
    return currRoomsCnt++;
  }

  @Override
  public void pushGlobalMap(String filePath, Integer roomId) {

  }

  @Override
  public List<Long> getRooms() {
    var list = new ArrayList<Long>();
    for (int i = 0; i < currRoomsCnt; i++) {
      list.add((long) i);
    }
    return list;
  }

  @Override
  public boolean login() {
    return false;
  }

  @Override
  public boolean isAdmin(int roomId) {
    return false;
  }

  @Override
  public List<Long> getLastBlockedMaps(int roomId) {
    return null;
  }

  @Override
  public boolean registration(String username, String password, String passConfirm) {
    return false;
  }

  @Override
  public boolean pushMap(int num, int roomId, String filepath) {
    TrafficMap map = EditOperationsManager.loadMap(filepath);
    EditOperationsManager.saveMap(serverFolder + "room_" + roomId + "/map_" + num + ".tsp", map);
    return false;
  }

  @Override
  public String getMapFromServer(int num, int roomId, boolean block) {
    return serverFolder + "room_" + roomId + "/map_" + num + ".tsp";
  }

  @Override
  public String getGlobalMapFromServer(int roomId) {
    return serverFolder + "room_" + roomId + "/global_map.tsp";
  }

  @Override
  public boolean dropBlock(int roomId, int mapId) {
    return false;
  }

  @Override
  public List<Long> blockedMaps(int roomid) {
    return null;
  }
}
