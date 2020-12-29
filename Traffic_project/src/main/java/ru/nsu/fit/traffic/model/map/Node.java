package ru.nsu.fit.traffic.model.map;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Node {
  private final double x;
  private final double y;

  private List<Spawner> spawners = null;
  private TrafficLight trafficLight = null;
  private PlaceOfInterest placeOfInterest = null;

  // На одинаковых индексах лежат противоположно направленные дороги
  private List<Road> roadsOut = new ArrayList<>();
  private List<Road> roadsIn = new ArrayList<>();

  public Node(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public List<Spawner> getSpawners() {
    return spawners;
  }

  public void setSpawners(List<Spawner> spawners) {
    this.spawners = spawners;
  }

  public PlaceOfInterest getPlaceOfInterest() {
    return placeOfInterest;
  }

  public void setPlaceOfInterest(PlaceOfInterest placeOfInterest) {
    this.placeOfInterest = placeOfInterest;
  }

  public TrafficLight getTrafficLight() {
    return trafficLight;
  }

  public void setTrafficLight(TrafficLight trafficLight) {
    this.trafficLight = trafficLight;
  }

  public void foreachRoadOut(Consumer<Road> f) {
    roadsOut.forEach(f);
  }

  public Stream<Road> getRoadOutStream() {
    return roadsOut.stream();
  }

  public void foreachRoadIn(Consumer<Road> f) {
    roadsIn.forEach(f);
  }

  public Stream<Road> getRoadInStream() {
    return roadsIn.stream();
  }

  public void addRoadOut(Road road) {
    roadsOut.add(road);
  }

  public void addRoadIn(Road road) {
    roadsIn.add(road);
  }

  public void removeRoadOut(Road road) {
    roadsOut.remove(road);
  }

  public List<Road> getRoadsOut() {
    return roadsOut;
  }

  public List<Road> getRoadsIn() {
    return roadsIn;
  }


  public List<Map.Entry<Road, Road>> getRoadPair(){
    List<Map.Entry<Road, Road>> roads = new ArrayList<>();
    for (Road r: roadsOut){
      roads.add(new AbstractMap.SimpleEntry<Road, Road>(r, r.getBackRoad()));
    }
    for (Road r: roadsIn){
      boolean inList = false;
      for (Map.Entry entry: roads){
        if (entry.getValue() == r){
          inList = true;
          break;
        }
      }
      if (inList){
        continue;
      }
      roads.add(new AbstractMap.SimpleEntry<>(null, r));
    }
    return roads;
  }


  public void removeFromPlaceOfInterest() {
    if (placeOfInterest != null) {
      placeOfInterest.removeNode(this);
    }
    placeOfInterest = null;
  }

  public void removeRoadIn(Road road) {
    roadsIn.remove(road);
  }

  public void connect(Road roadTo, Road roadFrom, Node node) {
    roadTo.setBackRoad(roadFrom);
    roadFrom.setBackRoad(roadTo);

    roadTo.setFrom(this);
    roadTo.setTo(node);
    roadFrom.setFrom(node);
    roadFrom.setTo(this);

    addRoadOut(roadTo);
    addRoadIn(roadFrom);
    node.addRoadOut(roadFrom);
    node.addRoadIn(roadTo);
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public int getRoadsOutNum() {
    return roadsOut.size();
  }

  public int getRoadsInNum() {
    return roadsIn.size();
  }
}
