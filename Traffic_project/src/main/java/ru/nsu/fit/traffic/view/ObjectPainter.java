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
        //TODO: изменить на полигон.
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
        if (node.getSpawners() != null) {
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
        double cos = x / Math.sqrt(x*x + y*y);
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