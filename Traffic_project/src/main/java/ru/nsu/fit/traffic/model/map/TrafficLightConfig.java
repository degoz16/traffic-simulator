package ru.nsu.fit.traffic.model.map;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.fit.traffic.model.map.Road;

public class TrafficLightConfig {
  private int delay;
  private List<Road> roads;

  public TrafficLightConfig(int delay, List<Road> roads) {
    this.delay = delay;
    assert roads != null;
    this.roads = new ArrayList<>(roads);
  }

  public int getDelay() {
    return delay;
  }

  public List<Road> getRoads() {
    return roads;
  }
}
