package ru.nsu.fit.traffic.javafx.controller.rooms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.App;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.GlobalMapSceneElementsControl;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.javafx.controller.create.map.CreateMapController;
import ru.nsu.fit.traffic.javafx.controller.edit.GlobalSelectorController;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;

public class RoomController {

  @FXML
  private HBox root;
  private ConnectionConfig connectionConfig = ConnectionConfig.getConnectionConfig();
  private Connection connection;
  Stage stage;

  public void setStage(Stage stage){
    this.stage = stage;
  }
  /**
   * инициализация.
   */
  private final GlobalMapSceneElementsControl sceneElementsControl =
    new GlobalMapSceneElementsControl() {
      @Override
      public void setSelectRectVisible(boolean visible) {
      }

      @Override
      public void setConnectorIconVisible(boolean visible) {
      }

      @Override
      public void setCurrentOperation(String operation){

      }
    };


  @FXML
  public void initialize() {
    connection = connectionConfig.getConnection();
    List<Button> buttonList = new ArrayList<>();
    connection.getRooms().forEach(i -> {
      int roomNum = Math.round(i);
      String fileName = connection.getGlobalMapFromServer(roomNum);
      String name = Objects.requireNonNull(GlobalMapEditOpManager.loadRegMap(fileName)).getName();
      Button button = new Button(
        name.replace('+', ' ')
      );
      button.setId(String.valueOf(roomNum));
      buttonList.add(button);
    });
    buttonList.forEach(button -> {
      button.setOnAction(this::buttonClickHandler);
      button.setMinSize(80, 50);
    });
    root.getChildren().addAll(buttonList);
  }

  private void buttonClickHandler(ActionEvent event) {
    Button button = (Button) event.getSource();
    ConnectionConfig.getConnectionConfig().setRoomId(Integer.parseInt(button.getId()));
    FXMLLoader loader = new FXMLLoader(App.class.getResource("view/GlobalMapSelectorView.fxml"));
    try {
      Parent root = loader.load();
      Scene scene = new Scene(root);

      stage.setTitle("Traffic simulator");
      stage.setScene(scene);
      GlobalSelectorController controller = loader.getController();
      controller.setStage(stage);
      controller.setMap(connection.getGlobalMapFromServer(connectionConfig.getRoomId()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    stage.show();

  }

  @FXML
  private void onCreateMapClicked() {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("view/CreateMap.fxml"));
    try {
      Parent root = loader.load();
      Scene scene = new Scene(root);

      stage.setTitle("Traffic simulator");
      stage.setScene(scene);
      CreateMapController controller = loader.getController();
      controller.setStage(stage);
    } catch (IOException e) {
      e.printStackTrace();
    }
    stage.show();
  }

}
