package ru.nsu.fit.traffic.controller.edit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.time.LocalTime;
import java.util.ArrayList;
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
import ru.nsu.fit.traffic.model.playback.PlaybackStruct;
import ru.nsu.fit.traffic.model.road.Lane;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.trafficsign.RoadSign;
import ru.nsu.fit.traffic.model.trafficsign.SignFactory;
import ru.nsu.fit.traffic.model.trafficsign.SignType;

public class EditControl extends BaseControl implements EditControlInterface{
  private final ReportStruct reportStruct = new ReportStruct();
  private final SignFactory signFactory = new SignFactory();
  private final StatisticControl statisticControl;
  private final SaveLoadControl saveLoadControl;
  private final EngineController engineController;
  private PlaybackStruct playbackStruct;
  private double lastBaseX = 0d;
  private double lastBaseY = 0d;
  private double lastClickX = 0;
  private double lastClickY = 0;
  private Road lastRoadClicked = null;
  private Node lastNodeClicked = null;
  private PlaceOfInterest lastPOIClicked = null;

  public EditControl(SceneElementsControl sceneElementsControl,
                     StatisticControl statisticControl,
                     SaveLoadControl saveLoadControl,
                     EngineController engineController) {
    super(sceneElementsControl);
    this.statisticControl = statisticControl;
    this.saveLoadControl = saveLoadControl;
    this.engineController = engineController;
    sceneElementsControl.timeLineReportSliderInit(
      reportStruct.getWindowList().size(),
      i -> reportStruct.getWindowList().get(i).getEnd()
    );
    sceneElementsControl.editModeEnable();
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
    sceneElementsControl.numberOfLanesPaneSetVisible(false);
    sceneElementsControl.nodeSettingsSetVisible(false);
  }

  public void startSimulation() {
    System.out.println(saveLoadControl.getPathToProjectDir());
//    String dirPath = saveLoadControl.getPathToProjectDir();
    engineController.setMapPath("");
//    String delim = dirPath.contains("/") ? "/" : "\\";
//    int lastIndexOfDel = dirPath.lastIndexOf(delim);
    engineController.setCarStatePath("carStateOut.json");
    engineController.setHeatMapPath("heatMapOut.json");
    engineController.startEngine();
    sceneElementsControl.simulationProcessModeEnable();
    editOperationsManager.setCurrentOperation(EditOperation.SIMULATION);
  }

  public void stopSimulation() {
    engineController.stopEngine();
    sceneElementsControl.simulationStopModeEnable();
    //editOperationsManager.setCurrentOperation(EditOperation.NONE);
  }

  public void rewindForward() {
    if (editOperationsManager.getCurrentOperation() == EditOperation.REPORT_SHOWING) {
      sceneElementsControl.timeLineReportSliderAddValue(1);
    }
    if (editOperationsManager.getCurrentOperation() == EditOperation.PLAYBACK_SHOWING) {
      sceneElementsControl.playbackSliderAddValue(1);
    }
  }

  public void rewindBack() {
    if (editOperationsManager.getCurrentOperation() == EditOperation.REPORT_SHOWING) {
      sceneElementsControl.timeLineReportSliderAddValue(-1);
    }
    if (editOperationsManager.getCurrentOperation() == EditOperation.PLAYBACK_SHOWING) {
      sceneElementsControl.playbackSliderAddValue(-1);
    }
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

  public void startTimePicker(String time) {
    if (editOperationsManager.getCurrentOperation() == EditOperation.TIME_PICKING) {
      editOperationsManager.setCurrentOperation(EditOperation.NONE);
      editOperationsManager.setEndTime(time);
    } else {
      editOperationsManager.setCurrentOperation(EditOperation.TIME_PICKING);
      editOperationsManager.setStartTime(time);
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

  public void playbackClicked() {
    editOperationsManager.setCurrentOperation(EditOperation.PLAYBACK_SHOWING);
    sceneElementsControl.playBackModeEnable();
    playbackStruct = new PlaybackStruct(editOperationsManager.getMap());
    playbackStruct.fillInTimeMap(engineController.getCarStatePath());
    sceneElementsControl.playbackSliderInit(playbackStruct.getMaxTime(), playbackStruct.getMinTime());
    editOperationsManager.updateCarStates(playbackStruct.getCarStates(playbackStruct.getMinTime()));
  }

  public void reportClicked() {
    //DEBUG
    //File file = new File("heatMap.json");
    //DEBUG
    editOperationsManager.updateCarStates(new ArrayList<>());
    File file = new File(engineController.getHeatMapPath());
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
      sceneElementsControl.timeLineReportSliderSetMax(Math.max(reportStruct.getWindowList().size() - 1, 0));
      sceneElementsControl.timeLineReportSliderInit(
        reportStruct.getWindowList().size(),
        i -> reportStruct.getWindowList().get(i).getEnd()
      );
      sceneElementsControl.reportModeEnable();
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    }
  }

  public void editClicked() {
    editOperationsManager.setCurrentOperation(EditOperation.NONE);
    sceneElementsControl.editModeEnable();
    editOperationsManager.updateCarStates(new ArrayList<>());
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
    if (reportStruct.getWindowList().size() > (int) Math.round(newValue.doubleValue())) {
      editOperationsManager.updateCongestions(reportStruct.getWindowList().get((int) Math.round(newValue.doubleValue())));
    }
  }

  public void onPlaybackLineSliderChange(Number newVal) {
    editOperationsManager.updateCarStates(playbackStruct.getCarStates((int) Math.round(newVal.doubleValue())));
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
            sceneElementsControl.roadSignMenuSetVisible(false);
          }
          case NONE -> sceneElementsControl.roadSettingsSetVisible(false);
        }
      }
      case SECONDARY -> stopOperation();
    }
  }

  public void onNodeClick(Node node, MouseEventWrapper event) {
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
            sceneElementsControl.trafficLightSettingsSetVisible(true);
            sceneElementsControl.trafficLightSettingsSetPos(lastBaseX, lastBaseY);
            if (lastNodeClicked.getTrafficLight() == null) {
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
                sceneElementsControl.nodeSettingsSetParams(
                  LocalTime.parse(node.getSpawners().get(0).getStartString()),
                  LocalTime.parse(node.getSpawners().get(0).getEndString()),
                  node.getSpawners().get(0).getSpawnRate()
                );
              }
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
        case POI_CREATION -> sceneElementsControl.resizeSelectRect(event.getX(), event.getY());
      }
    }
  }

  public void onRoadClick(Road road, int i, MouseEventWrapper event) {
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
    sceneElementsControl.buildingSettingsSetVisible(false);
    sceneElementsControl.nodeSettingsSetVisible(false);
    sceneElementsControl.numberOfLanesPaneSetVisible(false);
    sceneElementsControl.roadSettingsSetVisible(false);
    sceneElementsControl.trafficLightSettingsSetVisible(false);
    sceneElementsControl.roadSignMenuSetVisible(false);
  }
}
