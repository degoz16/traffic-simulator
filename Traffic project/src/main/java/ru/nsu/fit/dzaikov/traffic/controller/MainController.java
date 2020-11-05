package ru.nsu.fit.dzaikov.traffic.controller;

import javafx.fxml.FXML;

/** Контроллер основной сцены, на которой располагаются все остальные. */
public class MainController {

  /** инициализация. */
  @FXML
  public void initialize() {

  }

  @FXML
  public void startSimulation(){
    System.out.println("пуск");
  }

  @FXML
  public void pauseSimulation(){
    System.out.println("пауза");
  }

  @FXML
  public void stopSimulation(){
    System.out.println("стоп");
  }

  @FXML
  public void roadButtonClicked(){
    System.out.println("road");
  }

  @FXML
  public void trafficLightButtonClicked(){
    System.out.println("светофор");
  }

  @FXML
  public void buildingButtonClicked(){
    System.out.println("точки интереса");
  }

  @FXML
  public void carButtonClicked(){
    System.out.println("точки спавна машин");
  }

  @FXML
  public void trafficSignButtonClicked(){
    System.out.println("дорожный знак");
  }

  @FXML
  public void showStatistic(){
    System.out.println("статистика");
  }

  @FXML
  public void speedUpSimulation(){
    System.out.println("быстрее");
  }

  @FXML
  public void speedDownSimulation(){
    System.out.println("медленнее");
  }
}
