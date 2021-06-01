package ru.nsu.fit.traffic.json.parse;

import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;

import java.util.ArrayList;
import java.util.List;

public class RegionMapJson {
  private List<RegionJson> regions = new ArrayList<>();
  private List<ConnectorJson> connectors = new ArrayList<>();
  private String name;
  private int currConnectorsCnt;

  public RegionMapJson(RegionsMap map) {
    name = map.getName();
    currConnectorsCnt = map.getCurrConnectorsCnt();
    List<RoadConnector> connectorList = new ArrayList<>();
    List<RectRegion> rectRegionList = new ArrayList<>();
    map.foreachRegion(region -> {
      rectRegionList.add(region);
      regions.add(new RegionJson(region));
      region.foreachConnector(connector -> {
        connectorList.add(connector);
        connectors.add(new ConnectorJson(connector.getX(), connector.getY()));
      });
    });
    for (int i = 0; i < connectorList.size(); i++) {
      connectors.get(i).setConnectorLink(
          connectorList.indexOf(connectorList.get(i).getConnectorLink()));
      connectors.get(i).setRegion(
          rectRegionList.indexOf(connectorList.get(i).getRegion()));
    }
    for (int i = 0; i < rectRegionList.size(); i++) {
      int fI = i;
      rectRegionList.get(i).foreachConnector(connector -> {
        regions.get(fI).getConnectorList().add(connectorList.indexOf(connector));
      });
    }
  }

  public RegionsMap getMap() {
    RegionsMap map = new RegionsMap();
    map.setName(name);
    map.setCurrConnectorsCnt(currConnectorsCnt);
    List<RoadConnector> connectorList = new ArrayList<>();
    List<RectRegion> rectRegionList = new ArrayList<>();
    connectors.forEach(connectorJson -> {
      connectorList.add(new RoadConnector(connectorJson.getX(), connectorJson.getY()));
    });
    regions.forEach(regionJson -> {
      RectRegion reg = new RectRegion(
          regionJson.getX(), regionJson.getY(),
          regionJson.getWidth(), regionJson.getHeight());
      reg.setRegionMapLink(regionJson.getRegionMapLink());
      rectRegionList.add(reg);
    });
    for (int i = 0; i < connectors.size(); i++) {
      connectorList.get(i).setRegion(rectRegionList.get(connectors.get(i).getRegion()));
      connectorList.get(i).setConnectorLink(connectorList.get(connectors.get(i).getConnectorLink()));
      rectRegionList.get(connectors.get(i).getRegion()).addConnector(connectorList.get(i));
    }
    rectRegionList.forEach(map::addRegion);
    return map;
  }
}
