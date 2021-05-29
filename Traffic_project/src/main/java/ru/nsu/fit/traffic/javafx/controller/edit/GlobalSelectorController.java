package ru.nsu.fit.traffic.javafx.controller.edit;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.controller.GlobalMapEditControlInitializer;
import ru.nsu.fit.traffic.controller.GlobalMapSelectorInitializer;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapControlInitializerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapEditControlInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorControllerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorInitializerInterface;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;
import ru.nsu.fit.traffic.utils.Pair;
import ru.nsu.fit.traffic.view.GlobalMapEditorViewUpdater;
import ru.nsu.fit.traffic.view.GlobalMapObjectPainter;

public class GlobalSelectorController {
  private Stage stage;
  @FXML private ScrollPane mainScrollPane;
  @FXML private Group scrollPaneContent;
  @FXML private Pane mainPane;
  @FXML private AnchorPane basePane;
  @FXML private VBox centeredField;
  private GlobalMapSelectorControllerInterface selectorControl;
  private GlobalMapEditOpManager editOpManager = null;
  private GlobalMapUpdateObserver updateObserver = null;

  @FXML
  public void initialize() {
    GlobalMapObjectPainter painter = new GlobalMapObjectPainter();
    painter = new GlobalMapObjectPainter();
    GlobalMapSelectorInitializerInterface initializer =
        new GlobalMapSelectorInitializer();
    selectorControl = initializer.getSelectorControl();

    GlobalMapEditorViewUpdater viewUpdater =
        new GlobalMapEditorViewUpdater(
            ((rect, id, regW, regH) -> {
              rect.setOnMouseClicked(event -> {
                selectorControl.onRegionClick(id, MouseEventWrapper.getMouseEventWrapper(event));
              });
            }),
            mainPane);
    initializer.initialize(viewUpdater::updateMapView);
  }
}
