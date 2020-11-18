package ru.nsu.fit.traffic.controller.painters;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.Road;

public class ObjectPainter {
    private final int LANE_SIZE;
    private final int NODE_SIZE;

    public ObjectPainter(int laneSize, int nodeSize) {
        LANE_SIZE = laneSize;
        NODE_SIZE = nodeSize;
    }

    public Shape paintRoad(Road road) {
        double pointFromX = road.getFrom().getX();
        double pointFromY = road.getFrom().getY();
        double pointToX = road.getTo().getX();
        double pointToY = road.getTo().getY();

        // формула прямой y = ax + b
        double a = (double) (pointFromY - pointToY) / (pointToX - pointFromX);
        double b = (double) (pointFromX * pointToY - pointFromY * pointToX) / (pointToX - pointFromX);

        // направляющий вектор перпендикуляра
        double vx = pointFromY - pointToY;
        double vy = -pointFromX + pointToX;
        double vlen = Math.sqrt(Math.abs(vx * vx + vy * vy));
        double size = road.getLanes().size() * LANE_SIZE / vlen;

        Shape curr = new Polygon(
                vx * size / 2 + pointFromX, vy * size / 2 + pointFromY,
                pointFromX, pointFromY,
                pointToX, pointToY,
                vx * size / 2 + pointToX, vy * size / 2 + pointToY);
        curr.setFill(Paint.valueOf("#aaaaaa"));
        curr.setStroke(Paint.valueOf("#ffffff"));
        curr.strokeWidthProperty().setValue(2);
        return curr;
    }

    public Shape paintNode(Node node) {
        int maxSize = Math.max(
                node.getRoadInStream()
                        .map(road -> road.getLanesNum() + road.getBackRoad().getLanesNum())
                        .max(Integer::compareTo)
                        .orElse(0),
                node.getRoadOutStream()
                        .map(road -> road.getLanesNum() + road.getBackRoad().getLanesNum())
                        .max(Integer::compareTo)
                        .orElse(0));

        //todo: мне не нравятся наши ноды на карте, нужно подумать, как норм рисовать
        Shape shape = new Circle(node.getX(), node.getY(), (double)maxSize / 2 * NODE_SIZE);
        shape.setFill(Paint.valueOf("#aaaaaa"));
        return shape;
    }
}
