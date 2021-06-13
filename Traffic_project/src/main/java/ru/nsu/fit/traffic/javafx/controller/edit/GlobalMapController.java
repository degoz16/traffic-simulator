package ru.nsu.fit.traffic.javafx.controller.edit;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.App;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.GlobalMapEditControlInitializer;
import ru.nsu.fit.traffic.controller.GlobalMapSceneElementsControl;
import ru.nsu.fit.traffic.controller.edit.GlobalMapEditControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapControlInitializerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapEditControlInterface;
import ru.nsu.fit.traffic.javafx.paiters.UiPainter;
import ru.nsu.fit.traffic.utils.Pair;
import ru.nsu.fit.traffic.view.GlobalMapEditorViewUpdater;

public class GlobalMapController {
  private final Rectangle selectRect = UiPainter.getSelectRect();
  private final Circle connectorIcon = UiPainter.getConnectorIcon();
  private Stage stage;
  @FXML private ScrollPane mainScrollPane;
  @FXML private Pane mainPane;
  @FXML private AnchorPane basePane;
  @FXML private VBox centeredField;
  @FXML private Text fragmentHeightText;
  @FXML private Text fragmentWightText;
  @FXML private AnchorPane fragmentParamsPane;
  @FXML private Text currentOperation;
  private GlobalMapEditControlInterface editControl;
  private boolean isSelRectVisible = false;
  private boolean isConnectorIconVisible = false;

  public void setMapParams(double wight, double height, String name) {
    mainPane.setPrefWidth(wight);
    mainPane.setMaxWidth(wight);
    mainPane.setPrefHeight(height);
    mainPane.setMaxHeight(height);

    centeredField.setPrefWidth(wight);
    centeredField.setMaxWidth(wight);
    centeredField.setPrefHeight(height);
    centeredField.setMaxHeight(height);

    editControl.getCurrRegionsMap().setName(name);
  }

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

        @Override
        public void setCurrentOperation(String currOperation) {
          currentOperation.setText(currOperation);
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

  private Rectangle resizeSelectRect(double x, double y) {
    UiPainter.resizeSelectRect(x, y, selectRect);
    return selectRect;
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
  public void deleteFragment() {
    editControl.deleteRegion();
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
    editControl.onNewGet();
    // editControl.onGet();
  }

  @FXML
  public void deleteConnector() {
    editControl.deleteConnector();
  }

  @FXML
  public void saveMap() {
    Integer roomId = editControl.onNewPut();
    ConnectionConfig.getConnectionConfig().setRoomId(roomId);
    FXMLLoader loader = new FXMLLoader(App.class.getResource("view/GlobalMapSelectorView.fxml"));
    try {
      Parent root = loader.load();
      Scene scene = new Scene(root);

      stage.setTitle("Traffic simulator");
      stage.setScene(scene);

      GlobalSelectorController controller = loader.getController();
      controller.setStage(stage);
      controller.setMap(
          ConnectionConfig.getConnectionConfig().getConnection().getGlobalMapFromServer(roomId));

      stage.show();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public GlobalMapEditControlInterface getEditControl() {
    return editControl;
  }

  @FXML
  public void initialize() {
    GlobalMapControlInitializerInterface initializer =
        new GlobalMapEditControlInitializer(sceneElementsControl);
    editControl = initializer.getEditControl();
    addConnectorIcon(0, 0);
    connectorIcon.setVisible(false);
    connectorIcon.setMouseTransparent(true);
    fragmentParamsPane.setVisible(false);
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
              rect.setOnMouseClicked(
                  event -> {
                    editControl.onRegionClick(id, MouseEventWrapper.getMouseEventWrapper(event));
                  });
              rect.setOnMousePressed(
                  event ->
                      editControl.onRegionPressed(MouseEventWrapper.getMouseEventWrapper(event)));
            }),
            (connector, connectorShape) -> {
              connectorShape.setOnMouseClicked(
                  event -> {
                    editControl.onConnectorClicked(connector);
                  });
            },
            mainPane);
    initializer.initialize(viewUpdater::updateMapView);

    mainPane.setOnMousePressed(
        event -> {
          editControl.onMainPanePressed(MouseEventWrapper.getMouseEventWrapper(event));
          if (isSelRectVisible && event.getButton() == MouseButton.PRIMARY) {
            fragmentParamsPane.setVisible(true);
            fragmentHeightText.setText(String.valueOf(0));
            fragmentWightText.setText(String.valueOf(0));
            addSelectRect(event.getX(), event.getY());
          }
        });

    mainPane.setOnMouseReleased(
        event -> {
          editControl.onMainPaneReleased(MouseEventWrapper.getMouseEventWrapper(event));
          removeSelectRect();
          fragmentParamsPane.setVisible(false);
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
            Rectangle rect = resizeSelectRect(editControl.getCurrX(), editControl.getCurrY());
            fragmentParamsPane.setVisible(true);
            fragmentHeightText.setText(
                String.valueOf((int) (rect.getHeight() / 3.3 * GlobalMapEditControl.MAP_SCALE)));
            fragmentWightText.setText(
                String.valueOf((int) (rect.getWidth() / 3.3 * GlobalMapEditControl.MAP_SCALE)));
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
