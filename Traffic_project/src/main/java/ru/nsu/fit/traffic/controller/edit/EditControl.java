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
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.BaseControl;
import ru.nsu.fit.traffic.controller.SceneElementsControl;
import ru.nsu.fit.traffic.controller.engine.EngineController;
import ru.nsu.fit.traffic.controller.notification.NotificationType;
import ru.nsu.fit.traffic.controller.menu.MenuControl;
import ru.nsu.fit.traffic.controller.statistic.StatisticControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapperButton;
import ru.nsu.fit.traffic.interfaces.control.EditControlInterface;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.model.congestion.ReportStruct;
import ru.nsu.fit.traffic.model.congestion.ReportWindowStruct;
import ru.nsu.fit.traffic.model.logic.EditOperation;
import ru.nsu.fit.traffic.model.logic.UpdateObserver;
import ru.nsu.fit.traffic.model.map.Node;
import ru.nsu.fit.traffic.model.map.PlaceOfInterest;
import ru.nsu.fit.traffic.model.playback.PlaybackStruct;
import ru.nsu.fit.traffic.model.map.Lane;
import ru.nsu.fit.traffic.model.map.Road;
import ru.nsu.fit.traffic.model.map.RoadSign;
import ru.nsu.fit.traffic.model.map.SignFactory;
import ru.nsu.fit.traffic.model.map.SignType;

