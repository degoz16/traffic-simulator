package ru.nsu.fit.traffic.model.map;

import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TrafficMap {
    private List<Node> nodes = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();
    private List<Street> streets = new ArrayList<>();
    private List<PlaceOfInterest> placesOfInterest = new ArrayList<>();
    private String start;
    private String end;
    private int regionId;
    private double width = 1000;
    private double height = 1000;

    public TrafficMap() {

    }

    public TrafficMap(int regId, RectRegion region, double scale) {
        this.width = region.getWidth() * scale;
        this.height = region.getHeight() * scale;
        for (int i = 0; i < region.getConnectorsCount(); i++) {
            nodes.add(new Node(
                region.getConnector(i).getX() * scale,
                region.getConnector(i).getY() * scale,
                regId,
                i));
        }
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

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
