package ru.nsu.fit.traffic.view;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;

import java.util.ArrayList;
import java.util.List;

public class GlobalMapObjectPainter {

  public List<Shape> paintRegion(RectRegion region) {
    List<Shape> res = new ArrayList<>();
    Rectangle rect = new Rectangle();
    rect.setStroke(Color.valueOf("#506070"));
    rect.setHeight(region.getHeight());
    rect.setWidth(region.getWidth());
    rect.setX(region.getX());
    rect.setY(region.getY());
    rect.setStrokeWidth(10);
    rect.setFill(Color.valueOf("transparent"));
    for (RoadConnector road : region.getConnectorList()) {
      res.add(paintConnector(road));
    }
    return res;
  }

  public Shape paintConnector(RoadConnector connector) {
    Circle circle = new Circle();
    circle.setRadius(10);
    circle.setCenterX(connector.getX() + connector.getRectRegion().getX());
    circle.setCenterY(connector.getY() + connector.getRectRegion().getY());
    Image img = new Image(getClass().getResource("Images/connector_on_map.png").toExternalForm());
    if (img != null) {
      circle.setFill(new ImagePattern(img));
    } else {
      circle.setFill(Paint.valueOf("#101010"));
    }
    return circle;
  }

  public Shape paintPreFragment(double x, double y){
    Rectangle rect = new Rectangle();
    rect.setX(x);
    rect.setY(y);
    rect.setHeight(1);
    rect.setWidth(1);
    rect.setStrokeWidth(6);
    rect.setFill(Color.valueOf("transparent"));
    rect.setStroke(Color.valueOf("#708090"));
    return rect;
  }
}
