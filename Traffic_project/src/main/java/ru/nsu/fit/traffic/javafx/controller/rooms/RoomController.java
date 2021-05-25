package ru.nsu.fit.traffic.javafx.controller.rooms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.App;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.GlobalMapEditControlInitializer;
import ru.nsu.fit.traffic.controller.GlobalMapSceneElementsControl;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapControlInitializerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapEditControlInterface;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.javafx.controller.createMap.CreateMapController;
import ru.nsu.fit.traffic.javafx.controller.edit.GlobalMapController;

public class RoomController {

  @FXML
  private HBox hBox;
  private ConnectionConfig connectionConfig = ConnectionConfig.getConnectionConfig();
  private Connection connection;
  private GlobalMapEditControlInterface editControl;
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
    };


  @FXML
  public void initialize() {
    connection = connectionConfig.getConnection();
    List<Button> buttonList = new ArrayList<>();
    connection.getRooms().forEach(i -> buttonList.add(new Button(String.valueOf(Math.round(i)))));
    buttonList.forEach(button -> {
      button.setOnAction(this::buttonClickHandler);
      button.setMinSize(80, 80);
    });
    hBox.getChildren().addAll(buttonList);
  }

  private void buttonClickHandler(ActionEvent event) {
    Button button = (Button) event.getSource();
    FXMLLoader loader = new FXMLLoader(App.class.getResource("view/GlobalMapView.fxml"));
    try {
      Parent root = loader.load();
      Scene scene = new Scene(root);

      stage.setTitle("Traffic simulator");
      stage.setScene(scene);
      GlobalMapController controller = loader.getController();
      controller.setStage(stage);
      controller.getEditControl().updateMap(
        connection.getGlobalMapFromServer(Integer.parseInt(button.getText()))
      );
    } catch (IOException e) {
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
