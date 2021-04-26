package ru.nsu.fit.traffic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.javafx.controller.edit.GlobalMapController;

public class GlobalMapApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/GlobalMapView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Traffic simulator");
        stage.setScene(scene);
        stage.setWidth(850);
        stage.setHeight(540);
        stage.show();

        GlobalMapController controller = loader.getController();
        controller.setStage(stage);

        stage.setMinWidth(850);
        stage.setMinHeight(540);
    }

    @Override
    public void stop() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
