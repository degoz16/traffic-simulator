package ru.nsu.fit.traffic.controller.edit;

import static ru.nsu.fit.traffic.model.logic.GlobalMapEditOp.DELETE_CONNECTOR;
import static ru.nsu.fit.traffic.model.logic.GlobalMapEditOp.NONE;
import static ru.nsu.fit.traffic.model.logic.GlobalMapEditOp.SET_CONNECTOR;

import java.util.List;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorControllerInterface;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;

public class GlobalMapSelectorController implements GlobalMapSelectorControllerInterface {
  private GlobalMapUpdateObserver updateObserver = null;
  private GlobalMapEditOpManager editOpManager = null;
  private Connection connection = null;


  @Override
  public String onRegionClick(int id, MouseEventWrapper event) throws Exception {
    if (editOpManager.getCurrentOp() == NONE) {
      event.consume();
      try {
        return connection.getMapFromServer(
          id,
          ConnectionConfig.getConnectionConfig().getRoomId(),
          true
        );
      } catch (RuntimeException e) {
        return null;
      }
    } else if (editOpManager.getCurrentOp() == SET_CONNECTOR){
      editOpManager.addConnector(editOpManager.getCurrRegMap().getRegion(id), event.getX(), event.getY());
    }
    return null;
  }

  @Override
  public void setRegionMap(RegionsMap map) {
    editOpManager.setCurrRegMap(map);
    updateObserver.update(editOpManager, true);
  }

  @Override
  public void setRegionMap(String map) {
    editOpManager.setCurrRegMap(GlobalMapEditOpManager.loadRegMap(map));
    updateObserver.update(editOpManager, true);
  }

  @Override
  public double getMapWidth() {
    return editOpManager.getCurrRegMap().getWidth();
  }

  @Override
  public double getMapHeight() {
    return editOpManager.getCurrRegMap().getHeight();
  }

  @Override
  public void onRegionPressed(MouseEventWrapper event) {

  }

  @Override
  public void onConnectorClicked(RoadConnector connector){
    if (editOpManager.getCurrentOp() == DELETE_CONNECTOR){
      List<RectRegion> regions = editOpManager.getCurrRegMap().getRegions();
      RectRegion currRegion = regions.get(regions.indexOf(connector.getRegion()));
      currRegion.deleteConnector(connector);
      connector.deleteLink();
      updateObserver.update(editOpManager, false);
    }
  }

  @Override
  public String onMergeMap() {
    return editOpManager.mergeGlobalMap();
  }

  public void setEditOpManager(GlobalMapEditOpManager editOpManager) {
    this.editOpManager = editOpManager;
  }

  public void setUpdateObserver(GlobalMapUpdateObserver updateObserver) {
    this.updateObserver = updateObserver;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }


}
