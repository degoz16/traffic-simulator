package ru.nsu.fit.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class Road {
    private Node from = null;
    private Node to = null;
    private List<Lane> lanes;
    private int length = -1;
    private Road backRoad = null;

    public Road(int lanesNum) {
        lanes = new ArrayList<>();
        for (int i = 0; i < lanesNum; ++i)
            lanes.add(new Lane(this));
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
    }

    //Этот и аналогичные методы для Lane и RoadSign
    //нужны для случая вставки ноды в середину дороги
    //чтобы сохранить настройки дороги.
    /**
     * Возвращает копию дороги.
     * @return копия
     */
    public Road getCopyRoad() {
        List<Lane> laneList = new ArrayList<>();
        Road copyRoad = new Road(0);
        lanes.forEach(lane -> laneList.add(lane.getCopyLane(copyRoad)));
        copyRoad.setLanes(laneList);
        return copyRoad;
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

    public void setBackRoad(Road backRoad) {
        this.backRoad = backRoad;
    }

    public Road getBackRoad() {
        return backRoad;
    }

    public int getLen() {
        if (length == -1) {
            return length = (int) Math.sqrt(
                    Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2));
        } else return length;
    }

    public List<Lane> getLanes(){
        return lanes;
    }

    public int getLanesNum() {
        return lanes.size();
    }
}
