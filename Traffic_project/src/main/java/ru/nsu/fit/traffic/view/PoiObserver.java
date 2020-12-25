package ru.nsu.fit.traffic.view;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.place.PlaceOfInterest;

public interface PoiObserver {
    void action(Shape shape, PlaceOfInterest placeOfInterest);
}
