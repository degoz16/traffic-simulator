package ru.nsu.fit.traffic.jsonParse;

import java.util.List;

public class NodeJsonStruct {
  private double x;
  private double y;
  private List<Integer> roadsIn;
  private List<Integer> roadsOut;
  private List<TrafficLightConfigJsonStruct> trafficLight;
  private List<SpawnerJsonStruct> periodsOfSpawn;

  public NodeJsonStruct(
      double x,
      double y,
      List<Integer> roadsFrom,
      List<Integer> roadsTo,
      List<SpawnerJsonStruct> periodsOfSpawn,
      List<TrafficLightConfigJsonStruct> trafficLight) { //FIXME Поменять на TrafficJson
    this.x = x;
    this.y = y;
    this.roadsIn = roadsFrom;
    this.roadsOut = roadsTo;
    this.periodsOfSpawn = periodsOfSpawn;
    this.trafficLight = trafficLight;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public List<Integer> getRoadsIn() {
    return roadsIn;
  }

  public List<Integer> getRoadsOut() {
    return roadsOut;
  }

  public List<TrafficLightConfigJsonStruct> getTrafficLight() {
    return trafficLight;
  }

  public List<SpawnerJsonStruct>  getPeriodsOfSpawn() {
      return periodsOfSpawn;
  }
}
