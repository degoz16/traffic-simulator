package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;

public interface GlobalMapSelectorInitializerInterface {
  void initialize(GlobalMapUpdateObserver updateObserver);

  GlobalMapSelectorControllerInterface getSelectorControl();
}
