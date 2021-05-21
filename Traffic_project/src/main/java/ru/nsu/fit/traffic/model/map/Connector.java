package ru.nsu.fit.traffic.model.map;

public class Connector {
  private Node node;
  private int regionId;
  private int connectorId;

  public Connector(int id, double x, double y) {
    node = new Node(x, y);
    connectorId = id;
  }

  public int getRegionId() {
    return regionId;
  }

  public void setRegionId(int regionId) {
    this.regionId = regionId;
  }

  public Node getNode() {
    return node;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public int getConnectorId() {
    return connectorId;
  }

  public void setConnectorId(int connectorId) {
    this.connectorId = connectorId;
  }
}
