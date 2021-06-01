package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.model.map.Node;
import ru.nsu.fit.traffic.model.map.PlaceOfInterest;
import ru.nsu.fit.traffic.model.map.Road;

public interface EditControlInterface {
  void startSimulation();

  void stopSimulation();

  void rewindForward();

  void rewindBack();

  void roadButtonClicked();

  void startTimePicker(String time);

  void trafficLightButtonClicked();

  void buildingButtonClicked();

  void playbackClicked();

  void reportClicked();

  void editClicked();

  void roadSignButtonClicked();

  void showStatistic();

  void setSpeedSign(int speed);

  void setMainRoad();

  void onLeftLanesTextFieldChange(String newValue);

  void onRightLanesTextFieldChange(String newValue);

  void onTimeLineSliderChange(Number newValue);

  void onPlaybackLineSliderChange(Number newVal);

  void onBasePaneClickedFilter(
      MouseEventWrapper event,
      double basePaneWidth,
      double basePaneHeight,
      double windowWidth,
      double windowHeight);

  void onMainPaneReleased(MouseEventWrapper event);

  void onMainPanePressed(MouseEventWrapper event);

  void onPoiClicked(int id, MouseEventWrapper event);

  void onMainPaneClicked(MouseEventWrapper event);

  void onNodeClick(int id, MouseEventWrapper event);

  void onMainPaneDrag(MouseEventWrapper event);

  void onRoadClick(int id, int i, MouseEventWrapper event);

  void saveMap();
}
