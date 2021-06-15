package ru.nsu.fit.traffic.controller.edit;

import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorControllerInterface;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;
import ru.nsu.fit.traffic.model.map.TrafficMap;

public class GlobalMapSelectorController implements GlobalMapSelectorControllerInterface {
  private GlobalMapUpdateObserver updateObserver = null;
  private GlobalMapEditOpManager editOpManager = null;
  private Connection connection = null;


  @Override
  public String onRegionClick(int id, MouseEventWrapper event) throws Exception {
    event.consume();
    try {
      return connection.getMapFromServer(id, ConnectionConfig.getConnectionConfig().getRoomId());
    } catch (RuntimeException e){
      return null;
    }
  }

  @Override
  public void setRegionMap(RegionsMap map) {
    editOpManager.setCurrRegMap(map);
    updateObserver.update(editOpManager, true);
  }

  public void setRegionMap(String map) {
      editOpManager.setCurrRegMap(GlobalMapEditOpManager.loadRegMap(map));
      updateObserver.update(editOpManager, true);
  }

  @Override
  public void onRegionPressed(MouseEventWrapper event) {

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
