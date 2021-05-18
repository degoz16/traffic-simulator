package ru.nsu.fit.traffic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.javafx.controller.FirstMenuController;

public class MainMenu extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/FirstMenu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Traffic simulator");
        stage.setScene(scene);
        stage.setWidth(300);
        stage.setMaxWidth(300);
        stage.setMinWidth(300);

        stage.setHeight(240);
        stage.setMinHeight(240);
        stage.setMaxHeight(240);

        stage.show();

        FirstMenuController controller = loader.getController();
        controller.setStage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
