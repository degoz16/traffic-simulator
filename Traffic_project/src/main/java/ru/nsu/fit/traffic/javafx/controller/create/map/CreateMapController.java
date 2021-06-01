package ru.nsu.fit.traffic.javafx.controller.create.map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.App;
import ru.nsu.fit.traffic.javafx.controller.edit.GlobalMapController;

import java.io.IOException;
import java.util.function.UnaryOperator;

public class CreateMapController {

  private @FXML TextField text_field_wight;
  private @FXML TextField text_field_height;

  private Stage stage;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  public void initialize() {
    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
      String input = change.getText();
      int text_size = change.getControlNewText().length();

      if (input.matches("[0-9]*") && text_size <= 4) {
        return change;
      }
      return null;
    };

    text_field_wight.setTextFormatter(new TextFormatter<>(integerFilter));
    text_field_height.setTextFormatter(new TextFormatter<>(integerFilter));
  }

  @FXML
  public void createMap() {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("view/GlobalMapView.fxml"));
    try {
      Parent root = loader.load();
      Scene scene = new Scene(root);

      stage.setTitle("Traffic simulator");
      stage.setScene(scene);
      GlobalMapController controller = loader.getController();
      controller.setStage(stage);

      controller.setMapParams(
          Integer.parseInt(text_field_wight.getText()) * 3.3,
          Integer.parseInt(text_field_height.getText()) * 3.3);

      stage.setWidth(850);
      stage.setHeight(540);
      stage.show();

      stage.setMinWidth(850);
      stage.setMinHeight(540);

      stage.setMaxHeight(100000);
      stage.setMaxWidth(100000);
    } catch (IOException e) {
      // todo
    }
  }
}
