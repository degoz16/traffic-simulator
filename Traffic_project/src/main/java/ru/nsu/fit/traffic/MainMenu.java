package ru.nsu.fit.traffic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.javafx.controller.rooms.LoginController;

public class MainMenu extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Traffic simulator");
        stage.setScene(scene);
        stage.setWidth(310);
        stage.setMaxWidth(310);
        stage.setMinWidth(310);

        stage.setHeight(280);
        stage.setMinHeight(280);
        stage.setMaxHeight(280);

        stage.show();

        LoginController controller = loader.getController();
        controller.setStage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
