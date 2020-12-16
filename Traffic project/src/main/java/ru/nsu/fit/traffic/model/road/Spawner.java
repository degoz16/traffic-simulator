package ru.nsu.fit.traffic.model.road;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Spawner {

  private Date start;
  private Date end;
  private int spawnRate;

  public Spawner(String start, String end, int spawnRate) {
    try {
      DateFormat format = new SimpleDateFormat("HH:mm");
      this.start = format.parse(start);
      this.end = format.parse(end);
      this.spawnRate = spawnRate;
    } catch (ParseException e) {
      System.err.println("Wrong data type in spawner creation.");
      assert false;
    }
  }

  public Spawner(Date start, Date end, int spawnRate) {
    this.start = start;
    this.end = end;
    this.spawnRate = spawnRate;
  }

  public String getStartString() {
    DateFormat format = new SimpleDateFormat("HH:mm");
    return format.format(start);
  }

  public String getEndString() {
    DateFormat format = new SimpleDateFormat("HH:mm");
    return format.format(end);
  }

  public Date getStart() {
    return start;
  }

  public Date getEnd() {
    return end;
  }

  public int getSpawnRate() {
    return spawnRate;
  }
}
