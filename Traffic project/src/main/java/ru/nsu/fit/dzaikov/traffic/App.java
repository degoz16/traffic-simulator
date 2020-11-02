package ru.nsu.fit.dzaikov.traffic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {



  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("view/MainView.fxml"));
    Scene scene = new Scene(root);
    stage.setTitle("Traffic simulator");
    stage.setScene(scene);
    stage.setWidth(800);
    stage.setHeight(600);
    stage.show();
  }

  @Override
  public void stop() {

  }

  public static void main(String[] args) {
    launch(args);
  }
}
