package ru.nsu.fit.traffic.json.parse;

public class RegionConnectorJson {
  private int regionId;
  private int connectorId;

  public RegionConnectorJson(int regionId, int connectorId) {
    this.regionId = regionId;
    this.connectorId = connectorId;
  }

  public int getRegionId() {
    return regionId;
  }

  public void setRegionId(int regionId) {
    this.regionId = regionId;
  }

  public int getConnectorId() {
    return connectorId;
  }

  public void setConnectorId(int connectorId) {
    this.connectorId = connectorId;
  }
}
