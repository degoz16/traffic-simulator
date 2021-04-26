package ru.nsu.fit.traffic.view.elements.observers;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.map.Node;

public interface NodeObserver {
    void action(Shape shape, Node node);
}
