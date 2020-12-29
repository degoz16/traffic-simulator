package ru.nsu.fit.traffic.model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Road {
    private Node from = null;
    private Node to = null;
    private List<Lane> lanes;
    private int length = -1;
    private Road backRoad = null;
    private Street currStreet = null;
    private int congestion = 0;
    private RoadHighLight roadHighLight = RoadHighLight.NONE;

    public Road(int lanesNum) {
        lanes = new ArrayList<>();
        for (int i = 0; i < lanesNum; ++i) lanes.add(new Lane());
    }

    public Road() {
    }

    public RoadHighLight getRoadHighLight() {
        return roadHighLight;
    }

    public void setRoadHighLight(RoadHighLight roadHighLight) {
        this.roadHighLight = roadHighLight;
    }

    public int getCongestion() {
        return congestion;
    }

    public void setCongestion(int congestion) {
        this.congestion = congestion;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
    }

    public void forEachLane(Consumer<Lane> f) {
        lanes.forEach(f);
    }

    // Этот и аналогичные методы для Lane и RoadSign
    // нужны для случая вставки ноды в середину дороги
    // чтобы сохранить настройки дороги.

    /**
     * Возвращает копию дороги.
     *
     * @return копия
     */
    public Road getCopyRoad() {
        List<Lane> laneList = new ArrayList<>();
        Road copyRoad = new Road(0);
        lanes.forEach(lane -> laneList.add(lane.getCopyLane()));
        copyRoad.setLanes(laneList);
        return copyRoad;
    }

    public void setCurrStreet(Street currStreet) {
        this.currStreet = currStreet;
    }

    public void disconnect() {
        to.removeRoadIn(this);
        from.removeRoadOut(this);
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public Road getBackRoad() {
        return backRoad;
    }

    public void setBackRoad(Road backRoad) {
        this.backRoad = backRoad;
    }

    public int getLen() {
        if (length == -1) {
            return length =
                    (int)
                            Math.sqrt(
                                    Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2));
        } else return length;
    }

    public Lane getLane(int id) {
        return lanes.get(id);
    }

    public int getLanesNum() {
        return lanes.size();
    }

    public void removeLane(int id) {
        lanes.remove(id);
    }

    public void addLane(int id) {
        lanes.add(id, new Lane());
    }

    public void clearLanes() {
        lanes = new ArrayList<>();
    }

    public Street getStreet() {
        return currStreet;
    }

    public void disconnectWithStreet() {
        if (currStreet != null) {
            currStreet.removeRoad(this);
            currStreet = null;
        }
    }
}
