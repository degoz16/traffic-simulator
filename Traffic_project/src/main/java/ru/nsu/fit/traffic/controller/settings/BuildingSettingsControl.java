package ru.nsu.fit.traffic.controller.settings;

import ru.nsu.fit.traffic.controller.BaseControl;
import ru.nsu.fit.traffic.controller.SceneElementsControl;
import ru.nsu.fit.traffic.controller.edit.EditControl;
import ru.nsu.fit.traffic.controller.statistic.StatisticControl;
import ru.nsu.fit.traffic.interfaces.control.BuildingSettingsControlInterface;

public class BuildingSettingsControl extends BaseControl implements BuildingSettingsControlInterface {
    private final StatisticControl statisticControl;
    private final EditControl editControl;
    public BuildingSettingsControl(
            SceneElementsControl sceneElementsControl,
            EditControl editControl,
            StatisticControl statisticControl) {
        super(sceneElementsControl);
        this.editControl = editControl;
        this.statisticControl = statisticControl;
    }

    public void closeSettings() {
        sceneElementsControl.buildingSettingsSetVisible(false);
    }

    public void confirmSettings(double weight, int parkingPlaces) {
        sceneElementsControl.buildingSettingsSetVisible(false);
        editControl.getLastPOIClicked().setWeight(weight);
        editControl.getLastPOIClicked().setNumberOfParkingPlaces(parkingPlaces);
        statisticControl.updateStatistics();
    }

    public void deleteBuilding() {
        editOperationsManager.getMap().removePOI(editControl.getLastPOIClicked());
        update.update(editOperationsManager);
        sceneElementsControl.buildingSettingsSetVisible(false);
        statisticControl.updateStatistics();
    }
}
