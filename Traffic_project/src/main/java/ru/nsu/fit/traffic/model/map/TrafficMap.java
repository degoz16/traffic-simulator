package ru.nsu.fit.traffic.model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TrafficMap {
    private List<Node> nodes;
    private List<Road> roads;
    private List<Street> streets;
    private List<PlaceOfInterest> placesOfInterest;
    private String start;
    private String end;
    private int regionId;

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public TrafficMap() {
        roads = new ArrayList<>();
        nodes = new ArrayList<>();
        streets = new ArrayList<>();
        placesOfInterest = new ArrayList<>();
    }

    public void clearMap() {
        nodes.clear();
        roads.clear();
        streets.clear();
        placesOfInterest.clear();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addRoad(Road road) {
        roads.add(road);
    }

    public int getRoadCount() {
        return roads.size();
    }

    public int getPoiCount() {
        return placesOfInterest.size();
    }

    public int getNodesCount() {
        return nodes.size();
    }

    public Road getRoad(int i) {
        return roads.get(i);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public void removeRoad(Road road) {
        roads.remove(road);
    }

    public void removePOI(PlaceOfInterest poi) {
        placesOfInterest.remove(poi);
    }

    public void forEachPlaceOfInterest(Consumer<PlaceOfInterest> f) {
        placesOfInterest.forEach(f);
    }

    public void forEachNode(Consumer<Node> f) {
        nodes.forEach(f);
    }

    public void forEachRoad(Consumer<Road> f) {
        roads.forEach(f);
    }

    public int indexOfNode(Node node) {
        return nodes.indexOf(node);
    }

    public int indexOfRoad(Road road) {
        return roads.indexOf(road);
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads;
    }

    public void setStreets(List<Street> streets) {
        this.streets = streets;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public void setPlacesOfInterest(List<PlaceOfInterest> placesOfInterest) {
        this.placesOfInterest = placesOfInterest;
    }

    public PlaceOfInterest getPlaceOfInterest(int i) {
        return placesOfInterest.get(i);
    }

    public Node getNode(int id) {
        return nodes.get(id);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public void addPlaceOfInterest(PlaceOfInterest placeOfInterest) {
        placesOfInterest.add(placeOfInterest);
    }
}
