package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;

public interface GlobalMapSelectorControllerInterface {
  String onRegionClick(int id, MouseEventWrapper event);
  void onRegionPressed(MouseEventWrapper event);

  void setRegionMap(RegionsMap map);

  void setRegionMap(String map);
}
