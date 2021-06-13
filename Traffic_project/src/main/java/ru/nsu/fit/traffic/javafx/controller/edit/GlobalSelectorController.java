package ru.nsu.fit.traffic.javafx.controller.edit;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
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
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.view.GlobalMapEditorViewUpdater;
import ru.nsu.fit.traffic.view.GlobalMapObjectPainter;

public class GlobalSelectorController {
  private Stage stage;
  @FXML private ScrollPane mainScrollPane;
  @FXML private Group scrollPaneContent;
  @FXML private Pane mainPane;
  @FXML private AnchorPane basePane;
  @FXML private VBox centeredField;
  @FXML private CheckBox checkBox;
  private GlobalMapObjectPainter painter;
  private GlobalMapSelectorControllerInterface selectorControl;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setCurrentMap(RegionsMap regionsMap) {
    for (RectRegion region : regionsMap.getRegions()) {
      mainPane.getChildren().add(painter.paintRegion(region));
      for (RoadConnector connector : region.getConnectorList()) {
        mainPane.getChildren().add(painter.paintConnector(connector, true));
      }
    }
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
                  });
            }),
            (connector, shape) -> {},
            mainPane);
    initializer.initialize(viewUpdater::updateMapView);
  }

  @FXML
  public void checkBoxClicked() {
    if (checkBox.isSelected()) {
      // todo: show whole map
      System.out.println("show whole map");
    } else {
      // todo: hide map
      System.out.println("hide map");
    }
  }

  public void setMap(RegionsMap map) {
    selectorControl.setRegionMap(map);
  }

  public void setMap(String map) {
    selectorControl.setRegionMap(map);
  }
}
