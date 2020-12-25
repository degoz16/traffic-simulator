package ru.nsu.fit.traffic.json.parse;

public class SpawnerJsonStruct {
  private String start;
  private String end;
  private int spawnRate;

  public SpawnerJsonStruct(String start, String end, int spawnRate) {
    this.start = start;
    this.end = end;
    this.spawnRate = spawnRate;
  }

  public String getStart() {
    return start;
  }

  public String getEnd() {
    return end;
  }

  public int getSpawnerRate() {
    return spawnRate;
  }
}
