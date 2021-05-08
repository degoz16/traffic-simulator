package ru.nsu.fit.traffic.view;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;


public class GlobalMapObjectPainter {

  public Rectangle paintRegion(RectRegion region) {
    Rectangle rect = new Rectangle();
    rect.setStroke(Color.valueOf("#A0A0A0"));
    rect.setHeight(region.getHeight());
    rect.setWidth(region.getWidth());
    rect.setX(region.getX());
    rect.setY(region.getY());
    rect.setStrokeWidth(0);
    rect.setFill(Color.valueOf("#808080"));
    return rect;
  }

  public Circle paintConnector(RoadConnector connector, boolean isSet) {
    Circle circle = new Circle();
    if (isSet) {
      circle.setRadius(10);
    }
    else{
      circle.setRadius(8);
    }
    circle.setCenterX(connector.getX());
    circle.setCenterY(connector.getY());
    Image img;
    if (isSet) {
      img = new Image("ru/nsu/fit/traffic/view/Images/connector_on_map.png");
    }else{
      img = new Image("ru/nsu/fit/traffic/view/Images/pre_road_connector.png");
    }
    if (img != null) {
      circle.setFill(new ImagePattern(img));
    } else {
      circle.setFill(Paint.valueOf("#101010"));
    }
    return circle;
  }
}
