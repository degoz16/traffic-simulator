package ru.nsu.fit.traffic.controller.edit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.time.LocalTime;
import java.util.List;
import ru.nsu.fit.traffic.controller.BaseControl;
import ru.nsu.fit.traffic.controller.SceneElementsControl;
import ru.nsu.fit.traffic.controller.engine.EngineController;
import ru.nsu.fit.traffic.controller.saveload.SaveLoadControl;
import ru.nsu.fit.traffic.controller.statistic.StatisticControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapperButton;
import ru.nsu.fit.traffic.model.congestion.ReportStruct;
import ru.nsu.fit.traffic.model.congestion.ReportWindowStruct;
import ru.nsu.fit.traffic.model.logic.EditOperation;
import ru.nsu.fit.traffic.model.logic.UpdateObserver;
import ru.nsu.fit.traffic.model.node.Node;
import ru.nsu.fit.traffic.model.place.PlaceOfInterest;
import ru.nsu.fit.traffic.model.road.Lane;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.trafficsign.RoadSign;
import ru.nsu.fit.traffic.model.trafficsign.SignFactory;
import ru.nsu.fit.traffic.model.trafficsign.SignType;

public class EditControl extends BaseControl {
  private final ReportStruct reportStruct = new ReportStruct();
  private final SignFactory signFactory = new SignFactory();
  private double lastBaseX = 0d;
  private double lastBaseY = 0d;
  private double lastClickX = 0;
  private double lastClickY = 0;
  private Road lastRoadClicked = null;
  private Node lastNodeClicked = null;
  private PlaceOfInterest lastPOIClicked = null;
  private final StatisticControl statisticControl;
  private final SaveLoadControl saveLoadControl;
  private final EngineController engineController;


  public EditControl(SceneElementsControl sceneElementsControl,
                     StatisticControl statisticControl,
                     SaveLoadControl saveLoadControl) {
    super(sceneElementsControl);
    this.statisticControl = statisticControl;
    this.saveLoadControl = saveLoadControl;
    engineController = new EngineController(
      "D:\\Jaguar\\GitHub\\traffic-simulator\\Traffic_project\\Engine.jar" //TODO
    );
    sceneElementsControl.timeLineSliderInit(
            reportStruct.getWindowList().size(),
            i -> reportStruct.getWindowList().get(i).getEnd()
    );
  }

  public Road getLastRoadClicked() {
    return lastRoadClicked;
  }

  public Node getLastNodeClicked() {
    return lastNodeClicked;
  }

  public PlaceOfInterest getLastPOIClicked() {
    return lastPOIClicked;
  }

  public void setUpdate(UpdateObserver update) {
    this.update = update;
  }

  private void stopOperation() {
    editOperationsManager.resetLastNode();
    editOperationsManager.setCurrentOperation(EditOperation.NONE);
    //numberOfLanesPane.setVisible(false);
    //nodeSettingsController.getNodeSettingPane().setVisible(false);
    sceneElementsControl.numberOfLanesPaneSetVisible(false);
    sceneElementsControl.nodeSettingsSetVisible(false);
  }

  public void startSimulation() {
    System.out.println(saveLoadControl.getPathToProjectDir());
    String dirPath = saveLoadControl.getPathToProjectDir();
    engineController.setMapPath(dirPath);
    String delim = dirPath.contains("/") ? "/" : "\\";
    int lastIndexOfDel = dirPath.lastIndexOf(delim);
    engineController.setCarStatePath(dirPath.substring(0, lastIndexOfDel) + delim + "carStateOut.json");
    engineController.setHeatMapPath(dirPath.substring(0, lastIndexOfDel) + delim + "heatMapOut.json");
    engineController.startEngine();
  }

  public void pauseSimulation() {
    System.out.println("WHAT AM I WAS CREATED FOR????");
  }

  public void stopSimulation() {
    engineController.stopEngine();
//    File file = new File(engineController.getHeatMapPath());
    File file = new File("heatMap.json");
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try {
      Reader fileReader = new FileReader(file);
      List<ReportWindowStruct> reportWindowStructList = gson.fromJson(
        fileReader, new TypeToken<List<ReportWindowStruct>>() {
        }.getType());
      reportStruct.setWindowList(reportWindowStructList);
      reportStruct.fillCongestionList(editOperationsManager.getMap().getRoadCount());
      editOperationsManager.setCurrentOperation(EditOperation.REPORT_SHOWING);
      if (reportStruct.getWindowList().size() > 0) {
        editOperationsManager.updateCongestions(reportStruct.getWindowList().get(0));
      }
      //timeLineSlider.setMax(Math.max(reportStruct.getWindowList().size() - 1, 0));
      sceneElementsControl.timeLineSliderSetMax(Math.max(reportStruct.getWindowList().size() - 1, 0));
      sceneElementsControl.timeLineSliderInit(
              reportStruct.getWindowList().size(),
              i -> reportStruct.getWindowList().get(i).getEnd()
      );
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    }
    //editOperationsManager.setCurrentOperation(EditOperation.NONE);
  }

