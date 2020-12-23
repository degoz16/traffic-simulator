package ru.nsu.fit.traffic.view;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.PlaceOfInterest;

public interface PoiObserver {
    void action(PlaceOfInterest placeOfInterest, Shape placeOfInterestShape);
}
