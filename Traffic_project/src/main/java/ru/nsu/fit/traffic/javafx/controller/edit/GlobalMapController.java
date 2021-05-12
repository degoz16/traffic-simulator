package ru.nsu.fit.traffic.javafx.controller.edit;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.controller.GlobalMapEditControlInitializer;
import ru.nsu.fit.traffic.controller.GlobalMapSceneElementsControl;
import ru.nsu.fit.traffic.controller.edit.GlobalMapEditControl;
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
  @FXML private AnchorPane basePane;
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

  public Rectangle getSelectRect() {
    return selectRect;
  }

  public Circle getConnectorIcon() {
    return connectorIcon;
  }

  private void addSelectRect(double x, double y) {
    UiPainter.addSelectRect(x, y, selectRect, mainPane);
  }

  private void resizeSelectRect(double x, double y) {
    UiPainter.resizeSelectRect(x, y, selectRect);
  }

  private void removeConnectorIcon() {
    UiPainter.removeConnectorIcon(connectorIcon, basePane);
  }

  private void addConnectorIcon(double x, double y) {
    UiPainter.addConnectorIcon(x, y, connectorIcon, basePane);
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
  public void onClear() {
    mainPane.getChildren().clear();
    editControl.onClear();
  }

  @FXML
  public void onGet() {
    editControl.onGet();
  }

  @FXML
  public void onPut() {
    editControl.onPut();
  }

  @FXML
  public void initialize() {
    painter = new GlobalMapObjectPainter();
    GlobalMapControlInitializerInterface initializer =
        new GlobalMapEditControlInitializer(sceneElementsControl);
    editControl = initializer.getEditControl();
    addConnectorIcon(0, 0);
    connectorIcon.setVisible(false);
    connectorIcon.setMouseTransparent(true);
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
                      if (editControl.testRegionsBounds(event.getX(), event.getY(), id)) {
                        connectorIcon.setVisible(true);
                        rePosConnectorIcon(coords.getFirst(), coords.getSecond());
                      } else {
                        connectorIcon.setVisible(false);
                      }
                    }
                  });
              rect.setOnMouseEntered(
                  event -> {
                    Pair<Double, Double> coords =
                        editControl.getSideCoordinates(
                            event.getX(), event.getY(), rect.getX(), rect.getY(), regW, regH);
                  });
              rect.setOnMouseClicked(event -> {
                editControl.onRegionClick(id, MouseEventWrapper.getMouseEventWrapper(event));
              });
              rect.setOnMousePressed(event ->
                  editControl.onRegionPressed(MouseEventWrapper.getMouseEventWrapper(event)));
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
          editControl.onMainPaneReleased(MouseEventWrapper.getMouseEventWrapper(event));
          removeSelectRect();
          selectRect.setHeight(0);
          selectRect.setWidth(0);
          removeConnectorIcon();
          connectorIcon.setVisible(false);
          addConnectorIcon(10, 10);
        });

    mainPane.setOnMouseDragged(
        event -> {
          editControl.onMainPaneDrag(MouseEventWrapper.getMouseEventWrapper(event));
          if (isSelRectVisible && event.getButton() == MouseButton.PRIMARY) {
            resizeSelectRect(editControl.getCurrX(), editControl.getCurrY());
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
