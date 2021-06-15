package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;

public interface GlobalMapSelectorControllerInterface {
  String onRegionClick(int id, MouseEventWrapper event) throws Exception;

  void onRegionPressed(MouseEventWrapper event);

  String onMergeMap();

  void setRegionMap(RegionsMap map);

  void setRegionMap(String map);
}
