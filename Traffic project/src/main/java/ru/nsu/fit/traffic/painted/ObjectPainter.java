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
    double vlen = Math.sqrt(vx * vx + vy * vy);
    double size = road.getLanes().size() * LANE_SIZE / vlen;

    if (road.getDirection() == Road.roadDirection.FORWARD) {
      Shape curr =
              new Polygon(
                      vx * size / 2 + pointFromX, vy * size / 2 + pointFromY,
                      pointFromX, pointFromY,
                      pointToX, pointToY,
                      vx * size / 2 + pointToX, vy * size / 2 + pointToY);
      curr.setFill(Paint.valueOf("#aaaaaa"));
      curr.setId(id);
      return curr;
    }
    else{
      Shape curr =
              new Polygon(
                      pointFromX, pointFromY,
                      -vx * size / 2 + pointFromX, -vy * size / 2 + pointFromY,
                      -vx * size / 2 + pointToX, -vy * size / 2 + pointToY,
                      pointToX, pointToY);
      curr.setFill(Paint.valueOf("#bbbbbb"));
      curr.setId(id);
      return curr;
    }
  }

  public Shape paintNode(String id, Node node) {
    Shape shape = new Circle(node.getX(), node.getY(), NODE_SIZE);
    shape.setFill(Paint.valueOf("#ffffff"));
    shape.setId(id);
    return shape;
  }
}
