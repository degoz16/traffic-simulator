package ru.nsu.fit.traffic.model.playback;

public class CarState {

  private int speed;
  private int id;
  private double position;
  private boolean draw;

  public void setTime(int time) {
    this.time = time;
  }

  private int time;

  private int currentLane;
  private int currentRoad;
  private int xCoord;
  private int yCoord;

  public boolean isDraw() {
    return draw;
  }

  public int getId() {
    return id;
  }

  public CarState(int speed, int carId, double position, int time, int currLane, int currRoad, int xCoord, int yCoord, boolean draw) {
    this.speed = speed;
    this.id = carId;
    this.position = position;
    this.time = time;
    this.currentLane = currLane;
    this.currentRoad = currRoad;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.draw = draw;
  }

  public int getSpeed() {
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

  public int getxCoord() {
    return xCoord;
  }

  public int getyCoord() {
    return yCoord;
  }
}
