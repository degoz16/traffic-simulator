package ru.nsu.fit.traffic.controller.settings;

import ru.nsu.fit.traffic.controller.BaseControl;
import ru.nsu.fit.traffic.controller.SceneElementsControl;
import ru.nsu.fit.traffic.controller.edit.EditControl;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.logic.UpdateObserver;
import ru.nsu.fit.traffic.model.node.Spawner;
import java.util.ArrayList;

public class NodeSettingsControl extends BaseControl implements NodeSettingsControlInterface {
    private final EditControl editControl;

    public NodeSettingsControl(
            SceneElementsControl sceneElementsControl,
            EditControl editControl) {
        super(sceneElementsControl);
        this.editControl = editControl;
    }

    public void confirmSpawnerCreationAction(String startTime, String endTime, int spawnRate) {
        if (editControl.getLastNodeClicked().getSpawners() == null) {
            editControl.getLastNodeClicked().setSpawners(new ArrayList<>());
        }
        editControl.getLastNodeClicked().getSpawners().add(
                new Spawner(startTime, endTime, spawnRate));
        sceneElementsControl.nodeSettingsSetVisible(false);
        update.update(editOperationsManager);
    }
}
