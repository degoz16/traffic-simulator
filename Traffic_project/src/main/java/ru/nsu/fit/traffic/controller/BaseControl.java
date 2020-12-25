package ru.nsu.fit.traffic.controller;

import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.logic.UpdateObserver;

public abstract class BaseControl {
    protected EditOperationsManager editOperationsManager;
    protected UpdateObserver update;
    protected SceneElementsControl sceneElementsControl;

    public BaseControl(
            SceneElementsControl sceneElementsControl) {
        this.sceneElementsControl = sceneElementsControl;
    }

    public void setUpdate(UpdateObserver update) {
        this.update = update;
    }

    public void setEditOperationsManager(EditOperationsManager editOperationsManager) {
        this.editOperationsManager = editOperationsManager;
    }
}
