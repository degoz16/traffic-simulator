package ru.nsu.fit.traffic.jsonParse;

public class SpawnerJsonStruct {
  private String start;
  private String end;
  private int spawnerRate;

  public SpawnerJsonStruct(String start, String end, int spawnerRate) {
    this.start = start;
    this.end = end;
    this.spawnerRate = spawnerRate;
  }

  public String getStart() {
    return start;
  }

  public String getEnd() {
    return end;
  }

  public int getSpawnerRate() {
    return spawnerRate;
  }
}
