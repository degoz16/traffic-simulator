package ru.nsu.fit.traffic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.controller.MainController;

public class App extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainView.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);

    stage.setTitle("Traffic simulator");
    stage.setScene(scene);
    stage.setWidth(800);
    stage.setHeight(520);
    stage.show();

    MainController controller = loader.getController();
    controller.setPrimaryStage(stage);

    stage.setMinWidth(800);
    stage.setMinHeight(520);
  }

  @Override
  public void stop() {

  }

  public static void main(String[] args) {
    launch(args);
  }
}
