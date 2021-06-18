package ru.nsu.fit.traffic.javafx.controller.edit;

import java.io.IOException;
import java.util.List;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.App;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.GlobalMapSelectorInitializer;
import ru.nsu.fit.traffic.controller.SelectorSceneElementsControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorControllerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorInitializerInterface;
import ru.nsu.fit.traffic.javafx.controller.rooms.RoomController;
import ru.nsu.fit.traffic.javafx.paiters.UiPainter;
import ru.nsu.fit.traffic.utils.Pair;
import ru.nsu.fit.traffic.view.GlobalMapEditorViewUpdater;
import ru.nsu.fit.traffic.view.GlobalMapObjectPainter;

public class GlobalSelectorController {
  private Stage stage;
  @FXML private ScrollPane mainScrollPane;
  @FXML private Group scrollPaneContent;
  @FXML private Pane mainPane;
  @FXML private AnchorPane basePane;
  @FXML private VBox centeredField;
  @FXML private AnchorPane adminPane;
  @FXML private Text currentOperation;
  private final Circle connectorIcon = UiPainter.getConnectorIcon();
  private boolean isConnectorIconVisible = false;
  private GlobalMapObjectPainter painter;
  private GlobalMapSelectorControllerInterface selectorControl;

  private final SelectorSceneElementsControl sceneElementsControl = new SelectorSceneElementsControl() {
    @Override
    public void loadFragmentScene(String path) {
      try {
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
          controller.showSaveMap();
          controller.setStage(stage);
          if (path != null) {
            controller.initMap(path);
            System.out.println(path);
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

    @Override
    public void setConnectorIconVisible(boolean visible) {
      isConnectorIconVisible = visible;
    }

    @Override
    public void redrawConnectorIcon() {
      mainPane.getChildren().remove(connectorIcon);
      mainPane.getChildren().add(connectorIcon);
    }

    @Override
    public void setCurrentOperation(String currOperation){
      currentOperation.setText(currOperation);
    }
  };

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  private void rePosConnectorIcon(double x, double y) {
    UiPainter.rePosConnectorIcon(x, y, connectorIcon);
  }

  private void addConnectorIcon(double x, double y) {
    UiPainter.addConnectorIcon(x, y, connectorIcon, mainPane);
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
    GlobalMapSelectorInitializerInterface initializer = new GlobalMapSelectorInitializer(sceneElementsControl);
    selectorControl = initializer.getSelectorControl();
    addConnectorIcon(0, 0);
    ConnectionConfig connectionConfig = ConnectionConfig.getConnectionConfig();
    List<Long> blocks = connectionConfig.getConnection().blockedMaps(connectionConfig.getRoomId());
    if (!connectionConfig.getConnection().isAdmin(connectionConfig.getRoomId())) {
      setVisibleFalse();
    }
    mainScrollPane.setHvalue(0.5);
    mainScrollPane.setVvalue(0.5);
    GlobalMapEditorViewUpdater viewUpdater =
        new GlobalMapEditorViewUpdater(
            ((rect, id, regW, regH) -> {
              rect.setOnMouseMoved(
                  event -> {

                    Pair<Double, Double> coords =
                        selectorControl.getSideCoordinates(
                            event.getX(), event.getY(), rect.getX(), rect.getY(), regW, regH);
                    if (isConnectorIconVisible) {
                      if (selectorControl.testRegionsBounds(event.getX(), event.getY(), id)) {
                        connectorIcon.setVisible(true);
                        rePosConnectorIcon(coords.getFirst(), coords.getSecond());
                      } else {
                        connectorIcon.setVisible(false);
                      }
                    }
                  });
              rect.setOnMouseClicked(
                  event -> {
                    try {
                      selectorControl.onRegionClick(id, MouseEventWrapper.getMouseEventWrapper(event));
                    } catch (Exception e) {
                      e.printStackTrace();
                      Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                      errorAlert.setHeaderText("Connection error");
                      errorAlert.setContentText("Error while trying to get map from server");
                      errorAlert.showAndWait();
                    }
                  });
            }),
            (regId, conId, shape) -> {
              shape.setOnMouseClicked(
                  event -> {
                    try {
                      selectorControl.onConnectorClicked(regId, conId);
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                  });
            },
            mainPane);
    initializer.initialize(viewUpdater::updateMapView);
  }

  @FXML
  public void setConnector(){
    selectorControl.onSetConnector();
  }

  @FXML
  public void deleteConnector(){
    selectorControl.onDeleteConnector();
  }

  @FXML
  public void kick(){
    selectorControl.onKick();
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
        controller.showGoBack();
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

  private void setVisibleFalse(){
    adminPane.setVisible(false);
  }

  public void setMap(String map) {
    selectorControl.setRegionMap(map);
    mainPane.setPrefWidth(selectorControl.getMapWidth()/6);
    mainPane.setMaxWidth(selectorControl.getMapWidth()/6);
    mainPane.setPrefHeight(selectorControl.getMapHeight()/6);
    mainPane.setMaxHeight(selectorControl.getMapHeight()/6);
  }
}
