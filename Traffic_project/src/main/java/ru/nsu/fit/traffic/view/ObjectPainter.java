package ru.nsu.fit.traffic.view;

import java.util.*;

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
import ru.nsu.fit.traffic.model.map.Node;
import ru.nsu.fit.traffic.model.map.PlaceOfInterest;
import ru.nsu.fit.traffic.model.playback.CarState;
import ru.nsu.fit.traffic.model.map.Lane;
import ru.nsu.fit.traffic.model.map.Road;
import ru.nsu.fit.traffic.model.map.RoadSign;


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
                line.setStrokeWidth(0);
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

    public List<Shape> paintNode(Node node) {
        Shape shape;
        List<Shape> res = new ArrayList<>();
        if(node.getConnector() != null) {
            shape = new Circle(8);
            shape.setFill(Color.LIGHTBLUE);
            res.add(shape);
            return res;
        }
        if (node.getRoadPair().size() == 0) {
            return new ArrayList<>();
        }
        if (node.getRoadPair().size() <= 1) {

            Road currRoad = node.getRoadsIn().get(0);
            double rad = (double) (currRoad.getLanesNum() + currRoad.getBackRoad().getLanesNum()) * LANE_SIZE / 2;
            double x1 = currRoad.getFrom().getY() - currRoad.getTo().getY();
            double y1 = -currRoad.getFrom().getX() + currRoad.getTo().getX();
            double len1 = Math.sqrt(Math.abs(x1 * x1 + y1 * y1));

            x1 *= LANE_SIZE * currRoad.getLanesNum() / len1;
            y1 *= LANE_SIZE * currRoad.getLanesNum() / len1;

            shape = new Circle(node.getX() + x1 * currRoad.getLanesNum() - currRoad.getBackRoad().getLanesNum() * x1,
                    node.getY() + y1 * currRoad.getLanesNum() - currRoad.getBackRoad().getLanesNum() * y1, rad);
            shape.setFill(roadColor);

            if (node.getSpawners() != null) {
                Image img = new Image(getClass().getResource("../view/Images/spawner.png").toExternalForm());
                shape.setFill(new ImagePattern(img));
            }
            List<Shape> res1 = new ArrayList<>();
            res.add(shape);
            return res1;
        }
        List<Map.Entry<Road, Road>> roadPairs = node.getRoadPair();
        double[] angles = new double[roadPairs.size()];

        for (int i = 0; i < roadPairs.size(); ++i) {
            Road currRoad = roadPairs.get(i).getKey();
            angles[i] = getAngle(currRoad.getFrom().getX(), currRoad.getFrom().getY(),
                    currRoad.getTo().getX(), currRoad.getTo().getY());
        }
        int minAngleIndex = -1;
        double[] order = new double[roadPairs.size()];
        for (int i = 0; i < roadPairs.size(); ++i) {
            for (int j = 0; j < roadPairs.size(); ++j) {
                if ((minAngleIndex == -1 && angles[j] != Double.MAX_VALUE) || (angles[minAngleIndex] > angles[j])) {
                    minAngleIndex = j;
                }
            }
            order[i] = minAngleIndex;
            angles[minAngleIndex] = Double.MAX_VALUE;
        }

        List<Map.Entry<Double, Double>> polygonPoints = new ArrayList<>();

        for (int i = 0; i < order.length; ++i) {
            int currIndex = -1;
            int nextIndex = -1;
            for (int j = 0; j < order.length; ++j) {
                if (order[j] == i) {
                    currIndex = j;
                }
                if (order[j] == i + 1 || (i == order.length - 1 && order[j] == 0)) {
                    nextIndex = j;
                }
            }
            Road currRoad = roadPairs.get(currIndex).getKey();
            Road nextRoad = roadPairs.get(nextIndex).getValue();

            double x1 = currRoad.getFrom().getY() - currRoad.getTo().getY();
            double y1 = -currRoad.getFrom().getX() + currRoad.getTo().getX();
            double len1 = Math.sqrt(Math.abs(x1 * x1 + y1 * y1));

            x1 *= LANE_SIZE * currRoad.getLanesNum() / len1;
            y1 *= LANE_SIZE * currRoad.getLanesNum() / len1;

            Line line1 = new Line(currRoad.getFrom().getX() + x1,
                    currRoad.getFrom().getY() + y1,
                    currRoad.getTo().getX() + x1,
                    currRoad.getTo().getY() + y1);

            double x2 = nextRoad.getFrom().getY() - nextRoad.getTo().getY();
            double y2 = -nextRoad.getFrom().getX() + nextRoad.getTo().getX();
            double len2 = Math.sqrt(Math.abs(x2 * x2 + y2 * y2));

            x2 *= LANE_SIZE * nextRoad.getLanesNum() / len2;
            y2 *= LANE_SIZE * nextRoad.getLanesNum() / len2;

            Line line2 = new Line(nextRoad.getTo().getX() + x2,
                    nextRoad.getTo().getY() + y2,
                    nextRoad.getFrom().getX() + x2,
                    nextRoad.getFrom().getY() + y2);

            Map.Entry<Double, Double> inter = calculateIntersectionPair(line1, line2);

            polygonPoints.add(new AbstractMap.SimpleEntry<>(inter.getKey(), inter.getValue()));
            polygonPoints.add(new AbstractMap.SimpleEntry<>(line1.getStartX(), line1.getStartY()));
            polygonPoints.add(new AbstractMap.SimpleEntry<>(line2.getStartX(), line2.getStartY()));
        }

        double[] points = getConvexHull(polygonPoints);
        shape = new Polygon(points);
        shape.setFill(roadColor);
        shape.setStroke(roadColor);
        shape.setStrokeWidth(2);

        res.add(shape);

        if (node.getTrafficLight() != null) {
            shape = new Circle(node.getX(), node.getY(), LANE_SIZE/1.5);
            Image img = new Image(getClass().getResource("../view/Images/traffic_light.png").toExternalForm());
            shape.setFill(new ImagePattern(img));
            res.add(shape);
        }
        return res;
    }

    private boolean rotate(Map.Entry<Double, Double> A, Map.Entry<Double, Double> B, Map.Entry<Double, Double> C) {
        return (B.getKey() - A.getKey()) * (C.getValue() - B.getValue()) -
                (B.getValue() - A.getValue()) * (C.getKey() - B.getKey()) > 0;
    }

    /**
     * This method create convex hull using Jarvis march.
     *
     * @param input - Map with points.
     * @return array with coordinates. [2*i] - x, [2*i+1] - y. For creating Polygon.
     */
    private double[] getConvexHull(List<Map.Entry<Double, Double>> input) {
        int n = input.size();// число точек
        List<Integer> p = new LinkedList<>(); // список номеров точек
        for (int i = 0; i < n; ++i) {
            p.add(i);
        }
        for (int i = 0; i < n; ++i) {
            if (input.get(p.get(i)).getKey() < input.get(p.get(0)).getKey()) {
                Collections.swap(p, 0, i);
            }
        }
        List<Integer> stack = new ArrayList<>();
        stack.add(p.remove(0));
        p.add(stack.get(0));
        while (true) {
            int right = 0;
            for (int i = 1; i < p.size(); ++i) {
                if (rotate(input.get(stack.get(stack.size() - 1)), input.get(p.get(right)), input.get(p.get(i))))
                    right = i;
            }
            if (p.get(right).equals(stack.get(0))) {
                break;
            } else {
                stack.add(p.get(right));
                p.remove(right);
            }
        }
        List<Map.Entry<Double, Double>> output = new ArrayList<>();
        for (Integer i : stack) {
            output.add(input.get(i));
        }
        double[] point = new double[output.size() * 2];
        for (int i = 0; i < output.size(); ++i) {
            point[i * 2] = output.get(i).getKey();
            point[i * 2 + 1] = output.get(i).getValue();
        }
        return point;
    }

    private Map.Entry<Double, Double> calculateIntersectionPair(Line line1, Line line2) {
// параметры отрезков
        double a1 = line1.getStartY() - line1.getEndY();
        double b1 = line1.getEndX() - line1.getStartX();
        double a2 = line2.getStartY() - line2.getEndY();
        double b2 = line2.getEndX() - line2.getStartX();

        double d = a1 * b2 - a2 * b1;
        if (d == 0) {
            return null;
        }
        double c1 = line1.getEndY() * line1.getStartX() - line1.getEndX() * line1.getStartY();
        double c2 = line2.getEndY() * line2.getStartX() - line2.getEndX() * line2.getStartY();

        double x = (b1 * c2 - b2 * c1) / d;
        double y = (a2 * c1 - a1 * c2) / d;
        Map.Entry<Double, Double> res = new AbstractMap.SimpleEntry<>(x, y);
        return res;
    }

    public Shape paintPlaceOfInterest(PlaceOfInterest placeOfInterest) {
        Rectangle building = new Rectangle(
                placeOfInterest.getX(),
                placeOfInterest.getY(),
                placeOfInterest.getWidth(),
                placeOfInterest.getHeight());
        Image img = new Image(getClass().getResource("Images/Building.png").toExternalForm());
        if (img != null) {
            building.setFill(new ImagePattern(img));
        }
        else {
            building.setFill(Paint.valueOf("#101010"));
        }
        return building;
    }

    private double getAngle(double startX, double startY, double endX, double endY) {
        // Получим косинус угла по формуле
        double x = endX - startX;
        double y = endY - startY;
        double cos = x / Math.sqrt(x * x + y * y);
        double angle = Math.acos(cos);
// Вернем arccos полученного значения (в радианах!)
        if (endY < startY) {
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