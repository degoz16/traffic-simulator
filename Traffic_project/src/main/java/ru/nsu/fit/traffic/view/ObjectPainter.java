package ru.nsu.fit.traffic.view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Pair;
import ru.nsu.fit.traffic.model.node.Node;
import ru.nsu.fit.traffic.model.place.PlaceOfInterest;
import ru.nsu.fit.traffic.model.playback.CarState;
import ru.nsu.fit.traffic.model.road.Lane;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.trafficsign.RoadSign;

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

        double vlen = Math.sqrt(vx * vx + vy * vy);
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
                    case NONE -> curr.setFill(roadColor);
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
        List<Pair<Double, Double>> pointsInPolygon = new ArrayList<Pair<Double, Double>>();
        List<Road> roads = new ArrayList<>(node.getRoadsIn());
        roads.addAll(node.getRoadsOut());
        for (int i = 0; i < roads.size(); ++i) {
            Road r = roads.get(i);
            double vx = r.getFrom().getY() - r.getTo().getY();
            double vy = -r.getFrom().getX() + r.getTo().getX();
            double vlen = Math.sqrt(Math.abs(vx * vx + vy * vy));

            vx *= LANE_SIZE * r.getLanesNum() / vlen;
            vy *= LANE_SIZE * r.getLanesNum() / vlen;

            Node nodeTo = r.getTo();
            Node nodeFrom = r.getFrom();
            Pair<Double, Double> pointEdgeOut;
            Line outLine = new Line(nodeFrom.getX() + vx,
                    nodeFrom.getY() + vy,
                    nodeTo.getX() + vx,
                    nodeTo.getY() + vy);
            pointEdgeOut = new Pair<>(node.getX() + vx, node.getY() + vy);
            for (int j = i + 1; j < roads.size(); ++j) {
                Road roadOut = roads.get(j);
                if (roadOut == r || roadOut == r.getBackRoad()) {
                    continue;
                }
                vx = roadOut.getFrom().getY() - roadOut.getTo().getY();
                vy = -roadOut.getFrom().getX() + roadOut.getTo().getX();
                vlen = Math.sqrt(Math.abs(vx * vx + vy * vy));

                vx *= LANE_SIZE / vlen;
                vy *= LANE_SIZE / vlen;
                nodeTo = roadOut.getTo();
                nodeFrom = roadOut.getFrom();
                Line inLine = new Line(nodeFrom.getX() + vx * roadOut.getLanesNum(), nodeFrom.getY() + vy * roadOut.getLanesNum(),
                        nodeTo.getX() + vx * roadOut.getLanesNum(), nodeTo.getY() + vy * roadOut.getLanesNum());
                Pair<Double, Double> inter = calculateIntersectionPair(inLine, outLine);

                if (inter == null)
                    continue;
                pointsInPolygon.add(inter);
            }
            pointsInPolygon.add(pointEdgeOut);
        }

        Polygon shape = new Polygon(getConvexHull(pointsInPolygon));
        //System.out.println(pointsInPolygon.size());
        //System.out.println(getConvexHull(pointsInPolygon).length);
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
        }
        return shape;
    }*/

        //System.out.println(shape);
        shape.setFill(roadColor);
        return /*new Polygon(point)*/ shape;
    }

    private double calculateDistance(Pair<Double, Double> point1, Pair<Double, Double> point2) {
        return Math.sqrt(Math.pow(point1.getKey() - point2.getKey(), 2) +
                Math.pow(point1.getValue() - point2.getValue(), 2));
    }

    private boolean rotate(Pair<Double, Double> A, Pair<Double, Double> B, Pair<Double, Double> C) {
        return (B.getKey() - A.getKey()) * (C.getValue() - B.getValue()) -
                (B.getValue() - A.getValue()) * (C.getKey() - B.getKey()) > 0;
    }

    private double[] getConvexHull(List<Pair<Double, Double>> input) {
        int n = input.size();// число точек
        int[] p = new int[n]; // список номеров точек
        for (int i = 0; i < n; ++i) {
            p[i] = i;
        }
        for (int i = 1; i < n; ++i) {
            if (input.get(p[i]).getKey() < input.get(p[0]).getKey()) {
                int save = p[i];
                p[i] = p[0];
                p[0] = save;
            }
        }
        for (int i = 2; i < n; ++i) {
            int j = i;
            while (j > 1 && !(rotate(input.get(p[0]), input.get(p[j - 1]), input.get(p[j])))) {
                int save = p[j];
                p[j] = p[j - 1];
                p[j - 1] = save;
                j -= 1;
            }
        }
        List<Integer> stack = new ArrayList<>();
        stack.add(p[0]);
        stack.add(p[1]);
        for (int i = 2; i < n; ++i) {
            if (stack.size() > 1) {
                System.out.println("input: " + input.toString());
                System.out.println("stack: " + stack.toString());
                while (!rotate(input.get(stack.get(stack.size() - 2)),
                        input.get(stack.get(stack.size() - 1)),
                        input.get(p[i]))) {
                    System.out.println("stack: " + stack.toString());
                    stack.remove(stack.size() - 2);
                }
            }
            stack.add(p[i]);
        }

        List<Pair<Double, Double>> output = new ArrayList<>();
        for (Integer i : stack) {
            output.add(input.get(i));
        }
        double[] point = new double[output.size() * 2];
        for (int i = 0; i < output.size(); ++i) {
            point[i * 2] = output.get(i).getKey();
            point[i * 2 + 1] = output.get(i).getValue();
        }
        System.out.println("----------------------------------------------");
        return point;
    }

    private Pair<Double, Double> calculateIntersectionPair(Line line1, Line line2) {

        double m1, m2;
        double b1, b2;
        return null;
        /*m1 = (line1.getStartY() - line1.getEndY()) / (line1.getStartX() - line1.getEndX());
        b1 = line1.getEndX() * m1 - line1.getEndY();

        m2 = (line2.getStartY() - line2.getEndY()) / (line2.getStartX() - line2.getEndX());
        b2 = line2.getEndX() * m2 - line2.getEndY();
        if (Math.abs(m1 - m2) <= 0.00001) {
            return null;
        }

        double x = (b2 - b1) / (m1 - m2);
        double y = m1 * x + b1;
        x = Math.abs(x);
        y = Math.abs(y);
        Pair<Double, Double> res = new Pair<>(Math.abs(x), Math.abs(y));
        /*if (x > Math.max(line1.getStartX(), line1.getEndX()))
            return null;
        if (x < Math.min(line1.getStartX(), line1.getEndX()))
            return null;*/

        /*return res;*/
    }

    public Shape paintPlaceOfInterest(PlaceOfInterest placeOfInterest) {
        Rectangle building = new Rectangle(
                placeOfInterest.getX(),
                placeOfInterest.getY(),
                placeOfInterest.getWidth(),
                placeOfInterest.getHeight());
        Image img = new Image(getClass().getResource("Images/building.png").toExternalForm());
        building.setFill(new ImagePattern(img));
        return building;
    }

    private double getAngle(double carX, double carY, double destX, double destY) {
        // Получим косинус угла по формуле
        double x = destX - carX;
        double y = destY - carY;
        double cos = x / Math.sqrt(x * x + y * y);
        double angle = Math.acos(cos);
// Вернем arccos полученного значения (в радианах!)
        if (destY < carY) {
            angle = 2 * Math.PI - angle;
        }
        // в ГРАДУСЫ (сириусли, javafx?) pizdec blyat...
        return angle * 180d / Math.PI + 90d;
    }

    public Shape paintCar(CarState carState, Road road, Lane lane) {
        Rectangle car = new Rectangle(
                carState.getXCoord() - CarState.CAR_WIDTH / 2,
                carState.getYCoord() - CarState.CAR_HEIGHT / 2,
                CarState.CAR_WIDTH, CarState.CAR_HEIGHT
        );
        double x1 = road.getFrom().getX();
        double y1 = road.getFrom().getY();
        double x2 = road.getTo().getX();
        double y2 = road.getTo().getY();
        double len = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        x2 = (x2 - x1) / len;
        y2 = (y2 - y1) / len;
        double swap = x2;
        x2 = y2;
        y2 = -swap;
        x1 = road.getTo().getX() - x2 * 10 * (carState.getCurrentLane() + 0.5);
        y1 = road.getTo().getY() - y2 * 10 * (carState.getCurrentLane() + 0.5);

        car.setRotate(getAngle(
                car.getX() - car.getWidth() / 2,
                car.getY() - car.getHeight() / 2,
                x1, y1));
        Image img = new Image(getClass().getResource("../view/Images/car.png").toExternalForm());
        car.setFill(new ImagePattern(img));
        return car;
    }
}