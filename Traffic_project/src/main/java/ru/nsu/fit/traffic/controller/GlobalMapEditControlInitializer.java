package ru.nsu.fit.traffic.controller;

import ru.nsu.fit.traffic.controller.edit.GlobalMapEditControl;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapControlInitializerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapEditControlInterface;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;

public class GlobalMapEditControlInitializer implements GlobalMapControlInitializerInterface {
  private final GlobalMapEditControl editControl;

  public GlobalMapEditControlInitializer(GlobalMapSceneElementsControl sceneElementsControl) {
    editControl = new GlobalMapEditControl(sceneElementsControl);
  }

  @Override
  public void initialize(GlobalMapUpdateObserver updateObserver) {
    GlobalMapEditOpManager editOperationsManager = new GlobalMapEditOpManager(updateObserver);

    editControl.setUpdateObserver(updateObserver);
    editControl.setEditOpManager(editOperationsManager);
  }

  @Override
  public GlobalMapEditControl getEditControl() {
    return editControl;
  }
}