  public void roadButtonClicked() {
    closeAllSettings();
    switch (editOperationsManager.getCurrentOperation()) {
      case ROAD_CREATION -> {
        //numberOfLanesPane.setVisible(false);
        sceneElementsControl.numberOfLanesPaneSetVisible(false);
        editOperationsManager.resetLastNode();
        editOperationsManager.setCurrentOperation(EditOperation.NONE);
      }
      default -> {
        //numberOfLanesPane.setVisible(true);
        sceneElementsControl.numberOfLanesPaneSetVisible(true);
        editOperationsManager.resetLastNode();
        editOperationsManager.setCurrentOperation(EditOperation.ROAD_CREATION);
      }
    }
  }

  public void trafficLightButtonClicked() {
    System.out.println("OUTED");
    switch (editOperationsManager.getCurrentOperation()) {
      case TRAFFIC_LIGHT_CREATION -> {
        editOperationsManager.setCurrentOperation(EditOperation.NONE);
      }
      default -> {
        closeAllSettings();
        editOperationsManager.setCurrentOperation(EditOperation.TRAFFIC_LIGHT_CREATION);
      }
    }
  }

  public void buildingButtonClicked() {
    closeAllSettings();
    switch (editOperationsManager.getCurrentOperation()) {
      case POI_CREATION -> {
        editOperationsManager.setCurrentOperation(EditOperation.NONE);
      }
      default -> {
        closeAllSettings();
        editOperationsManager.setCurrentOperation(EditOperation.POI_CREATION);
      }
    }
  }

  public void carButtonClicked() {
    System.out.println("машина");
  }

  public void roadSignButtonClicked() {
    closeAllSettings();
    switch (editOperationsManager.getCurrentOperation()) {
      case SIGN_CREATION:
        //roadSignPane.setVisible(false);
        sceneElementsControl.roadSignMenuSetVisible(false);
        editOperationsManager.setCurrentOperation(EditOperation.NONE);
        break;
      default:
        //roadSignPane.setVisible(true);
        sceneElementsControl.roadSignMenuSetVisible(true);
        //roadSettingsController.getRoadSettingsPane().setVisible(false);
        sceneElementsControl.roadSettingsSetVisible(false);
        //numberOfLanesPane.setVisible(false);
        sceneElementsControl.numberOfLanesPaneSetVisible(false);
        editOperationsManager.setCurrentOperation(EditOperation.SIGN_CREATION);
        break;
    }
  }

  public void showStatistic() {
    sceneElementsControl.statisticSwitchVisible();
  }

  public void setSpeedSign(int speed) {
    editOperationsManager.setCurrSign(signFactory.getSpeedLimitSign(speed));
  }

  public void setMainRoad() {
    editOperationsManager.setCurrSign(signFactory.getMainRoadSign());
  }

  public void onLeftLanesTextFieldChange(String newValue) {
    editOperationsManager.setLanesNumLeft(Integer.parseInt(newValue));
  }

  public void onRightLanesTextFieldChange(String newValue) {
    editOperationsManager.setLanesNumRight(Integer.parseInt(newValue));
  }

  public void onTimeLineSliderChange(Number newValue) {
    if (reportStruct.getWindowList().size() > (int)Math.round(newValue.doubleValue())) {
      editOperationsManager.updateCongestions(reportStruct.getWindowList().get((int) Math.round(newValue.doubleValue())));
    }
  }

  public void onBasePaneClickedFilter(
    MouseEventWrapper event,
    double basePaneWidth,
    double basePaneHeight,
    double windowWidth,
    double windowHeight) {
    lastBaseX = (Math.min(
      event.getX(),
      basePaneWidth - windowWidth));
    lastBaseY = (Math.min(
      event.getY(),
      basePaneHeight - windowHeight));
  }

  public void onMainPaneReleased(MouseEventWrapper event) {
    if (event.getButton() == MouseEventWrapperButton.PRIMARY) {
      switch (editOperationsManager.getCurrentOperation()) {
        case POI_CREATION -> {
          double x = Math.min(event.getX(), lastClickX);
          double y = Math.min(event.getY(), lastClickY);
          double width = Math.abs(event.getX() - lastClickX);
          double height = Math.abs(event.getY() - lastClickY);
          editOperationsManager.addPlaceOfInterest(x, y, width, height);
          //statisticsController.updateStatistics();
          //mainPane.getChildren().remove(selectRect);
          sceneElementsControl.removeSelectRect();
          statisticControl.updateStatistics();
        }
      }
    }
  }

