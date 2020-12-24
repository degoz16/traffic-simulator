package ru.nsu.fit.traffic.json.parse;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.fit.traffic.model.road.Road;

public class TrafficLightConfigJsonStruct {

  private int delay;
  private List<Integer> roads;

  public TrafficLightConfigJsonStruct(int delay, List<Integer> roads){
    this.delay = delay;
    assert roads != null;
    this.roads = new ArrayList<>(roads);
  }

  public int getDelay() {
    return delay;
  }

  public List<Integer> getRoads() {
    return roads;
  }

}
