package ru.nsu.fit.traffic.javafx.controller.edit;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.view.GlobalMapObjectPainter;

public class GlobalMapController {
  private Stage stage;

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML Group scrollPaneContent;
  @FXML Pane mainPane;
  // TODO: перенести все флаги в контроллер
  Rectangle currPreFragment;
  GlobalMapObjectPainter painter;
  private int mouseX;
  private int mouseY;
  GlobalOperation operation = GlobalOperation.None;

  public enum GlobalOperation {
    None,
    SetConnector,
    SetFragment
  }

  @FXML
  public void fieldDragDetected() {
    System.out.println("field drag detected");
  }

  @FXML
  public void fieldDragDone() {
    System.out.println("field drag done");
  }

  @FXML
  public void fieldDragDropped() {
    System.out.println("field drag dropped");
  }

  @FXML
  public void fieldClicked() {
    System.out.println("field clicked");
  }

  @FXML
  public void setFragment() {
    System.out.println("set fragment");
    if (operation != GlobalOperation.SetFragment) {
      operation = GlobalOperation.SetFragment;
    } else {
      operation = GlobalOperation.None;
    }
  }

  @FXML
  public void setConnector() {
    System.out.println("set connector");
    if (operation != GlobalOperation.SetConnector) {
      operation = GlobalOperation.SetConnector;
    } else {
      operation = GlobalOperation.None;
    }
  }

  @FXML
  public void initialize() {
    painter = new GlobalMapObjectPainter();

    mainPane.setOnDragDetected(
        event -> {
          fieldDragDetected();
          if (operation == GlobalOperation.SetFragment) {
            mouseX = (int)event.getX();
            mouseY = (int)event.getY();
            currPreFragment = (Rectangle) painter.paintPreFragment(event.getX(), event.getY());
            scrollPaneContent.getChildren().add(currPreFragment);
            stage.show();
            operation = GlobalOperation.None;
          }
        });

    mainPane.setOnMouseMoved(event ->{
      if (currPreFragment != null){
        scrollPaneContent.getChildren().remove(currPreFragment);
        currPreFragment.setX(Math.min(mouseX, event.getX()));
        currPreFragment.setY(Math.min(mouseX, event.getX()));
        currPreFragment.setWidth(Math.abs(mouseX - event.getX()));
        currPreFragment.setHeight(Math.abs(mouseY - event.getY()));
        scrollPaneContent.getChildren().add(currPreFragment);
        stage.show();
      }
    });

    mainPane.setOnMouseClicked(
        event -> {
          fieldClicked();
          if (operation == GlobalOperation.SetFragment) {
            if (currPreFragment != null) {
              // todo: create map, add regions to the map
              RectRegion region =
                  new RectRegion(
                      currPreFragment.getLayoutX(),
                      currPreFragment.getLayoutY(),
                      currPreFragment.getHeight(),
                      currPreFragment.getWidth());
              currPreFragment.setStroke(Color.valueOf("#506070"));
              currPreFragment.setStrokeWidth(10);
              currPreFragment = null;
              stage.show();
            }
          }
        });


  }
}
