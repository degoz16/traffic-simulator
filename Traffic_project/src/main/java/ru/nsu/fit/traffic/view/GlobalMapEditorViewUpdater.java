package ru.nsu.fit.traffic.view;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;

public class GlobalMapEditorViewUpdater {
  private final Pane mainPane;

  public GlobalMapEditorViewUpdater(Pane mainPane) {
    this.mainPane = mainPane;
  }

  /**
   * Метод отрисовки текущего состояния карты
   */
  public void updateMapView(GlobalMapEditOpManager editOperationsManager) {
    Platform.runLater(() -> {
      //TODO pane update
    });
  }
}
