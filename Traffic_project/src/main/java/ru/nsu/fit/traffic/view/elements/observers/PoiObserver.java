package ru.nsu.fit.traffic.view.elements.observers;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.map.PlaceOfInterest;

public interface PoiObserver {
    void setPoiClickHandler(Shape shape, int placeOfInterestId);
}
