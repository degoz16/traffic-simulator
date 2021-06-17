package ru.nsu.fit.traffic.view.elements.observers;

import javafx.scene.shape.Shape;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;

public interface ConnectorObserver {
    void setConnectorObserver(int regId, int conId, Shape connectorShape);
}
