package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.utils.Pair;

public interface GlobalMapEditControlInterface {

  RegionsMap getCurrRegionsMap();

  void onMainPaneReleased(MouseEventWrapper event);

  void onMainPanePressed(MouseEventWrapper event);

  void onMainPaneClicked(MouseEventWrapper event);

  void onMainPaneDrag(MouseEventWrapper event);

  void onRegionClick(int id, MouseEventWrapper event);

  void onRegionMouseMove(int id, MouseEventWrapper event);

  void onSetRegionButton();

  void onSetConnectorButton();

  Pair<Double, Double> getSideCoordinates(
      double x, double y, double regX, double regY, double regW, double regH);
}
