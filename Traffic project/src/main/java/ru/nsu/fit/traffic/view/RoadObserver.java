package ru.nsu.fit.traffic.view;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.road.Road;

import java.util.List;

public interface RoadObserver {
    void action(Road road, List<List<Shape>> roadShape);
}
