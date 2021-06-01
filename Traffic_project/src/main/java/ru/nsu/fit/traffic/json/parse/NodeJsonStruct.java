package ru.nsu.fit.traffic.json.parse;

import java.util.List;

public class NodeJsonStruct {
  private double x;
  private double y;
  private List<Integer> roadsIn;
  private List<Integer> roadsOut;
  private List<TrafficLightConfigJsonStruct> trafficLight;
  private List<SpawnerJsonStruct> periodsOfSpawn;
  private RegionConnectorJson connector;

  public NodeJsonStruct(
      double x,
      double y,
      List<Integer> roadsFrom,
      List<Integer> roadsTo,
      List<SpawnerJsonStruct> periodsOfSpawn,
      List<TrafficLightConfigJsonStruct> trafficLight,
      RegionConnectorJson connector) {
    this.x = x;
    this.y = y;
    this.roadsIn = roadsFrom;
    this.roadsOut = roadsTo;
    this.periodsOfSpawn = periodsOfSpawn;
    this.trafficLight = trafficLight;
    this.connector = connector;
  }

  public RegionConnectorJson getConnector() {
    return connector;
  }

  public void setConnector(RegionConnectorJson connector) {
    this.connector = connector;
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
