package ru.nsu.fit.traffic.model.playback;

import ru.nsu.fit.traffic.model.map.Road;

public class CarState {
  public static final double CAR_WIDTH = 6;
  public static final double CAR_HEIGHT = 10;
  private double speed;
  private int id;
  private double position = 0;
  private boolean draw;

  public void setTime(int time) {
    this.time = time;
  }

  private int time;

  private int currentLane = 0;
  private int currentRoad = 0;
  private double xCoord = 0;
  private double yCoord = 0;

  public boolean isDraw() {
    return draw;
  }

  public int getId() {
    return id;
  }

//  public CarState(
//          int speed,
//          int carId,
//          double position,
//          int time,
//          int currLane,
//          int currRoad,
//          int xCoord,
//          int yCoord,
//          boolean draw) {
//    this.speed = speed;
//    this.id = carId;
//    this.position = position;
//    this.time = time;
//    this.currentLane = currLane;
//    this.currentRoad = currRoad;
//    this.xCoord = xCoord;
//    this.yCoord = yCoord;
//    this.draw = draw;
//  }

  public void initCoords(Road road) {
    double x1 = road.getFrom().getX();
    double y1 = road.getFrom().getY();
    double x2 = road.getTo().getX();
    double y2 = road.getTo().getY();
    double len = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    xCoord = x1 + (position / len)*(x2-x1);
    yCoord = y1 + (position / len)*(y2-y1);
    x2 = (x2 - x1) / len;
    y2 = (y2 - y1) / len;
    double swap = x2;
    x2 = y2;
    y2 = -swap;
    xCoord -= x2 * 10 * (currentLane + 0.5);
    yCoord -= y2 * 10 * (currentLane + 0.5);
  }

  public double getSpeed() {
    return speed;
  }

  public int getCarId() {
    return id;
  }

  public double getPosition() {
    return position;
  }

  public int getTime() {
    return time;
  }

  public int getCurrentLane() {
    return currentLane;
  }

  public int getCurrentRoad() {
    return currentRoad;
  }

  public double getXCoord() {
    return xCoord;
  }

  public double getYCoord() {
    return yCoord;
  }
}
