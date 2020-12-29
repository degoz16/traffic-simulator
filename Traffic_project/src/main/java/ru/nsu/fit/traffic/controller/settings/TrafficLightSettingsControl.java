package ru.nsu.fit.traffic.controller.settings;

import ru.nsu.fit.traffic.controller.BaseControl;
import ru.nsu.fit.traffic.controller.SceneElementsControl;
import ru.nsu.fit.traffic.controller.edit.EditControl;
import ru.nsu.fit.traffic.interfaces.control.TrafficLightSettingsControlInterface;

public class TrafficLightSettingsControl extends BaseControl implements TrafficLightSettingsControlInterface {

    private final EditControl editControl;

    public TrafficLightSettingsControl(
            SceneElementsControl sceneElementsControl,
            EditControl editControl) {
        super(sceneElementsControl);
        this.editControl = editControl;
    }

    public void closeSettings(){
        sceneElementsControl.trafficLightSettingsSetVisible(false);
        editOperationsManager.resetRoadsHighLight(editControl.getLastNodeClicked());
        update.update(editOperationsManager);
    }
    public void confirmSettings(int greenDelay, int redDelay){
        sceneElementsControl.trafficLightSettingsSetVisible(false);
        editOperationsManager.resetRoadsHighLight(editControl.getLastNodeClicked());
        editOperationsManager.applyTrafficLightSettings(
                editControl.getLastNodeClicked(),
                greenDelay,
                redDelay
        );
    }
}
