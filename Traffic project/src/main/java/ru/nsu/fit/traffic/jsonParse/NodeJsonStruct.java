package ru.nsu.fit.traffic.jsonParse;

import java.util.List;
import ru.nsu.fit.traffic.model.trafficlight.TrafficLight;

public class NodeJsonStruct {
  private double x;
  private double y;
  private List<Integer> roadsIn;
  private List<Integer> roadsOut;
  private TrafficLight trafficLight;
  private SpawnerJsonStruct spawner;

  public NodeJsonStruct(
      double x,
      double y,
      List<Integer> roadsFrom,
      List<Integer> roadsTo,
      SpawnerJsonStruct spawner,
      TrafficLight trafficLight) { //FIXME Поменять на TrafficJson
    this.x = x;
    this.y = y;
    this.roadsIn = roadsFrom;
    this.roadsOut = roadsTo;
    this.spawner = spawner;
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

  public TrafficLight getTrafficLight() {
    return trafficLight;
  }

  public SpawnerJsonStruct getSpawner() {
      return spawner;
  }
}
