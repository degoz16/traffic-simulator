package ru.nsu.fit.traffic.model;

import ru.nsu.fit.traffic.model.logic.EditOperationsManager;

public interface UpdateObserver {
    void update(EditOperationsManager editOperationsManager);
}
