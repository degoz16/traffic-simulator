package ru.nsu.fit.traffic.controller.painters;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.Road;

import java.util.ArrayList;
import java.util.List;

public class ObjectPainter {
    private final int LANE_SIZE;
    private final int NODE_SIZE;
    private final Paint roadColor = Paint.valueOf("#9a9a9a");

    public ObjectPainter(int laneSize, int nodeSize) {
        LANE_SIZE = laneSize;
        NODE_SIZE = nodeSize;
    }

    public List<List<Shape>> paintRoad(Road road) {
        double pointFromX = road.getFrom().getX();
        double pointFromY = road.getFrom().getY();
        double pointToX = road.getTo().getX();
        double pointToY = road.getTo().getY();

        // направляющий вектор перпендикуляра
        double vx = pointFromY - pointToY;
        double vy = -pointFromX + pointToX;

        double vlen = Math.sqrt(Math.abs(vx * vx + vy * vy));
        List<List<Shape>> paintedRoad = new ArrayList<>();

        vx *= LANE_SIZE / vlen;
        vy *= LANE_SIZE / vlen;

        for (int i = 0; i < road.getLanesNum(); i++) {
            List<Shape> roadGroup = new ArrayList<>();
            Shape curr = new Polygon(
                    vx + pointFromX + i * vx,
                    vy + pointFromY + i * vy,
                    pointFromX + i * vx, pointFromY + i * vy,
                    pointToX + i * vx, pointToY + i * vy,
                    vx + pointToX + i * vx,
                    vy + pointToY + i * vy);
            curr.setFill(roadColor);
            curr.strokeWidthProperty().setValue(2);
            curr.setStroke(curr.getFill());

            roadGroup.add(curr);

            Line line = new Line(pointFromX + i * vx, pointFromY + i * vy,
                    pointToX + i * vx, pointToY + i * vy);
            line.setStroke(Color.WHITE);
            line.setStrokeWidth(1);

            if (i != 0) {
                line.getStrokeDashArray().addAll(4d, 21d);
            }

            roadGroup.add(line);


            if (i == road.getLanesNum() - 1) {
                line = new Line(vx + pointFromX + i * vx,
                        vy + pointFromY + i * vy,
                        vx + pointToX + i * vx,
                        vy + pointToY + i * vy);
                line.setStroke(Color.WHITE);
                line.setStrokeWidth(1);
                roadGroup.add(line);
            }

            paintedRoad.add(roadGroup);
        }
        return paintedRoad;
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

        Shape shape = new Circle(node.getX(), node.getY(), (double)maxSize / 2 * NODE_SIZE);
        shape.setFill(roadColor);
        return shape;
    }
}