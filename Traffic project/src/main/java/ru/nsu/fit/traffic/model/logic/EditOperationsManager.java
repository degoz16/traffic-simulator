package ru.nsu.fit.traffic.model.logic;

import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.PlaceOfInterest;
import ru.nsu.fit.traffic.model.Road;
import ru.nsu.fit.traffic.model.TrafficMap;

public class EditOperationsManager {
    public EditOperationsManager(TrafficMap map) {
        this.map = map;
    }

    private TrafficMap map;
    private EditOperation currentOperation = EditOperation.NONE;
    public Node lastNode = null;
    private int lanesNumLeft = 1;
    private int lanesNumRight = 1;

    /**
     * Установить текущую операцию
     *
     * @param currentOperation - операция
     */
    public void setCurrentOperation(EditOperation currentOperation) {
        this.currentOperation = currentOperation;
    }

    public void setLanesNumLeft(int lanesNumLeft) {
        this.lanesNumLeft = lanesNumLeft;
    }

    public void setLanesNumRight(int lanesNumRight) {
        this.lanesNumRight = lanesNumRight;
    }

    public void resetLastNode() {
        lastNode = null;
    }

    /**
     * Случай добавления ноды на пустое место
     *
     * @param x координата x
     * @param y координата y
     */
    public void buildRoadOnEmpty(double x, double y) {
        if (lanesNumLeft + lanesNumRight == 0) {
            return;
        }
        Node newNode = new Node(x, y);
        map.addNode(newNode);
        if (lastNode != null) {
            Road newRoadTo = new Road(lanesNumLeft);
            Road newRoadFrom = new Road(lanesNumRight);
            newNode.connect(newRoadTo, newRoadFrom, lastNode);
            map.addRoad(newRoadTo);
            map.addRoad(newRoadFrom);
        }
        lastNode = newNode;
    }

    /**
     * Случай добавления ноды на дорогу
     *
     * @param x    координата
     * @param y    координата
     * @param road дорога, по которой было произведено нажатие
     */
    public void buildRoadOnRoad(double x, double y, Road road) {
        if (lanesNumLeft + lanesNumRight == 0) {
            return;
        }
        if (lastNode == road.getFrom() || lastNode == road.getTo()) {
            return;
        }
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

    /**
     * Случай клика по существующей ноде
     *
     * @param node нода, по которой кликнули
     */
    public void buildRoadOnNode(Node node) {
        if (lanesNumLeft + lanesNumRight == 0) {
            return;
        }
        if (lastNode == null) {
            lastNode = node;
            return;
        }
        if (node.getRoadsOutNum() >= 4) {
            return;
        }
        boolean checkOverlap = lastNode.getRoadOutStream().anyMatch(road -> road.getTo() == node);
        if (!checkOverlap) {
            Road newRoadTo = new Road(lanesNumLeft);
            Road newRoadFrom = new Road(lanesNumRight);
            lastNode.connect(newRoadTo, newRoadFrom, node);
            map.addRoad(newRoadTo);
            map.addRoad(newRoadFrom);
            lastNode = node;
        }
    }

    public void buildRoadOnPlaceOfInterest(double x, double y, PlaceOfInterest placeOfInterest) {
        buildRoadOnEmpty(x, y);
        placeOfInterest.addNode(lastNode);
        lastNode.setPlaceOfInterest(placeOfInterest);
        lastNode.getRoadInStream().forEach(road -> road.getFrom().removeFromPlaceOfInterest());
    }

    public void addPlaceOfInterest(double x, double y, double width, double height) {
        PlaceOfInterest placeOfInterest = new PlaceOfInterest(width, height, x, y);
        map.forEachNode(node -> {
            if (
                    node.getX() > x
                    && node.getY() > y
                    && node.getX() < x + width
                    && node.getY() < y + height
                    && node.getRoadsOutNum() <= 1
            ) {
                node.setPlaceOfInterest(placeOfInterest);
                placeOfInterest.addNode(node);
            }
        });
        map.addPlaceOfInterest(placeOfInterest);
    }

    /**
     * Текущая операция
     *
     * @return операция
     */
    public EditOperation getCurrentOperation() {
        return currentOperation;
    }
}
