package ru.nsu.fit.traffic.view;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.node.Node;

public interface NodeObserver {
    void action(Shape shape, Node node);
}
