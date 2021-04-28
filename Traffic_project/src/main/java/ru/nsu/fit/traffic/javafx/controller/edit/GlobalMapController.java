package ru.nsu.fit.traffic.javafx.controller.edit;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;
import ru.nsu.fit.traffic.view.GlobalMapObjectPainter;

import java.util.AbstractMap;
import java.util.Map;

public class GlobalMapController {
  @FXML private ScrollPane mainScrollPane;
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
  private Rectangle currPreFragment;
  private GlobalMapObjectPainter painter;
  private Shape currPreConnector;
  private RegionsMap regionsMap = new RegionsMap();
  private int mouseX;
  private int mouseY;
  GlobalOperation operation = GlobalOperation.None;

  public enum GlobalOperation {
    None,
    SetConnector,
    SetFragment,
    DrawFragment
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

    mainPane.setOnMousePressed(
        event -> {
          fieldDragDetected();
          if (operation == GlobalOperation.SetFragment) {
            System.out.println("Mouse pressed");
            mouseX = (int) event.getX();
            mouseY = (int) event.getY();
            currPreFragment = (Rectangle) painter.paintPreFragment(event.getX(), event.getY());
            scrollPaneContent.getChildren().add(currPreFragment);
            stage.show();
            operation = GlobalOperation.DrawFragment;
          }
        });

    mainPane.setOnMouseMoved(
        // TODO: find how to set event onDraggedMouseMove, at this moment this code is a trash.
        event -> {
          if (operation == GlobalOperation.DrawFragment) {
            if (currPreFragment != null) {
              System.out.println("Mouse moved");
              scrollPaneContent.getChildren().remove(currPreFragment);
              currPreFragment.setX(Math.min(mouseX, event.getX()));
              currPreFragment.setY(Math.min(mouseY, event.getY()));
              currPreFragment.setWidth(Math.abs(mouseX - event.getX()));
              currPreFragment.setHeight(Math.abs(mouseY - event.getY()));
              scrollPaneContent.getChildren().add(currPreFragment);
              stage.show();
            }
          }
        });

    mainPane.setOnMouseReleased(
        event -> {
          fieldClicked();
          if (operation == GlobalOperation.DrawFragment) {
            if (currPreFragment != null) {
              System.out.println("Mouse released");
              // todo: create map, add regions to the map
              RectRegion region =
                  new RectRegion(
                      Math.min(mouseX, event.getX()),
                      Math.min(mouseY, event.getY()),
                      Math.abs(mouseX - event.getX()),
                      Math.abs(mouseY - event.getY()));
              regionsMap.addRegion(region);
              currPreFragment = (Rectangle) painter.paintRegion(region).get(0);
              scrollPaneContent.getChildren().add(currPreFragment);
              currPreFragment.setOnMouseMoved(event1 -> {});

              currPreFragment.setOnMouseClicked(
                  event1 -> {
                    System.out.println("Clicked on fragment");
                    drawConnector(event1, region);
                  });

              currPreFragment.setOnMouseMoved(event1 -> {
                onMovedMouseOnRegion(event1, region);
              });
              currPreFragment = null;

              stage.show();
              operation = GlobalOperation.None;
            }
          }
        });
  }

  private void drawConnector(MouseEvent event1, RectRegion region) {
    if (operation == GlobalOperation.SetConnector) {
      if (currPreConnector != null) {
        scrollPaneContent.getChildren().remove(currPreConnector);
      }
      Map.Entry<Double, Double> point = setOnSide(event1, region);
      RoadConnector connector = new RoadConnector(region, point.getKey(), point.getValue());
      currPreConnector = painter.paintConnector(connector, true);
      scrollPaneContent.getChildren().add(currPreConnector);
      region.getConnectorList().add(connector);
      stage.show();
      operation = GlobalOperation.None;
      currPreConnector = null;
    }
  }

  private void onMovedMouseOnRegion(MouseEvent event1, RectRegion region) {
    if (operation == GlobalOperation.SetConnector) {
      if (event1.getX() - region.getX() < 15
          || event1.getX() - region.getX() >= region.getWidth() - 15
          || event1.getY() - region.getY() >= region.getHeight() - 15
          || event1.getY() - region.getY() < 15) {
        if (currPreConnector != null) {
          scrollPaneContent.getChildren().remove(currPreConnector);
        }

        currPreConnector =
            painter.paintConnector(
                new RoadConnector(
                    region, event1.getX() - region.getX(), event1.getY() - region.getY()),
                false);

        currPreConnector.setOnMouseClicked(
            event2 -> {
              drawConnector(event1, region);
            });

        scrollPaneContent.getChildren().add(currPreConnector);
        stage.show();
      }
    } else {
      if (currPreConnector != null) {
        scrollPaneContent.getChildren().remove(currPreConnector);
      }
    }
  }

  private Map.Entry<Double, Double> setOnSide(MouseEvent event1, RectRegion region) {
    double x = 0, y = 0;
    if (event1.getX() - region.getX() < 15) {
      x = 0;
      y = event1.getY() - region.getY();
    } else if (event1.getX() - region.getX() >= region.getWidth() - 15) {
      x = region.getWidth();
      y = event1.getY() - region.getY();
    } else if (event1.getY() - region.getY() >= region.getHeight() - 15) {
      x = event1.getX() - region.getX();
      y = region.getHeight();
    } else {
      x = event1.getX() - region.getX();
      y = 0;
    }
    return new AbstractMap.SimpleEntry<Double, Double>(x, y);
  }
}
