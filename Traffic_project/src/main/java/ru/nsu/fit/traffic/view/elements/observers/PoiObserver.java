package ru.nsu.fit.traffic.view.elements.observers;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.map.PlaceOfInterest;

public interface PoiObserver {
    void action(Shape shape, PlaceOfInterest placeOfInterest);
}
