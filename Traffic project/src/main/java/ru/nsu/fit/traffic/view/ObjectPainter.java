package ru.nsu.fit.traffic.view;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Pair;
import ru.nsu.fit.traffic.model.node.Node;
import ru.nsu.fit.traffic.model.PlaceOfInterest;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.trafficsign.RoadSign;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectPainter {
    private final int LANE_SIZE;
    private final int NODE_SIZE;
    private final Paint roadColor = Paint.valueOf("#9a9a9a");

    public ObjectPainter(int laneSize, int nodeSize) {
        LANE_SIZE = laneSize;
        NODE_SIZE = nodeSize;
    }

    private Color getGradientColor(int percent) {
        int percentBounded;
        if (percent < 0) {
            percentBounded = 0;
        } else percentBounded = Math.min(percent, 100);
        if (percentBounded < 50) {
            return Color.GREEN.interpolate(Color.YELLOW, (double) percentBounded / 50d);
        } else {
            return Color.YELLOW.interpolate(Color.RED, ((double) percentBounded - 50) / 50d);
        }
    }

    public List<List<Shape>> paintRoad(Road road, boolean reportMode) {
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
            if (!reportMode) {
                switch (road.getRoadHighLight()) {
                    case NONE -> curr.setFill(Paint.valueOf("transparent"));
                    case GREEN -> curr.setFill(Color.GREEN);
                    case RED -> curr.setFill(Color.RED);
                }

            } else {
                curr.setFill(getGradientColor(road.getCongestion()));
            }

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

            //Рисуем разметку скорости
            if (road.getLen() > LANE_SIZE * 3 && !road.getLane(i).getSigns().isEmpty()) {
                for (RoadSign s : road.getLane(i).getSigns()) {
                    switch (s.getSignType()) {
                        case SPEED_LIMIT:
                            double centerX = (pointFromX + i * vx + vx / 2 + vy * 2);
                            double centerY = (pointFromY + i * vy + vy / 2 - vx * 2);

                            Circle circle = new Circle(LANE_SIZE / 2.4);
                            circle.setStroke(Color.RED);
                            circle.setStrokeWidth(1);
                            circle.setFill(Color.WHITE);
                            circle.setCenterX(centerX);
                            circle.setCenterY(centerY);

                            String speed = s.getSettings().get("limit");
                            Text signSpeed = new Text(speed);
                            signSpeed.setStyle("-fx-font: 6 arial;");
                            signSpeed.setX(centerX - 3);
                            signSpeed.setY(centerY + 2);
                            roadGroup.add(circle);
                            roadGroup.add(signSpeed);
                            break;
                        case MAIN_ROAD:
                            if (road.getLanesNum() - 1 != i)
                                break;
                            double x = pointToX + vx * road.getLanesNum() / 2 - vy * 3;
                            double y = pointToY + vy * road.getLanesNum() / 2 + vx * 3;
                            int d = road.getLanesNum() * (int) (LANE_SIZE / 3);
                            Polygon shape = new Polygon(
                                    x - d, y,
                                    x, y + d,
                                    x + d, y,
                                    x, y - d);
                            shape.setFill(new Color(1, 1, 0, 0.5));
                            shape.setStroke(Color.WHITE);
                            shape.setStrokeWidth(d / 2.5);
                            roadGroup.add(shape);
                    }

                }
            }
            paintedRoad.add(roadGroup);
        }
        return paintedRoad;
    }

    public Shape paintNode(Node node) {
        //TODO: изменить на полигон.
        List<Pair<Double, Double>> pointsInPolygon = new ArrayList<Pair<Double, Double>>();
        if (node.getRoadsOutNum() <= 1 && node.getRoadsInNum() <= 1) {
            int maxSize = Math.max(
                    node.getRoadInStream()
                            .map(road -> road.getLanesNum() + road.getBackRoad().getLanesNum())
                            .max(Integer::compareTo)
                            .orElse(0),
                    node.getRoadOutStream()
                            .map(road -> road.getLanesNum() + road.getBackRoad().getLanesNum())
                            .max(Integer::compareTo)
                            .orElse(0));
            double rad = (double) maxSize / 2 * NODE_SIZE;
            Shape shape = new Circle(node.getX(), node.getY(), rad);
            shape.setFill(roadColor);
            return shape;
        }
        List<Road> roads = new ArrayList<>(node.getRoadsIn());
        roads.addAll(node.getRoadsIn());
        for (Road r : roads) {
            double vx = r.getFrom().getY() - r.getTo().getY();
            double vy = -r.getFrom().getX() + r.getTo().getX();
            double vlen = Math.sqrt(Math.abs(vx * vx + vy * vy));

            vx *= LANE_SIZE / vlen;
            vy *= LANE_SIZE / vlen;

            Node nodeTo = r.getTo();
            Line outLine = new Line(node.getX() + vx * r.getLanesNum(), node.getY() + vy * r.getLanesNum(),
                    nodeTo.getX() + vx * r.getLanesNum(), nodeTo.getY() + vy * r.getLanesNum());
            Pair<Double, Double> pointEdgeOut = new Pair<Double, Double>
                    ((node.getX() + vx * r.getLanesNum()), (node.getY() + vy * r.getLanesNum()));
            Pair<Double, Double> pointIntersectionCurr = null;
            Pair<Double, Double> pointRoadInEdge = null;
            double len = Double.MAX_VALUE;
            for (Road roadOut : roads) {
                if (roadOut == r) {
                    continue;
                }
                vx = roadOut.getFrom().getY() - roadOut.getTo().getY();
                vy = -roadOut.getFrom().getX() + roadOut.getTo().getX();
                vlen = Math.sqrt(Math.abs(vx * vx + vy * vy));

                vx *= LANE_SIZE / vlen;
                vy *= LANE_SIZE / vlen;
                nodeTo = roadOut.getTo();
                Node nodeFrom = roadOut.getFrom();
                Line inLine = new Line(nodeFrom.getX() + vx * r.getLanesNum(), nodeFrom.getY() + vy * r.getLanesNum(),
                        nodeTo.getX() + vx * r.getLanesNum(), nodeTo.getY() + vy * r.getLanesNum());
                Pair<Double, Double> inter = calculateIntersectionPair(inLine, outLine);
                if (pointRoadInEdge == null) {
                    pointRoadInEdge = new Pair<Double, Double>
                            ((node.getX() + vx * r.getLanesNum()), (node.getY() + vy * r.getLanesNum()));
                }
                if (inter == null)
                    continue;
                Pair<Double, Double> nodeOut = new Pair<>(roadOut.getFrom().getX(), roadOut.getFrom().getY());
                if (calculateDistance(inter, pointRoadInEdge) < len) {
                    pointIntersectionCurr = inter;
                    pointRoadInEdge = new Pair<Double, Double>
                            ((node.getX() + vx * r.getLanesNum()), (node.getY() + vy * r.getLanesNum()));
                    len = calculateDistance(inter, pointRoadInEdge);
                }
            }
            pointsInPolygon.add(pointEdgeOut);
            if (pointIntersectionCurr != null) {
                pointsInPolygon.add(pointIntersectionCurr);
            }
            pointsInPolygon.add(pointRoadInEdge);
        }

        Polygon shape = new Polygon(getConvexHull(pointsInPolygon));
        System.out.println(pointsInPolygon.size());
        System.out.println(getConvexHull(pointsInPolygon).length);
       /* if (node.getSpawners() != null) {
            if (node.getTrafficLight() == null) {
                Image img = new Image(getClass().getResource("../view/Images/spawner.png").toExternalForm());
                shape.setFill(new ImagePattern(img));
            } else {
                Image img = new Image(getClass().getResource("../view/Images/spawner_trafficlight.png").toExternalForm());
                shape.setFill(new ImagePattern(img));
            }
        } else if (node.getTrafficLight() != null) {
            Image img = new Image(getClass().getResource("../view/Images/trafficlight.png").toExternalForm());
            shape.setFill(new ImagePattern(img));
        }*/
        return shape;
    }

    private double calculateDistance(Pair<Double, Double> point1, Pair<Double, Double> point2) {
        return Math.sqrt(Math.pow(point1.getKey() - point2.getKey(), 2) +
                Math.pow(point1.getValue() - point2.getValue(), 2));
    }

    private double[] getConvexHull(List<Pair<Double, Double>> points) {
        List<Pair<Double, Double>> res = new ArrayList<>();
        boolean marks[] = new boolean[points.size()];
        Pair<Double, Double> currPair = points.get(0);

        for (Pair<Double, Double> pair : points) {
            if (currPair.getValue() < pair.getValue() ||
                    (currPair.getValue().equals(pair.getValue()) && currPair.getKey() < pair.getKey())) {
                currPair = pair;
            }
        }

        res.add(currPair);
        Pair<Double, Double> predPair = new Pair<>(currPair.getKey() - 1, currPair.getValue());
        do {
            double maxAngle = 0;
            Pair<Double, Double> des = null;
            for (Pair<Double, Double> candidate : points) {
                if (candidate == predPair || candidate == currPair){
                    continue;
                }
                double x1 = currPair.getKey() - predPair.getKey();
                double y1 = currPair.getValue() - predPair.getValue();
                double x2 = currPair.getKey() - candidate.getKey();
                double y2 = currPair.getValue() - candidate.getValue();
                double angle = Math.abs(x1*x2 + y1*y2)/(Math.sqrt(x1*x1+y1*y1) * Math.sqrt(x2*x2+y2*y2));
                if (angle > maxAngle){
                    maxAngle = angle;
                    des = candidate;
                }
            }
            predPair = currPair;
            currPair = des;
            res.add(currPair);
        } while (currPair != res.get(0));

        double[] point = new double[res.size() * 2];
        for (int i = 0; i < res.size(); ++i) {
            point[i * 2] = res.get(i).getKey();
            point[i * 2 + 1] = res.get(i).getValue();
        }
        return point;
    }

    private Pair<Double, Double> calculateIntersectionPair(Line line1, Line line2) {

        double m1, m2;
        double b1, b2;

        m1 = (line1.getStartY() - line1.getEndY()) / (line1.getStartX() - line1.getEndX());
        b1 = line1.getEndX() * m1 - line1.getEndY();

        m2 = (line2.getStartY() - line2.getEndY()) / (line2.getStartX() - line2.getEndX());
        b2 = line2.getEndX() * m2 - line2.getEndY();
        if (m1 == m2) {
            return null;
        }

        double x = (b2 - b1) / (m1 - m2);
        double y = m1 * x + b1;

        if ((x >= line1.getStartX() && x <= line1.getEndX()) || (x <= line1.getStartX() && x >= line1.getEndX())) {
            if ((y >= line1.getStartY() && y <= line1.getEndY()) || (y <= line1.getStartY() && y >= line1.getEndY()))
                return new Pair<>(Math.abs(x), Math.abs(y));
        }
        return null;
    }

    public Shape paintPlaceOfInterest(PlaceOfInterest placeOfInterest) {
        Rectangle building = new Rectangle(
                placeOfInterest.getX(),
                placeOfInterest.getY(),
                placeOfInterest.getWidth(),
                placeOfInterest.getHeight());
        Image img = new Image(getClass().getResource("../view/Images/building.png").toExternalForm());
        building.setFill(new ImagePattern(img));
        return building;
    }
}