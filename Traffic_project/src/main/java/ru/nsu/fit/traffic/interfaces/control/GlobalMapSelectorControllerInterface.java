package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;

public interface GlobalMapSelectorControllerInterface {
  void onRegionClick(int id, MouseEventWrapper event);
  void onRegionPressed(MouseEventWrapper event);
}
