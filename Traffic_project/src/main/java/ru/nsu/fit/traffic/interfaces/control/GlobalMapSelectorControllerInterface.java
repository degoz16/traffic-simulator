package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;

public interface GlobalMapSelectorControllerInterface {
  void onRegionClick(int id, MouseEventWrapper event);

  void onRegionPressed(MouseEventWrapper event);
}
