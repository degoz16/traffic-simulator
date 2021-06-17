package ru.nsu.fit.traffic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.javafx.controller.edit.MainController;

public class MainMenu extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Traffic simulator");
        stage.setScene(scene);

        stage.show();

        MainController controller = loader.getController();
        controller.setStage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
