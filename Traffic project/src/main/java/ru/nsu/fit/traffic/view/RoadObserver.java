package ru.nsu.fit.traffic.view;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.road.Road;

public interface RoadObserver {
    void action(Road road, Shape roadShape, int i);
}
