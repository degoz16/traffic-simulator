package ru.nsu.fit.traffic.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.javafx.controller.createMap.CreateMapController;

import java.io.IOException;

public class FirstMenuController {

    Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    public void open_map() {

    }

    @FXML
    public void create_map(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/CreateMap.fxml"));
        System.out.println(getClass());
        try{
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setTitle("Traffic simulator");
            stage.setScene(scene);
            CreateMapController controller = loader.getController();
            controller.setStage(stage);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
