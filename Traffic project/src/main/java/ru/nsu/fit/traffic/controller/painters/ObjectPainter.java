package ru.nsu.fit.traffic.controller.painters;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.PlaceOfInterest;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.trafficsign.RoadSign;

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
                            double x = (pointToX + i * vx + vx / 2 - vy * 2 + vx);
                            double y = (pointToY + i * vy + vy / 2 + vx * 2 + vy);

                            Polygon shape = new Polygon(
                                    x - 4, y,
                                    x, y + 4,
                                    x + 4, y,
                                    x, y - 4);
                            shape.setFill(Color.YELLOW);
                            shape.setStroke(Color.WHITE);
                            shape.setStrokeWidth(2);
                            roadGroup.add(shape);
                    }

                }
            }
            paintedRoad.add(roadGroup);
        }
        return paintedRoad;
    }

    public Shape paintRoadLight(Road road, boolean isGreen) {
        List<Shape> list = new ArrayList<>();
        Shape curr;
        int pointFromX = (int)road.getFrom().getX();
        int pointFromY = (int)road.getFrom().getY();
        int pointToX = (int)road.getTo().getX();
        int pointToY = (int)road.getTo().getY();

        double vx = pointFromY - pointToY;
        double vy = -pointFromX + pointToX;

        double vlen = Math.sqrt(Math.abs(vx * vx + vy * vy));
        List<List<Shape>> paintedRoad = new ArrayList<>();

        vx *= LANE_SIZE / vlen;
        vy *= LANE_SIZE / vlen;

        curr = new Polygon(
                vx + pointFromX,
                vy + pointFromY,
                pointFromX, pointFromY,
                pointToX, pointToY,
                vx*road.getLanesNum() + pointToX,
                vy*road.getLanesNum() + pointToY);
        curr.setFill(Paint.valueOf("transparent"));
        curr.setStrokeWidth(3);
        if (isGreen){
            curr.setStroke(Paint.valueOf("#00a550"));
        } else{
            curr.setStroke(Paint.valueOf("#eb003b"));
        }
        return curr;
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
        if (node.getSpawner() != null ) {
            if (node.getTrafficLight() == null) {
                Image img = new Image(getClass().getResource("../../view/Images/spawner.png").toExternalForm());
                shape.setFill(new ImagePattern(img));
            }
            else {
                Image img = new Image(getClass().getResource("../../view/Images/spawner_trafficlight.png").toExternalForm());
                shape.setFill(new ImagePattern(img));
            }
        } else if (node.getTrafficLight() != null) {
            Image img = new Image(getClass().getResource("../../view/Images/trafficlight.png").toExternalForm());
            shape.setFill(new ImagePattern(img));
        }
        return shape;
    }

    public Rectangle paintSelectRect() {
        Rectangle selectRect = new Rectangle(0, 0, 0, 0);
        selectRect.setFill(Color.TRANSPARENT);
        selectRect.setStroke(Color.valueOf("#656565"));
        selectRect.setStrokeWidth(4);
        selectRect.setStyle("{" +
                "-fx-stroke-width: 7;" +
                "-fx-stroke-dash-array: 12 2 4 2;" +
                "-fx-stroke-dash-offset: 6;" +
                "-fx-stroke-line-cap: butt;" +
                "}");
        return selectRect;
    }

    public Shape paintPlaceOfInterest(PlaceOfInterest placeOfInterest) {
        Rectangle building = new Rectangle(
                placeOfInterest.getX(),
                placeOfInterest.getY(),
                placeOfInterest.getWidth(),
                placeOfInterest.getHeight());
        building.setFill(Color.valueOf("#bbbbbb"));
        building.setStroke(Color.valueOf("#656565"));
        building.setStrokeWidth(4);
        building.setStyle("{" +
                "-fx-stroke-width: 7;" +
                "-fx-stroke-dash-array: 12 2 4 2;" +
                "-fx-stroke-dash-offset: 6;" +
                "-fx-stroke-line-cap: butt;" +
                "}");
        return building;
    }
}