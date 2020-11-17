package ru.nsu.fit.traffic.model.logic;

import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.Road;
import ru.nsu.fit.traffic.model.TrafficMap;

import java.util.concurrent.atomic.AtomicBoolean;

public class EditOperationsManager {
    public EditOperationsManager(TrafficMap map) {
        this.map = map;
    }

    private TrafficMap map;
    private EditOperation currentOperation = EditOperation.NONE;
    private Node lastNode = null;

    /**
     * Установить текущую операцию
     * @param currentOperation - операция
     */
    public void setCurrentOperation(EditOperation currentOperation) {
        this.currentOperation = currentOperation;
    }


    public void resetLastNode(){
        lastNode = null;
    }

    /**
     * Случай добавления ноды на пустое место
     * @param x координата x
     * @param y координата y
     */
    public void buildRoadOnEmpty(double x, double y) {
        Node newNode = new Node(x, y);
        map.addNode(newNode);
        if (lastNode != null) {
            Road newRoadTo = new Road(1);
            Road newRoadFrom = new Road(1);
            newNode.connect(newRoadTo, newRoadFrom, lastNode);
            map.addRoad(newRoadTo);
            map.addRoad(newRoadFrom);
        }
        lastNode = newNode;
    }

    /**
     * Случай добавления ноды на дорогу
     * @param x координата
     * @param y координата
     * @param road дорога, по которой было произведено нажатие
     */
    public void buildRoadOnRoad(double x, double y, Road road) {
        Node nodeFrom = road.getFrom();
        Node nodeTo = road.getTo();

        Road backRoad = road.getBackRoad();

        road.disconnect();
        backRoad.disconnect();

        buildRoadOnEmpty(x, y);

        Road road1 = road.getCopyRoad();
        Road backRoad1 = backRoad.getCopyRoad();
        map.addRoad(road1);
        map.addRoad(backRoad1);

        nodeFrom.connect(road, backRoad, lastNode);
        lastNode.connect(road1, backRoad1, nodeTo);
    }

    public void buildRoadOnNode(Node node) {
        boolean checkOverlap = lastNode.getRoadOutStream().anyMatch(road -> road.getTo() == node);
        if (!checkOverlap) {
            Road newRoadTo = new Road(1);
            Road newRoadFrom = new Road(1);
            lastNode.connect(newRoadTo, newRoadFrom, node);
            map.addRoad(newRoadTo);
            map.addRoad(newRoadFrom);
            lastNode = node;
        }
    }

    public EditOperation getCurrentOperation() {
        return currentOperation;
    }
}
