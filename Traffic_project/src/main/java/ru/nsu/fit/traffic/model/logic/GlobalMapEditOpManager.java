package ru.nsu.fit.traffic.model.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.nsu.fit.traffic.json.parse.RegionMapJson;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;
import ru.nsu.fit.traffic.model.map.Node;
import ru.nsu.fit.traffic.model.map.PlaceOfInterest;
import ru.nsu.fit.traffic.model.map.Road;
import ru.nsu.fit.traffic.model.map.TrafficMap;
import ru.nsu.fit.traffic.utils.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GlobalMapEditOpManager {
  private static final int MIN_REGION_SIZE = 40;
  private final GlobalMapUpdateObserver updateObserver;
  private RegionsMap currRegMap = new RegionsMap();
  private GlobalMapEditOp currOp = GlobalMapEditOp.NONE;

  public GlobalMapEditOpManager(GlobalMapUpdateObserver updateObserver) {
    this.updateObserver = updateObserver;
  }

  public Pair<Double, Double> getSideCoordinates(double x, double y, RectRegion region) {
    return getSideCoordinates(x, y, region.getX(), region.getY(), region.getWidth(), region.getHeight());
  }

  public Pair<Double, Double> getSideCoordinates(
      double x, double y, double regX, double regY, double regW, double regH) {
    boolean d1 = y > (regH / regW) * (x - regX) + regY;
    boolean d2 = y > -(regH / regW) * (x - regX - regW) + regY;
    if (d1 && d2) {
      return new Pair<>(x, regY + regH);
    }
    else if(d1) {
      return new Pair<>(regX, y);
    }
    else if (d2) {
      return new Pair<>(regX + regW, y);
    }
    else {
      return new Pair<>(x, regY);
    }
  }

  public void clearMap() {
    currRegMap = new RegionsMap();
    updateObserver.update(this, false);
  }

  public static void saveRegMap(String path, RegionsMap regionsMap) {
    try {
      Writer writer = new FileWriter(path);;
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String jsonMap = gson.toJson(new RegionMapJson(regionsMap));
      writer.write(jsonMap);
      writer.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  public void saveRegMap(String path) {
    saveRegMap(path, getCurrRegMap());
  }

  public static RegionsMap loadRegMap(String path) {
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      Reader fileReader = new FileReader(path);
      RegionMapJson map = gson.fromJson(fileReader, RegionMapJson.class);
      return map.getMap();
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    }
    return null;
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
        node.setX(scale * map.getSecond().getFirst() + node.getX());
        node.setY(scale * map.getSecond().getSecond() + node.getY());
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

    if (xMax - xMin < MIN_REGION_SIZE
        || yMax - yMin < MIN_REGION_SIZE) {
      return;
    }
    final double exp = 3;
    double expXmin = xMin - exp;
    double expXmax = xMax + exp;
    double expYmin = yMin - exp;
    double expYmax = yMax + exp;

    if (currRegMap.getRegions().stream().noneMatch(region -> {
      double regXmin = region.getX();
      double regXmax = region.getX() + region.getWidth();
      double regYmin = region.getY();
      double regYmax = region.getY() + region.getHeight();
      //Guano, no mne len` raznosit` eto
      return ((expXmin > regXmin && expXmin < regXmax) || (expXmax > regXmin && expXmax < regXmax)) &&
          ((expYmin > regYmin && expYmin < regYmax) || (expYmax > regYmin && expYmax < regYmax)) ||
          ((regXmin > expXmin && regXmin < expXmax) || (regXmax > expXmin && regXmax < expXmax)) &&
              ((regYmin > expYmin && regYmin < expYmax) || (regYmax > expYmin && regYmax < expYmax));
    }) && currRegMap.getRegionCount() > 0) {
      return;
    }
    RectRegion region = new RectRegion(name, xMin, yMin, xMax - xMin, yMax - yMin);

    currRegMap.addRegion(region);
    updateObserver.update(this, false);
  }

  public void addConnector(RectRegion region, double x, double y) {
    Pair<Double, Double> coords = getSideCoordinates(x, y, region);
    boolean vert = Math.abs(coords.getSecond() - region.getY()) < 0.001
        || Math.abs(coords.getSecond() - region.getY() - region.getHeight()) < 0.001;
    Pair<RectRegion, RectRegion> regions =
        getCurrRegMap()
            .getRegionsInThePoint(coords.getFirst(), coords.getSecond(), vert);

    if(regions != null) {
      RectRegion region1 = regions.getFirst();
      RectRegion region2 = regions.getSecond();
      int id = currRegMap.getNextConnectorId();
      double x1 = coords.getFirst();
      double x2 = coords.getFirst();
      double y1 = coords.getSecond();
      double y2 = coords.getSecond();
      if (vert) {
        if (region1.getY() < region2.getY()) {
          y1 -= 3;
          y2 += 3;
        } else {
          y1 += 3;
          y2 -= 3;
        }
      } else {
        if (region1.getX() < region2.getX()) {
          x1 -= 3;
          x2 += 3;
        } else {
          x1 += 3;
          x2 -= 3;
        }
      }
      Pair<Double, Double> coords1 = getSideCoordinates(x1, y1, region1);
      Pair<Double, Double> coords2 = getSideCoordinates(x2, y2, region2);
      x1 = coords1.getFirst();
      x2 = coords2.getFirst();
      y1 = coords1.getSecond();
      y2 = coords2.getSecond();

      RoadConnector connector1 = new RoadConnector(id, x1 - region1.getX(), y1 - region1.getY(), region1);
      RoadConnector connector2 = new RoadConnector(id, x2 - region2.getX(), y2 - region2.getY(), region2);
      connector1.setConnectorLink(connector2);
      connector2.setConnectorLink(connector1);
      region1.addConnector(connector1);
      region2.addConnector(connector2);
    }

    updateObserver.update(this, false);
  }
}
