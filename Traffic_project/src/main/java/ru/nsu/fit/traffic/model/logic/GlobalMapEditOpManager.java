package ru.nsu.fit.traffic.model.logic;

import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;
import ru.nsu.fit.traffic.model.map.Node;
import ru.nsu.fit.traffic.model.map.PlaceOfInterest;
import ru.nsu.fit.traffic.model.map.Road;
import ru.nsu.fit.traffic.model.map.TrafficMap;
import ru.nsu.fit.traffic.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class GlobalMapEditOpManager {
  private final GlobalMapUpdateObserver updateObserver;
  private RegionsMap currRegMap = new RegionsMap();
  private GlobalMapEditOp currOp = GlobalMapEditOp.NONE;

  public GlobalMapEditOpManager(GlobalMapUpdateObserver updateObserver) {
    this.updateObserver = updateObserver;
  }

  public void clearMap() {
    currRegMap = new RegionsMap();
    updateObserver.update(this);
  }

  public Pair<TrafficMap, Pair<Double, Double>> loadRegion(RectRegion region) {
    //TODO: загрузка карт + позиция региона
    return null;
  }

  //TODO: Test this shit!
  public TrafficMap mergeGlobalMap(double scale) {
    List<Pair<TrafficMap, Pair<Double, Double>>> maps = new ArrayList<>();
    currRegMap.foreachRegion(region -> maps.add(loadRegion(region)));
    List<Pair<Node, Node>> connectorPairs = new ArrayList<>();
    List<Node> nodes = new ArrayList<>();
    List<Road> roads = new ArrayList<>();
    List<PlaceOfInterest> pois = new ArrayList<>();
    for (int i = 0; i < currRegMap.getCurrConnectorsCnt(); i++) {
      connectorPairs.add(new Pair<>());
    }
    maps.forEach(map -> {
      map.getFirst().forEachNode(node -> {
        nodes.add(node);
        node.setX(scale * (map.getSecond().getFirst() + node.getX()));
        node.setY(scale * (map.getSecond().getSecond() + node.getY()));
        if (node.getConnector() != null) {
          int connectorId =
              currRegMap.getRegion(
                  node.getConnector().getRegionId())
                  .getConnector(node.getConnector().getConnectorId()).getId();
          if (connectorPairs.get(connectorId).getFirst() == null) {
            connectorPairs.get(connectorId).setFirst(node);
          } else {
            connectorPairs.get(connectorId).setSecond(node);
          }
        }
      });
      map.getFirst().forEachRoad(roads::add);
      map.getFirst().forEachPlaceOfInterest(pois::add);
    });
    connectorPairs.forEach(pair -> {
      pair.getSecond().getRoadsOut().forEach(road -> {
        road.setFrom(pair.getFirst());
        pair.getFirst().addRoadOut(road);
      });
      pair.getSecond().getRoadsIn().forEach(road -> {
        road.setTo(pair.getFirst());
        pair.getFirst().addRoadIn(road);
      });
    });

    TrafficMap map = new TrafficMap();
    map.setNodes(nodes);
    map.setRoads(roads);
    map.setPlacesOfInterest(pois);
    return map;
  }

  public void setCurrOp(GlobalMapEditOp currOp) {
    this.currOp = currOp;
  }

  public RegionsMap getCurrRegMap() {
    return currRegMap;
  }

  public void setCurrRegMap(RegionsMap currRegMap) {
    this.currRegMap = currRegMap;
  }

  public GlobalMapEditOp getCurrentOp() {
    return currOp;
  }

  public void addReg(String name, double x1, double y1, double x2, double y2) {
    double xMin = Math.min(x1, x2);
    double xMax = Math.max(x1, x2);
    double yMin = Math.min(y1, y2);
    double yMax = Math.max(y1, y2);
    RectRegion region = new RectRegion(name, xMin, yMin, xMax - xMin, yMax - yMin);
    currRegMap.addRegion(region);
    updateObserver.update(this);
  }

  public void addConnector(RectRegion region1, RectRegion region2, double x, double y) {
    int id = currRegMap.getNextConnectorId();
    RoadConnector connector1 = new RoadConnector(id, x - region1.getX(), y - region1.getY(), region1);
    RoadConnector connector2 = new RoadConnector(id, x - region2.getX(), y - region2.getY(), region2);
    connector1.setConnectorLink(connector2);
    connector2.setConnectorLink(connector1);
    region1.addConnector(connector1);
    region2.addConnector(connector2);

    updateObserver.update(this);
  }
}
