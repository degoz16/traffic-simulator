package ru.nsu.fit.traffic.json.parse;

import ru.nsu.fit.traffic.model.globalmap.RegionsMap;

import java.util.List;

public class RegionMapJson {
  private List<RegionJson> regions;
  private List<ConnectorJson> connectors;

  public RegionMapJson(RegionsMap map) {
    map.foreachRegion(region -> {
      region.foreachConnector(connector -> {

      });
    });
  }
}
