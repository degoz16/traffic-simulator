package ru.nsu.fit.traffic.view.elements.observers;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.map.Road;

public interface RoadObserver {
    void action(Shape shape, Road road, int i);
}
