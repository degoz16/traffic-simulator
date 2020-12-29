package ru.nsu.fit.traffic.interfaces.control;

import ru.nsu.fit.traffic.controller.edit.EditControl;
import ru.nsu.fit.traffic.controller.saveload.SaveLoadControl;
import ru.nsu.fit.traffic.controller.settings.BuildingSettingsControl;
import ru.nsu.fit.traffic.controller.settings.NodeSettingsControl;
import ru.nsu.fit.traffic.controller.settings.RoadSettingsControl;
import ru.nsu.fit.traffic.controller.settings.TrafficLightSettingsControl;
import ru.nsu.fit.traffic.model.logic.UpdateObserver;

public interface ControlInitializerInterface {
    EditControl getEditControl();
    SaveLoadControl getSaveLoadControl();
    BuildingSettingsControl getBuildingSettingsControl();
    NodeSettingsControl getNodeSettingsControl();
    RoadSettingsControl getRoadSettingsControl();
    TrafficLightSettingsControl getTrafficLightSettingsControl();
    void initialize(UpdateObserver updateObserver);
}