public class EditControl extends BaseControl implements EditControlInterface {
  private final ReportStruct reportStruct = new ReportStruct();
  private final SignFactory signFactory = new SignFactory();
  private final StatisticControl statisticControl;
  private final MenuControl saveLoadControl;
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
                     MenuControl saveLoadControl,
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
    sceneElementsControl.setSelectRectVisible(false);
  }

  public void startSimulation() {
    String dirPath = saveLoadControl.getPathToProjectDir();
    saveLoadControl.onSave();
    engineController.setMapPath(dirPath);
    String delimiter = dirPath.contains("/") ? "/" : "\\";
    int lastIndexOfDel = dirPath.lastIndexOf(delimiter);
    engineController.setCarStatePath(dirPath.substring(0, lastIndexOfDel) + delimiter + "carStateOut.json");
    engineController.setHeatMapPath(dirPath.substring(0, lastIndexOfDel) + delimiter + "heatMapOut.json");
    engineController.startEngine();
    sceneElementsControl.simulationProcessModeEnable();
    editOperationsManager.setCurrentOperation(EditOperation.SIMULATION);
    sceneElementsControl.setSelectRectVisible(false);
  }

  @Override
  public void startOptimization() {
    String dirPath = saveLoadControl.getPathToProjectDir();
    saveLoadControl.onSave();
    engineController.setMapPath(dirPath);
    engineController.setOptimizedMapPath(
      dirPath.substring(0,dirPath.lastIndexOf(".")) + "optimized.tsp"
    );
    engineController.startOptimizing();
    sceneElementsControl.optimizationProcessModeEnabled();
    editOperationsManager.setCurrentOperation(EditOperation.OPTIMIZATION);
    sceneElementsControl.setSelectRectVisible(false);
  }

  public void stopSimulation() {
    engineController.stopEngine();
    sceneElementsControl.simulationStopModeEnable();
    //editOperationsManager.setCurrentOperation(EditOperation.NONE);
    sceneElementsControl.setSelectRectVisible(false);
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
    sceneElementsControl.setSelectRectVisible(false);
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
    sceneElementsControl.setSelectRectVisible(false);
    if (editOperationsManager.getCurrentOperation() == EditOperation.TIME_PICKING) {
      editOperationsManager.setCurrentOperation(EditOperation.NONE);
      editOperationsManager.setEndTime(time);
    } else {
      editOperationsManager.setCurrentOperation(EditOperation.TIME_PICKING);
      editOperationsManager.setStartTime(time);
    }
  }

  public void trafficLightButtonClicked() {
    sceneElementsControl.setSelectRectVisible(false);
    switch (editOperationsManager.getCurrentOperation()) {
      case TRAFFIC_LIGHT_CREATION -> {
        editOperationsManager.setCurrentOperation(EditOperation.NONE);
      }
      default -> {
        closeAllSettings();
        sceneElementsControl.showNotification("Traffic light creation.",
          "Click on crossroad \nwith 3 and more roads.",
          NotificationType.INFORMATION);
        editOperationsManager.setCurrentOperation(EditOperation.TRAFFIC_LIGHT_CREATION);
      }
    }
  }

  public void buildingButtonClicked() {
    closeAllSettings();
    sceneElementsControl.setSelectRectVisible(true);
    switch (editOperationsManager.getCurrentOperation()) {
      case POI_CREATION -> {
        editOperationsManager.setCurrentOperation(EditOperation.NONE);
      }
      default -> {
        sceneElementsControl.showNotification(
          "Building creation",
          "Click on field and drag\nto create building.",
          NotificationType.INFORMATION
        );
        closeAllSettings();
        editOperationsManager.setCurrentOperation(EditOperation.POI_CREATION);
      }
    }
  }

  public void playbackClicked() {
    sceneElementsControl.setSelectRectVisible(false);
    editOperationsManager.setCurrentOperation(EditOperation.PLAYBACK_SHOWING);
    sceneElementsControl.playBackModeEnable();
    playbackStruct = new PlaybackStruct(editOperationsManager.getMap());
    playbackStruct.fillInTimeMap(engineController.getCarStatePath());
    sceneElementsControl.playbackSliderInit(playbackStruct.getMaxTime(), playbackStruct.getMinTime());
    editOperationsManager.updateCarStates(playbackStruct.getCarStates(playbackStruct.getMinTime()));
  }

  public void reportClicked() {
    sceneElementsControl.setSelectRectVisible(false);
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
    sceneElementsControl.setSelectRectVisible(false);
    editOperationsManager.setCurrentOperation(EditOperation.NONE);
    sceneElementsControl.editModeEnable();
    editOperationsManager.updateCarStates(new ArrayList<>());
  }

  public void roadSignButtonClicked() {
    closeAllSettings();
    sceneElementsControl.setSelectRectVisible(false);
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
    sceneElementsControl.setSelectRectVisible(false);
    sceneElementsControl.statisticSwitchVisible();
  }

  public void setSpeedSign(int speed) {
    sceneElementsControl.setSelectRectVisible(false);
    editOperationsManager.setCurrSign(signFactory.getSpeedLimitSign(speed));
  }

  public void setMainRoad() {
    sceneElementsControl.setSelectRectVisible(false);
    editOperationsManager.setCurrSign(signFactory.getMainRoadSign());
  }

  public void onLeftLanesTextFieldChange(String newValue) {
    editOperationsManager.setLanesNumLeft(Integer.parseInt(newValue));
  }

  public void onRightLanesTextFieldChange(String newValue) {
    editOperationsManager.setLanesNumRight(Integer.parseInt(newValue));
  }

  public void onTimeLineSliderChange(Number newValue) {
    sceneElementsControl.setSelectRectVisible(false);
    if (reportStruct.getWindowList().size() > (int) Math.round(newValue.doubleValue())) {
      editOperationsManager.updateCongestions(reportStruct.getWindowList().get((int) Math.round(newValue.doubleValue())));
    }
  }

  public void onPlaybackLineSliderChange(Number newVal) {
    sceneElementsControl.setSelectRectVisible(false);
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
      }
    }
  }

  public void onPoiClicked(int id, MouseEventWrapper event) {
    PlaceOfInterest placeOfInterest = editOperationsManager.getMap().getPlaceOfInterest(id);
    lastPOIClicked = placeOfInterest;
    switch (event.getButton()) {
      case PRIMARY -> {
        switch (editOperationsManager.getCurrentOperation()) {
          case ROAD_CREATION -> {
            event.consume();
            //System.out.println(placeOfInterest);
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
        switch (editOperationsManager.getCurrentOperation()) {
          case ROAD_CREATION -> {
            event.consume();
            int res = editOperationsManager.buildRoadOnEmpty(event.getX(), event.getY());
            if (res == -1){
              sceneElementsControl.showNotification("Road creation.",
                      "There can be 4 or less\nroads in the crossroad.",
                      NotificationType.WARNING);
            }
            statisticControl.updateStatistics();
          }
          case SIGN_CREATION -> {
            event.consume();
            editOperationsManager.setCurrentOperation(EditOperation.NONE);
            sceneElementsControl.setSelectRectVisible(false);
            sceneElementsControl.roadSignMenuSetVisible(false);
          }
          case NONE -> sceneElementsControl.roadSettingsSetVisible(false);
        }
      }
      case SECONDARY -> {
        stopOperation();
        sceneElementsControl.setSelectRectVisible(false);
      }
    }
  }

  public void onNodeClick(int id, MouseEventWrapper event) {
    Node node = editOperationsManager.getMap().getNode(id);
    lastNodeClicked = node;
    switch (event.getButton()) {
      case PRIMARY -> {
        event.consume();
        switch (editOperationsManager.getCurrentOperation()) {
          case ROAD_CREATION -> {
            if (editOperationsManager.buildRoadOnNode(node) == -1){
              sceneElementsControl.showNotification("Road creation.",
                      "There can be 4 or less\nroads in the crossroad.",
                      NotificationType.WARNING);
            }
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
   * Обработка перетаскивания на поле
   *
   * @param event событие
   */
  public void onMainPaneDrag(MouseEventWrapper event) {

  }

  public void onRoadClick(int id, int i, MouseEventWrapper event) {
    Road road = editOperationsManager.getMap().getRoad(id);
    Lane lane = road.getLane(i);
    switch (event.getButton()) {
      case PRIMARY -> {
        switch (editOperationsManager.getCurrentOperation()) {
          case ROAD_CREATION -> {
            event.consume();
            int res = editOperationsManager.buildRoadOnRoad(event.getX(), event.getY(), road);
            if (res == -1){
              sceneElementsControl.showNotification("Road creation.",
                      "There can be 4 or less\nroads in the crossroad.",
                      NotificationType.WARNING);
            }
            if (res == -2){
              sceneElementsControl.showNotification("Road creation.",
                      "Road can't be places\non road.",
                      NotificationType.WARNING);
            }
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
  }

  @Override
  public boolean saveMap() {
    Connection connection = ConnectionConfig.getConnectionConfig().getConnection();
    return connection.pushMap(
      ConnectionConfig.getConnectionConfig().getMapId(),
      ConnectionConfig.getConnectionConfig().getRoomId(),
      saveLoadControl.getPathToProjectDir()
    );
  }

  private void closeAllSettings() {
    sceneElementsControl.buildingSettingsSetVisible(false);
    sceneElementsControl.nodeSettingsSetVisible(false);
    sceneElementsControl.numberOfLanesPaneSetVisible(false);
    sceneElementsControl.roadSettingsSetVisible(false);
    sceneElementsControl.trafficLightSettingsSetVisible(false);
    sceneElementsControl.roadSignMenuSetVisible(false);
    sceneElementsControl.setSelectRectVisible(false);
  }

  @Override
  public double getMapWidth() {
    return editOperationsManager.getMap().getWidth();
  }

  @Override
  public double getMapHeight() {
    return editOperationsManager.getMap().getHeight();
  }
}
