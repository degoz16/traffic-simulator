package ru.nsu.fit.traffic.controller.edit;

import ru.nsu.fit.traffic.controller.SceneElementsControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapEditControlInterface;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOp;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.UpdateObserver;

import static ru.nsu.fit.traffic.event.wrappers.MouseEventWrapperButton.*;

public class GlobalMapEditControl implements GlobalMapEditControlInterface {
  private UpdateObserver update;
  //private SceneElementsControl sceneElementsControl;
  private GlobalMapEditOpManager editOpManager;
  private double lastClickX = 0;
  private double lastClickY = 0;

  @Override
  public void onMainPaneReleased(MouseEventWrapper event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        switch (editOpManager.getCurrentOp()) {
          case REGION_SELECT -> {
            editOpManager.addReg("", lastClickX, lastClickY, event.getX(), event.getY());
          }
        }
      }
    }
  }

  @Override
  public void onMainPanePressed(MouseEventWrapper event) {
    switch (event.getButton()) {
      case PRIMARY -> {
        lastClickX = event.getX();
        lastClickY = event.getY();
        switch (editOpManager.getCurrentOp()) {
          case REGION_SELECT -> {
            //selectRect.setX(event.getX());
            //selectRect.setY(event.getY());
            //mainPane.getChildren().add(selectRect);
            //sceneElementsControl.addSelectRect(event.getX(), event.getY());
          }
        }
      }
    }
  }

  @Override
  public void onMainPaneClicked(MouseEventWrapper event) {

  }

  @Override
  public void onMainPaneDrag(MouseEventWrapper event) {

  }

  private void stopOperation() {
    editOpManager.setCurrOp(GlobalMapEditOp.NONE);
  }
}
