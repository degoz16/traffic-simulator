package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;

public interface GlobalMapEditControlInterface {
  void onMainPaneReleased(MouseEventWrapper event);

  void onMainPanePressed(MouseEventWrapper event);

  void onMainPaneClicked(MouseEventWrapper event);

  void onMainPaneDrag(MouseEventWrapper event);
}
