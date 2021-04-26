package ru.nsu.fit.traffic.view.elements.observers;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;

public interface RegionObserver {
  void action(Shape shape, RectRegion region);
}
