package ru.nsu.fit.traffic.javafx.controller.edit;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.controller.GlobalMapEditControlInitializer;
import ru.nsu.fit.traffic.controller.GlobalMapSceneElementsControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapControlInitializerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapEditControlInterface;
import ru.nsu.fit.traffic.javafx.paiters.UiPainter;
import ru.nsu.fit.traffic.utils.Pair;
import ru.nsu.fit.traffic.view.GlobalMapEditorViewUpdater;
import ru.nsu.fit.traffic.view.GlobalMapObjectPainter;

public class GlobalMapController {
  private final Rectangle selectRect = UiPainter.getSelectRect();
  private final Circle connectorIcon = UiPainter.getConnectorIcon();
  private Stage stage;
  @FXML private ScrollPane mainScrollPane;
  @FXML private Group scrollPaneContent;
  @FXML private Pane mainPane;
  private GlobalMapEditControlInterface editControl;
  private GlobalMapObjectPainter painter;
  private boolean isSelRectVisible = false;
  private boolean isConnectorIconVisible = false;
  private final GlobalMapSceneElementsControl sceneElementsControl =
      new GlobalMapSceneElementsControl() {
        @Override
        public void setSelectRectVisible(boolean visible) {
          isSelRectVisible = visible;
        }

        @Override
        public void setConnectorIconVisible(boolean visible) {
          isConnectorIconVisible = visible;
        }
      };

  private void removeSelectRect() {
    UiPainter.removeSelectRect(selectRect, mainPane);
  }

  private void addSelectRect(double x, double y) {
    UiPainter.addSelectRect(x, y, selectRect, mainPane);
  }

  private void resizeSelectRect(double x, double y) {
    UiPainter.checkResizeSelectedRect(x, y, selectRect, editControl.getCurrRegionsMap());
  }

  private void removeConnectorIcon() {
    UiPainter.removeConnectorIcon(connectorIcon, mainPane);
  }

  private void addConnectorIcon(double x, double y) {
    UiPainter.addConnectorIcon(x, y, connectorIcon, mainPane);
  }

  private void rePosConnectorIcon(double x, double y) {
    UiPainter.rePosConnectorIcon(x, y, connectorIcon);
  }

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  public void setFragment() {
    editControl.onSetRegionButton();
  }

  @FXML
  public void setConnector() {
    editControl.onSetConnectorButton();
  }

  @FXML
  public void initialize() {
    painter = new GlobalMapObjectPainter();
    GlobalMapControlInitializerInterface initializer =
        new GlobalMapEditControlInitializer(sceneElementsControl);
    editControl = initializer.getEditControl();
    GlobalMapEditorViewUpdater viewUpdater =
        new GlobalMapEditorViewUpdater(
            ((rect, id, regW, regH) -> {
              rect.setOnMouseMoved(
                  event -> {
                    editControl.onRegionMouseMove(
                        id, MouseEventWrapper.getMouseEventWrapper(event));
                    Pair<Double, Double> coords =
                        editControl.getSideCoordinates(
                            event.getX(), event.getY(), rect.getX(), rect.getY(), regW, regH);
                    if (isConnectorIconVisible) {
                      rePosConnectorIcon(coords.getFirst(), coords.getSecond());
                    }
                  });
              rect.setOnMouseEntered(
                  event -> {
                    Pair<Double, Double> coords =
                        editControl.getSideCoordinates(
                            event.getX(), event.getY(), rect.getX(), rect.getY(), regW, regH);
                    if (isConnectorIconVisible) {
                      addConnectorIcon(coords.getFirst(), coords.getSecond());
                    }
                  });
              rect.setOnMouseExited(event -> removeConnectorIcon());
              rect.setOnMouseClicked(
                  event -> {
                    editControl.onRegionClick(id, MouseEventWrapper.getMouseEventWrapper(event));
                  });
            }),
            mainPane);
    initializer.initialize(viewUpdater::updateMapView);

    mainPane.setOnMousePressed(
        event -> {
          editControl.onMainPanePressed(MouseEventWrapper.getMouseEventWrapper(event));
          if (isSelRectVisible && event.getButton() == MouseButton.PRIMARY) {
            addSelectRect(event.getX(), event.getY());
          }
        });

    mainPane.setOnMouseReleased(
        event -> {
          if (selectRect.getStroke() != UiPainter.incorrectFragment) {
            editControl.onMainPaneReleased(MouseEventWrapper.getMouseEventWrapper(event));
          }
          removeSelectRect();
        });

    mainPane.setOnMouseDragged(
        event -> {
          editControl.onMainPaneDrag(MouseEventWrapper.getMouseEventWrapper(event));
          if (isSelRectVisible && event.getButton() == MouseButton.PRIMARY) {
            resizeSelectRect(event.getX(), event.getY());
          }
        });

    mainPane.setOnMouseClicked(
        event -> {
          editControl.onMainPaneClicked(MouseEventWrapper.getMouseEventWrapper(event));
        });

    mainScrollPane.setPannable(false);
    mainScrollPane.setOnMousePressed(
        event -> {
          if (event.getButton() == MouseButton.MIDDLE) {
            mainScrollPane.setPannable(true);
          }
        });
    mainScrollPane.setOnMouseReleased(
        event -> {
          if (event.getButton() == MouseButton.MIDDLE) {
            mainScrollPane.setPannable(false);
          }
        });
  }
}
