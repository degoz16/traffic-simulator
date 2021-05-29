package ru.nsu.fit.traffic.controller.edit;

import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorControllerInterface;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;

public class GlobalMapSelectorController implements GlobalMapSelectorControllerInterface {
  private GlobalMapUpdateObserver updateObserver = null;
  private GlobalMapEditOpManager editOpManager = null;

  @Override
  public void onRegionClick(int id, MouseEventWrapper event) {
    //TODO: TUT PISAT
  }

  @Override
  public void onRegionPressed(MouseEventWrapper event) {

  }

  public void setEditOpManager(GlobalMapEditOpManager editOpManager) {
    this.editOpManager = editOpManager;
  }

  public void setUpdateObserver(GlobalMapUpdateObserver updateObserver) {
    this.updateObserver = updateObserver;
  }
}
