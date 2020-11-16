package ru.nsu.fit.traffic.painted;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.Road;

public class ObjectPainter {
    private final int LANE_SIZE;
    private final int NODE_SIZE;

    public ObjectPainter(int LANE_SIZE, int NODE_SIZE) {
        this.LANE_SIZE = LANE_SIZE;
        this.NODE_SIZE = NODE_SIZE;
    }

    public Shape paintRoad(String id, Road road) {
        int pointFromX = road.getFrom().getX();
        int pointFromY = road.getFrom().getY();
        int pointToX = road.getTo().getX();
        int pointToY = road.getTo().getY();

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
        curr.setId(id);
        return curr;


    }

    public Shape paintNode(String id, Node node) {
        int maxSize = -1;
        for (Road r : node.getRoadsIn()) {
            if (r.getLanes().size() > maxSize) {
                maxSize = r.getLanes().size();
            }
        }
        for (Road r : node.getRoadsOut()) {
            if (r.getLanes().size() > maxSize) {
                maxSize = r.getLanes().size();
            }
        }
        //todo: мне не нравятся наши ноды на карте, нужно подумать, как норм рисовать
        Shape shape = new Circle(node.getX(), node.getY(), maxSize/2 * NODE_SIZE);
        shape.setFill(Paint.valueOf("#aaaaaa"));
        shape.setId(id);
        return shape;
    }
}
