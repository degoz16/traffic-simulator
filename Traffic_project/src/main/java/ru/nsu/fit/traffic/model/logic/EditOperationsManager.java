package ru.nsu.fit.traffic.model.logic;

import ru.nsu.fit.traffic.model.congestion.ReportWindowStruct;
import ru.nsu.fit.traffic.model.map.TrafficMap;
import ru.nsu.fit.traffic.model.map.Node;
import ru.nsu.fit.traffic.model.map.PlaceOfInterest;
import ru.nsu.fit.traffic.model.playback.CarState;
import ru.nsu.fit.traffic.model.map.Road;
import ru.nsu.fit.traffic.model.map.RoadHighLight;
import ru.nsu.fit.traffic.model.map.TrafficLight;
import ru.nsu.fit.traffic.model.map.TrafficLightConfig;
import ru.nsu.fit.traffic.model.map.MainRoadSign;
import ru.nsu.fit.traffic.model.map.RoadSign;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditOperationsManager {
    private final int ROAD_LIMIT = 4;
    private final TrafficMap map;
    private EditOperation currentOperation = EditOperation.NONE;
    public Node lastNode = null;
    private int lanesNumLeft = 1;
    private int lanesNumRight = 1;
    private final UpdateObserver updateView;
    private RoadSign currSign = new MainRoadSign();
    private List<CarState> carStates = new ArrayList<>();

    public List<CarState> getCarStates() {
        return carStates;
    }

    public EditOperationsManager(UpdateObserver updateView) {
        this.map = new TrafficMap();
        this.updateView = updateView;
    }

    public TrafficMap getMap() {
        return map;
    }

    public RoadSign getCurrSign() {
        return currSign;
    }

    public void setCurrSign(RoadSign currSign) {
        this.currSign = currSign;
    }

    /**
     * Установить текущую операцию
     *
     * @param currentOperation - операция
     */
    public void setCurrentOperation(EditOperation currentOperation) {
        if (currentOperation == EditOperation.ROAD_CREATION) {
            lastNode = null;
        }
        this.currentOperation = currentOperation;
        updateView.update(this);
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
    public int buildRoadOnEmpty(double x, double y) {
        if (lanesNumLeft + lanesNumRight == 0) {
            return 0;
        }
        Node newNode = new Node(x, y);
        map.addNode(newNode);
        if (lastNode != null) {
            if (lastNode.getRoadPair().size() >= ROAD_LIMIT) {
                return -1;
            }
            updateView.update(this);
            if (buildRoad(newNode, lastNode) == 0){
                lastNode = newNode;
                return 0;
            }
            else return  -1;
        }
        lastNode = newNode;
        updateView.update(this);
        return 0;
    }

    public void setStartTime(String time) {
        map.setStart(time);
    }

    public void setEndTime(String time) {
        map.setEnd(time);
    }

    private int buildRoad(Node newNode, Node lastNode) {
        if (newNode.getRoadPair().size() >= ROAD_LIMIT
                || lastNode.getRoadPair().size() >= ROAD_LIMIT ){
            return -1;
        }
        Road newRoadTo = new Road(lanesNumRight);
        Road newRoadFrom = new Road(lanesNumLeft);
        newNode.connect(newRoadFrom, newRoadTo, lastNode);
        map.addRoad(newRoadTo);
        map.addRoad(newRoadFrom);
        if (lastNode.getRoadsOutNum() > 1) {
            lastNode.removeFromPlaceOfInterest();
        }
        return 0;
    }

    /**
     * Случай добавления ноды на дорогу
     *
     * @param x    координата
     * @param y    координата
     * @param road дорога, по которой было произведено нажатие
     * @return: 0 - success.
     *         -1 - too many roads in the node.
     *         -2 - road places on the existing road.
     */
    public int buildRoadOnRoad(double x, double y, Road road) {
        if (lanesNumLeft + lanesNumRight == 0) {
            return 0;
        }
        if (lastNode == road.getFrom() || lastNode == road.getTo()) {
            updateView.update(this);
            return -2;
        }
        Node nodeFrom = road.getFrom();
        Node nodeTo = road.getTo();

        Road backRoad = road.getBackRoad();

        road.disconnect();
        backRoad.disconnect();

        int res = buildRoadOnEmpty(x, y);

        if (res == 0) {
            Road road1 = road.getCopyRoad();
            Road backRoad1 = backRoad.getCopyRoad();
            map.addRoad(road1);
            map.addRoad(backRoad1);

            nodeFrom.connect(road, backRoad, lastNode);
            lastNode.connect(road1, backRoad1, nodeTo);
            updateView.update(this);
            System.out.println("road on road");
            return 0;
        }
        else return -1;
    }

    /**
     * Случай клика по существующей ноде
     *
     * @param node нода, по которой кликнули
     * @return: 0 - success.
     *         -1 - too many roads in the node.
     *         -2 - road places on the existing road.
     */
    public int buildRoadOnNode(Node node) {
        if (lanesNumLeft + lanesNumRight == 0 || lastNode == node) {
            return 0;
        }
        if (lastNode == null) {
            lastNode = node;
            return 0;
        }
        if (node.getRoadPair().size() >= ROAD_LIMIT) {
            return -1;
        }
        boolean checkOverlap = lastNode.getRoadOutStream().anyMatch(road -> road.getTo() == node);
        if (!checkOverlap) {
            buildRoad(lastNode, node);
            lastNode = node;
            if (lastNode.getRoadsOutNum() > 1) {
                lastNode.removeFromPlaceOfInterest();
            }
        }
        node.setTrafficLight(null);
        updateView.update(this);
        return 0;
    }

    public void buildRoadOnPlaceOfInterest(double x, double y, PlaceOfInterest placeOfInterest) {
        buildRoadOnEmpty(x, y);
        placeOfInterest.addNode(lastNode);
        lastNode.setPlaceOfInterest(placeOfInterest);
        List<Node> nodeList = new ArrayList<>();
        updateView.update(this);
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
        updateView.update(this);
    }

    public void updateCongestions(ReportWindowStruct reportWindowStruct) {
        for (int i = 0; i < Math.min(reportWindowStruct.getCongestionList().size(), map.getRoadCount()); i++) {
            map.getRoad(i).setCongestion(reportWindowStruct.getCongestionListFilled().get(i));
        }
        updateView.update(this);
    }

    public void updateCarStates(List<CarState> carStates) {
        this.carStates = carStates;
        updateView.update(this);
    }

    public List<Integer> findPairOfRoad(Node node) {
        List<Double> k = new ArrayList<>();
        for (Iterator<Road> it = node.getRoadInStream().iterator(); it.hasNext(); ) {
            Road r = it.next();
            double a = -(r.getFrom().getY() - r.getTo().getY()) / (r.getFrom().getX() - r.getTo().getX());
            k.add(Math.atan(a));
            System.out.println(Math.atan(a));
        }
        double d2 = Double.MAX_VALUE;
        double d1 = d2;
        int curr = -1;

        for (int i = 1; i < k.size(); ++i) {
            if (Math.abs(k.get(0) - k.get(i)) < d1) {
                d1 = Math.abs(k.get(0) - k.get(i));
                curr = i;
            }
        }

        int curr2 = -1;
        for (int i = 1; i < k.size(); ++i) {
            if (curr != i && Math.abs(k.get(0) - k.get(i)) < d1) {
                d2 = Math.abs(k.get(0) - k.get(i));
                curr2 = i;
            }
        }
        List<Integer> res = new ArrayList<>();
        res.add(curr);
        if (d1 < d2) {
            res.add(0);
        } else {
            res.add(curr2);
        }
        return res;
    }

    public void updateRoadsHighLight(Node node) {
        List<Integer> greenIndex = findPairOfRoad(node);

        int i = 0;
        for (Iterator<Road> it = node.getRoadInStream().iterator(); it.hasNext(); i++) {
            Road r = it.next();
            boolean isGreen = false;
            for (int j : greenIndex)
                if (j == i) {
                    isGreen = true;
                    break;
                }
            r.setRoadHighLight(isGreen ? RoadHighLight.GREEN : RoadHighLight.RED);
        }
        updateView.update(this);
    }

    public void resetRoadsHighLight(Node node) {
        node.foreachRoadIn(road -> {
            road.setRoadHighLight(RoadHighLight.NONE);
        });
    }

    /**
     * Текущая операция
     *
     * @return операция
     */
    public EditOperation getCurrentOperation() {
        return currentOperation;
    }

    public void applyTrafficLightSettings(Node lastNodeClicked, int greenDelay, int redDelay) {
        List<Road> roadsGreen = new ArrayList<>();
        List<Road> roadsRed = new ArrayList<>();
        List<Integer> roadsIndexes = findPairOfRoad(lastNodeClicked);
        for (int i = 0; i < lastNodeClicked.getRoadsInNum(); ++i) {
            if (roadsIndexes.get(0) != i && roadsIndexes.get(1) != i)
                roadsRed.add((Road) lastNodeClicked.getRoadInStream().toArray()[i]);
            else roadsGreen.add((Road) lastNodeClicked.getRoadInStream().toArray()[i]);
        }
        TrafficLightConfig greenConfig = new TrafficLightConfig(greenDelay, roadsGreen);
        TrafficLightConfig redConfig = new TrafficLightConfig(redDelay, roadsRed);
        TrafficLight trafficLight = new TrafficLight(greenConfig, redConfig);
        lastNodeClicked.setTrafficLight(trafficLight);
        setCurrentOperation(EditOperation.NONE);
        updateView.update(this);
    }
}
