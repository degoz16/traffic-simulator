package ru.nsu.fit.traffic.controller;

import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.controller.edit.GlobalMapSelectorController;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorControllerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorInitializerInterface;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;

public class GlobalMapSelectorInitializer implements GlobalMapSelectorInitializerInterface {
  private final GlobalMapSelectorController selectorController;
  private GlobalMapEditOpManager editOpManager = null;

  public GlobalMapSelectorInitializer() {
    selectorController = new GlobalMapSelectorController();
  }

  @Override
  public void initialize(GlobalMapUpdateObserver updateObserver) {
    GlobalMapEditOpManager editOperationsManager = new GlobalMapEditOpManager(updateObserver);

    selectorController.setConnection(ConnectionConfig.getConnectionConfig().getConnection());
    selectorController.setUpdateObserver(updateObserver);
    selectorController.setEditOpManager(editOperationsManager);
  }

  @Override
  public GlobalMapSelectorControllerInterface getSelectorControl() {
    return selectorController;
  }
}
