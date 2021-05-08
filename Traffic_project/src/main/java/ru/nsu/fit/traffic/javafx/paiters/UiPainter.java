package ru.nsu.fit.traffic.javafx.paiters;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.w3c.dom.css.Rect;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;

public class UiPainter {
  private static final Color correctFragment = Color.valueOf("#656565");
  public static final Color incorrectFragment = Color.valueOf("#FF5555");

  public static Rectangle getSelectRect() {
    Rectangle selectRect = new Rectangle(0, 0, 0, 0);
    selectRect.setFill(Color.TRANSPARENT);
    selectRect.setStroke(Color.valueOf("#656565"));
    selectRect.setStrokeWidth(4);
    String style =
        "-fx-stroke-width: 7;"
            + "-fx-stroke-dash-array: 12 2 4 2;"
            + "-fx-stroke-dash-offset: 6;"
            + "-fx-stroke-line-cap: butt;";
    selectRect.setStyle(style);
    return selectRect;
  }

  public static Circle getConnectorIcon() {
    Circle circle = new Circle();

    circle.setRadius(8);

    // Image img = new Image(getResource("Images/pre_road_connector.png").toExternalForm());
    Image img = new Image("ru/nsu/fit/traffic/view/Images/pre_road_connector.png");
    if (img != null) {
      circle.setFill(new ImagePattern(img));
    } else {
      circle.setFill(Paint.valueOf("#101010"));
    }
    return circle;
  }

  public static void removeSelectRect(Rectangle selectRect, Pane mainPane) {
    mainPane.getChildren().remove(selectRect);
  }

  public static void addSelectRect(double x, double y, Rectangle selectRect, Pane mainPane) {
    selectRect.setX(x);
    selectRect.setY(y);
    if (selectRect.getStroke() == incorrectFragment) return;
    mainPane.getChildren().add(selectRect);
  }

  public static void resizeSelectRect(double x, double y, Rectangle selectRect) {
    double width = x - selectRect.getX();
    double height = y - selectRect.getY();
    if (width < 0) {
      selectRect.setTranslateX(width);
      selectRect.setWidth(-width);
    } else {
      selectRect.setTranslateX(0);
      selectRect.setWidth(width);
    }
    if (height < 0) {
      selectRect.setTranslateY(height);
      selectRect.setHeight(-height);
    } else {
      selectRect.setTranslateY(0);
      selectRect.setHeight(height);
    }
  }


  public static void checkResizeSelectedRect(
      double x, double y, Rectangle selectRect, RegionsMap map) {
    resizeSelectRect(x, y, selectRect);
    selectRect.setStroke(correctFragment);
    for (int i = 0; i < map.getRegionCount(); ++i) {
      RectRegion reg = map.getRegion(i);
      Rectangle rect = new Rectangle(reg.getX(), reg.getY(), reg.getWidth(), reg.getHeight());

      if (selectRect.intersects(
          reg.getX() - selectRect.getTranslateX(),
          reg.getY() - selectRect.getTranslateY(),
          reg.getWidth(),
          reg.getHeight())) {
        selectRect.setStroke(incorrectFragment);
        return;
      }
    }
  }

  public static void removeConnectorIcon(Shape icon, Pane mainPane) {
    mainPane.getChildren().remove(icon);
  }

  public static void addConnectorIcon(double x, double y, Circle icon, Pane mainPane) {
    rePosConnectorIcon(x, y, icon);
    mainPane.getChildren().add(icon);
  }

  public static void rePosConnectorIcon(double x, double y, Circle icon) {
    icon.setCenterX(x);
    icon.setCenterY(y);
  }
}
