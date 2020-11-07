package ru.nsu.fit.dzaikov.traffic.painted;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import ru.nsu.fit.dzaikov.traffic.model.Road;

import java.util.ArrayList;
import java.util.List;

public class PaintedRoad {
    public static final int LANE_SIZE = 15;

    public static List<Shape> getShape(Road road) {
        List<Shape> list = new ArrayList<>();
        Shape curr;
        int pointFromX = road.getFrom().getX();
        int pointFromY = road.getFrom().getY();
        int pointToX = road.getTo().getX();
        int pointToY = road.getTo().getY();

        //формула прямой y = ax + b
        double a = (double) (pointFromY - pointToY) / (pointToX - pointFromX);
        double b = (double) (pointFromX * pointToY - pointFromY * pointToX) / (pointToX - pointFromX);

        // направляющий вектор перпендикуляра
        double vx = pointFromY - pointToY;
        double vy = -pointFromX + pointToX;
        double vlen = Math.sqrt(vx*vx + vy*vy);
        double size = road.getLanes().size() * LANE_SIZE / vlen;

        // длинна дороги
        // todo: пока рисую посерединке, всё равно число полос константное
        curr = new Polygon(
                vx * size/2 + pointFromX, vy * size/2 + pointFromY,
                -vx * size/2 + pointFromX, -vy * size/2 + pointFromY,
                -vx * size/2 + pointToX, -vy * size/2 + pointToY,
                vx * size/2 + pointToX, vy * size/2 + pointToY
        );
        curr.setFill(Paint.valueOf("#aaaaaa"));
        list.add(curr);
        return list;
    }
}
