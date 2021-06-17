package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;
import ru.nsu.fit.traffic.utils.Pair;

public interface GlobalMapSelectorControllerInterface {
  void onRegionClick(int id, MouseEventWrapper event) throws Exception;

  String onMergeMap();

  void setRegionMap(String map);

  double getMapWidth();

  double getMapHeight();

  boolean testRegionsBounds(double x, double y, int id);

  Pair<Double, Double> getSideCoordinates(double x, double y, double regX, double regY, double regW, double regH);

  void onConnectorClicked(int regId, int conId) throws Exception;

  void onSetConnector();

  void onDeleteConnector();

  void onKick();
}