  public void onMainPanePressed(MouseEventWrapper event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        lastClickX = event.getX();
        lastClickY = event.getY();
        switch (editOperationsManager.getCurrentOperation()) {
          case POI_CREATION -> {
            //selectRect.setX(event.getX());
            //selectRect.setY(event.getY());
            //mainPane.getChildren().add(selectRect);
            sceneElementsControl.addSelectRect(event.getX(), event.getY());
          }
        }
      }
    }
  }

  public void onPoiClicked(PlaceOfInterest placeOfInterest, MouseEventWrapper event) {
    lastPOIClicked = placeOfInterest;
    switch (event.getButton()) {
      case PRIMARY -> {
        switch (editOperationsManager.getCurrentOperation()) {
          case ROAD_CREATION -> {
            event.consume();
            editOperationsManager.buildRoadOnPlaceOfInterest(event.getX(), event.getY(), placeOfInterest);
          }
          case NONE -> {
            event.consume();
//                        buildingSettingsController.getPane().setLayoutX(lastXbase);
//                        buildingSettingsController.getPane().setLayoutY(lastYbase);

//                        buildingSettingsController.getSlider().setValue(lastPOIClicked.getWeight());
//                        buildingSettingsController.getParkingPlaces().setText
//                                (String.valueOf(lastPOIClicked.getNumberOfParkingPlaces()));

//                        buildingSettingsController.getPane().setVisible(true);
            sceneElementsControl.buildingSettingsSetPos(lastBaseX, lastBaseY);
            sceneElementsControl.buildingSettingsSetParams(
              placeOfInterest.getWeight(), placeOfInterest.getNumberOfParkingPlaces());
            sceneElementsControl.buildingSettingsSetVisible(true);
          }
        }
      }
    }
  }

  /**
   * Обработка клика по полю
   *
   * @param event событие
   */
  public void onMainPaneClicked(MouseEventWrapper event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        System.out.println("MAIN PANE CLICK");
        switch (editOperationsManager.getCurrentOperation()) {
          case ROAD_CREATION -> {
            event.consume();
            editOperationsManager.buildRoadOnEmpty(event.getX(), event.getY());
            statisticControl.updateStatistics();
          }
          case SIGN_CREATION -> {
            event.consume();
            editOperationsManager.setCurrentOperation(EditOperation.NONE);
            //roadSignPane.setVisible(false);
            sceneElementsControl.roadSignMenuSetVisible(false);
          }
          case NONE -> {
            //roadSettingsController.getRoadSettingsPane().setVisible(false);
            sceneElementsControl.roadSettingsSetVisible(false);
          }
        }
      }
      case SECONDARY -> stopOperation();
    }
  }

  public void onNodeClick(Node node, MouseEventWrapper event) {
    //System.out.println("NODE CLICK");
    lastNodeClicked = node;
    switch (event.getButton()) {
      case PRIMARY -> {
        event.consume();
        switch (editOperationsManager.getCurrentOperation()) {
          case ROAD_CREATION -> {
            editOperationsManager.buildRoadOnNode(node);
            statisticControl.updateStatistics();
          }
          case TRAFFIC_LIGHT_CREATION -> {
            if (node.getRoadsInNum() <= 2) {
              break;
            }
//                        trafficLightController.getTrafficLightPane().setVisible(true);

//                        trafficLightController.getTrafficLightPane().setLayoutX(lastXbase);
//                        trafficLightController.getTrafficLightPane().setLayoutY(lastYbase);

//                        trafficLightController.setLastNodeClicked(node);
//                        trafficLightController.updateDelay(node);
            sceneElementsControl.trafficLightSettingsSetVisible(true);
            sceneElementsControl.trafficLightSettingsSetPos(lastBaseX, lastBaseY);
            if (lastNodeClicked.getTrafficLight() == null) {
              //greenDelay.setText("30");
              //redDelay.setText("30");
              sceneElementsControl.trafficLightSettingsSetParams(30, 30);
            } else {
              sceneElementsControl.trafficLightSettingsSetParams(
                lastNodeClicked.getTrafficLight().getGreenDelay(),
                lastNodeClicked.getTrafficLight().getRedDelay()
              );
            }
            editOperationsManager.updateRoadsHighLight(node);
          }
          case NONE -> {
            stopOperation();
            if (node.getPlaceOfInterest() != null) {
              if (node.getSpawners() != null) {
//                                nodeSettingsController.getStartTime().setValue(LocalTime.parse(node.getSpawners().get(0).getStartString()));
//                                nodeSettingsController.getEndTime().setValue(LocalTime.parse(node.getSpawners().get(0).getEndString()));
//                                nodeSettingsController.getSpawnerRate().setText(String.valueOf(node.getSpawners().get(0).getSpawnRate()));
                sceneElementsControl.nodeSettingsSetParams(
                  LocalTime.parse(node.getSpawners().get(0).getStartString()),
                  LocalTime.parse(node.getSpawners().get(0).getEndString()),
                  node.getSpawners().get(0).getSpawnRate()
                );
              }
//                            nodeSettingsController.getNodeSettingPane().setLayoutX(lastXbase);
//                            nodeSettingsController.getNodeSettingPane().setLayoutY(lastYbase);
//                            nodeSettingsController.getNodeSettingPane().setVisible(true);
              sceneElementsControl.nodeSettingsSetPos(lastBaseX, lastBaseY);
              sceneElementsControl.nodeSettingsSetVisible(true);
            }
            statisticControl.updateStatistics();
          }
        }
      }
    }
    //todo другие операции
  }

  /**
   * Обработка драга на поле
   *
   * @param event событие
   */
  public void onMainPaneDrag(MouseEventWrapper event) {
    if (event.getButton() == MouseEventWrapperButton.PRIMARY) {
      switch (editOperationsManager.getCurrentOperation()) {
        case POI_CREATION -> {
//                    double width = event.getX() - selectRect.getX();
//                    double height = event.getY() - selectRect.getY();
//                    if (width < 0) {
//                        selectRect.setTranslateX(width);
//                        selectRect.setWidth(-width);
//                    } else {
//                        selectRect.setTranslateX(0);
//                        selectRect.setWidth(width);
//                    }
//                    if (height < 0) {
//                        selectRect.setTranslateY(height);
//                        selectRect.setHeight(-height);
//                    } else {
//                        selectRect.setTranslateY(0);
//                        selectRect.setHeight(height);
//                    }
          sceneElementsControl.resizeSelectRect(event.getX(), event.getY());
        }
      }
    }
  }

  public void onRoadClick(Road road, int i, MouseEventWrapper event) {
    //System.out.println("ROAD CLICK");
    Lane lane = road.getLane(i);
    switch (event.getButton()) {
      case PRIMARY -> {
        switch (editOperationsManager.getCurrentOperation()) {
          case ROAD_CREATION -> {
            event.consume();
            editOperationsManager.buildRoadOnRoad(event.getX(), event.getY(), road);
            statisticControl.updateStatistics();
          }
          case NONE -> {
            event.consume();
            lastRoadClicked = road;
//                        roadSettingsController.updateRoad(road);
//                        roadSettingsController.getRoadSettingsPane().setVisible(true);
//                        roadSettingsController.getRoadSettingsHelperPane().setLayoutX(lastBaseX);
//                        roadSettingsController.getRoadSettingsHelperPane().setLayoutY(lastBaseY);
            sceneElementsControl.roadSettingsSetVisible(true);
            sceneElementsControl.roadSettingsSetPos(lastBaseX, lastBaseY);
            String street = "";
            if (road.getStreet() != null) {
              street = road.getStreet().getName();
            }
            sceneElementsControl.roadSettingsSetParams(road.getLanesNum(), street);
          }
          case SIGN_CREATION -> {
            RoadSign addedSign = editOperationsManager.getCurrSign().getCopySign();
            lane.addSign(addedSign);
            if (editOperationsManager.getCurrSign().getSignType() == SignType.MAIN_ROAD) {
              for (int k = 0; k < road.getLanesNum(); k++) {
                road.getLane(k).addSign(addedSign);
              }
            }
            update.update(editOperationsManager);
          }
        }
      }
    }
    //todo другие операции
  }

  private void closeAllSettings() {
//        buildingSettingsController.getPane().setVisible(false);
//        nodeSettingsController.getNodeSettingPane().setVisible(false);
//        numberOfLanesPane.setVisible(false);
//        roadSettingsController.getRoadSettingsPane().setVisible(false);
//        trafficLightController.getTrafficLightPane().setVisible(false);
//        roadSignPane.setVisible(false);
    sceneElementsControl.buildingSettingsSetVisible(false);
    sceneElementsControl.nodeSettingsSetVisible(false);
    sceneElementsControl.numberOfLanesPaneSetVisible(false);
    sceneElementsControl.roadSettingsSetVisible(false);
    sceneElementsControl.trafficLightSettingsSetVisible(false);
    sceneElementsControl.roadSignMenuSetVisible(false);
  }
}
