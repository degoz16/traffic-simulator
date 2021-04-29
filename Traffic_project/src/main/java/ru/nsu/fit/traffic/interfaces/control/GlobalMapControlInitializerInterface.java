package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.controller.edit.GlobalMapEditControl;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;

public interface GlobalMapControlInitializerInterface {
  void initialize(GlobalMapUpdateObserver updateObserver);

  GlobalMapEditControl getEditControl();
}
