package ru.nsu.fit.traffic.controller;

public interface GlobalMapSceneElementsControl {
  void setSelectRectVisible(boolean visible);
  void setConnectorIconVisible(boolean visible);
  void setCurrentOperation(String currentOperation);
  void redrawConnectorIcon();
}
