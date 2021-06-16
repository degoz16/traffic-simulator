package ru.nsu.fit.traffic.javafx.controller.edit;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.App;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.GlobalMapSelectorInitializer;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorControllerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorInitializerInterface;
import ru.nsu.fit.traffic.javafx.controller.rooms.RoomController;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOp;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;
import ru.nsu.fit.traffic.view.GlobalMapEditorViewUpdater;
import ru.nsu.fit.traffic.view.GlobalMapObjectPainter;

public class GlobalSelectorController {
  private Stage stage;
  @FXML private ScrollPane mainScrollPane;
  @FXML private Group scrollPaneContent;
  @FXML private Pane mainPane;
  @FXML private AnchorPane basePane;
  @FXML private VBox centeredField;
  private GlobalMapObjectPainter painter;
  private GlobalMapSelectorControllerInterface selectorControl;
  private GlobalMapEditOpManager editOpManager;
  private GlobalMapUpdateObserver updateObserver;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  public void goBack() {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("view/RoomButtonsView.fxml"));
    Parent root;
    try {
      root = loader.load();
      stage.getScene().setRoot(root);
      RoomController roomController = loader.getController();
      roomController.setStage(stage);
    } catch (IOException e) {
      e.printStackTrace();
    }
    stage.show();
  }

  @FXML
  public void initialize() {
    painter = new GlobalMapObjectPainter();
    GlobalMapSelectorInitializerInterface initializer = new GlobalMapSelectorInitializer();
    selectorControl = initializer.getSelectorControl();

    GlobalMapEditorViewUpdater viewUpdater =
        new GlobalMapEditorViewUpdater(
            ((rect, id, regW, regH) -> {
              rect.setOnMouseClicked(
                  event -> {
                    if (editOpManager.getCurrentOp() == GlobalMapEditOp.NONE) {
                      String partFilepath = null;
                      try {
                        partFilepath =
                            selectorControl.onRegionClick(
                                id, MouseEventWrapper.getMouseEventWrapper(event));
                        FXMLLoader loader =
                            new FXMLLoader(App.class.getResource("view/MainView.fxml"));
                        Parent root = null;
                        try {
                          root = loader.load();
                          Scene scene = new Scene(root);

                          stage.setTitle("Traffic simulator");
                          stage.setScene(scene);
                          stage.show();

                          MainController controller = loader.getController();
                          controller.setStage(stage);
                          if (partFilepath != null) {
                            controller.initMap(partFilepath);
                            System.out.println(partFilepath);
                          }
                          ConnectionConfig.getConnectionConfig().setMapId(id);
                        } catch (IOException e) {
                          e.printStackTrace();
                        }
                      } catch (Exception e) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("Connection error");
                        errorAlert.setContentText("Error while trying to get map from server");
                        errorAlert.showAndWait();
                      }
                    }
                    if (editOpManager.getCurrentOp() == GlobalMapEditOp.SET_CONNECTOR){
                      //todo: Привет! Сюда вставь чеки для сервера
                      rect.setOnMouseClicked(
                              event2 -> {
                                try {
                                  selectorControl.onRegionClick(id, MouseEventWrapper.getMouseEventWrapper(event));
                                } catch (Exception e) {
                                  e.printStackTrace();
                                }
                              });
                    }
                    if (editOpManager.getCurrentOp() == GlobalMapEditOp.KICK_USER){
                      //todo: Сюда нужно вставить вообще всё
                    }
                  });
            }),
            (connector, shape) -> {
              shape.setOnMouseClicked(
                      event -> {
                        selectorControl.onConnectorClicked(connector);
                      });
            },
            mainPane);
    initializer.initialize(viewUpdater::updateMapView);
    editOpManager = new GlobalMapEditOpManager(viewUpdater::updateMapView);
  }

  @FXML
  public void setConnector(){
    if (editOpManager.getCurrentOp() != GlobalMapEditOp.SET_CONNECTOR) {
      editOpManager.setCurrOp(GlobalMapEditOp.SET_CONNECTOR);
    }else{
      editOpManager.setCurrOp(GlobalMapEditOp.NONE);
    }
  }

  @FXML
  public void deleteConnector(){
    if (editOpManager.getCurrentOp() != GlobalMapEditOp.DELETE_CONNECTOR) {
      editOpManager.setCurrOp(GlobalMapEditOp.DELETE_CONNECTOR);
    }else{
      editOpManager.setCurrOp(GlobalMapEditOp.NONE);
    }
  }

  @FXML
  public void kick(){
    if (editOpManager.getCurrentOp() != GlobalMapEditOp.KICK_USER) {
      editOpManager.setCurrOp(GlobalMapEditOp.KICK_USER);
    }else{
      editOpManager.setCurrOp(GlobalMapEditOp.NONE);
    }
  }

  @FXML
  public void mergeMapClicked() {
    try {
      String partFilepath =
          selectorControl.onMergeMap();
      FXMLLoader loader =
          new FXMLLoader(App.class.getResource("view/MainView.fxml"));
      Parent root;
      try {
        root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Traffic simulator");
        stage.setScene(scene);
        stage.show();

        MainController controller = loader.getController();
        controller.setStage(stage);
        if (partFilepath != null) {
          controller.initMap(partFilepath);
          System.out.println(partFilepath);
        }
        //ConnectionConfig.getConnectionConfig().setMapId(id);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
      Alert errorAlert = new Alert(Alert.AlertType.ERROR);
      errorAlert.setHeaderText("Connection error");
      errorAlert.setContentText("Error while trying to get map from server");
      errorAlert.showAndWait();
    }
  }

  public void setMap(String map) {
    selectorControl.setRegionMap(map);
    mainPane.setPrefWidth(selectorControl.getMapWidth());
    mainPane.setMaxWidth(selectorControl.getMapWidth());
    mainPane.setPrefHeight(selectorControl.getMapHeight());
    mainPane.setMaxHeight(selectorControl.getMapHeight());
  }
}
