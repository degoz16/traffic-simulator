package ru.nsu.fit.traffic.controller;

import ru.nsu.fit.traffic.controller.edit.EditControl;
import ru.nsu.fit.traffic.controller.saveload.SaveLoadControl;
import ru.nsu.fit.traffic.controller.settings.BuildingSettingsControl;
import ru.nsu.fit.traffic.controller.settings.NodeSettingsControl;
import ru.nsu.fit.traffic.controller.settings.RoadSettingsControl;
import ru.nsu.fit.traffic.controller.settings.TrafficLightSettingsControl;
import ru.nsu.fit.traffic.controller.statistic.StatisticControl;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.logic.UpdateObserver;

public class ControlsInitializer {
    private EditControl editControl;
    private SaveLoadControl saveLoadControl;
    private BuildingSettingsControl buildingSettingsControl;
    private NodeSettingsControl nodeSettingsControl;
    private RoadSettingsControl roadSettingsControl;
    private TrafficLightSettingsControl trafficLightSettingsControl;
    private StatisticControl statisticControl;

    public ControlsInitializer(SceneElementsControl sceneElementsControl) {
        statisticControl = new StatisticControl(
                sceneElementsControl
        );
        editControl = new EditControl(
                sceneElementsControl,
                statisticControl
        );
        saveLoadControl = new SaveLoadControl(
                sceneElementsControl
        );
        buildingSettingsControl = new BuildingSettingsControl(
                sceneElementsControl,
                editControl,
                statisticControl
        );
        nodeSettingsControl = new NodeSettingsControl(
                sceneElementsControl,
                editControl
        );
        roadSettingsControl = new RoadSettingsControl(
                sceneElementsControl,
                editControl,
                statisticControl
        );
        trafficLightSettingsControl = new TrafficLightSettingsControl(
                sceneElementsControl,
                editControl
        );
    }

    public void initialize(UpdateObserver updateObserver) {
        EditOperationsManager editOperationsManager = new EditOperationsManager(updateObserver);
        editControl.setUpdate(updateObserver);
        saveLoadControl.setUpdate(updateObserver);
        buildingSettingsControl.setUpdate(updateObserver);
        nodeSettingsControl.setUpdate(updateObserver);
        roadSettingsControl.setUpdate(updateObserver);
        trafficLightSettingsControl.setUpdate(updateObserver);
        statisticControl.setUpdate(updateObserver);
        editControl.setEditOperationsManager(editOperationsManager);
        saveLoadControl.setEditOperationsManager(editOperationsManager);
        buildingSettingsControl.setEditOperationsManager(editOperationsManager);
        nodeSettingsControl.setEditOperationsManager(editOperationsManager);
        roadSettingsControl.setEditOperationsManager(editOperationsManager);
        trafficLightSettingsControl.setEditOperationsManager(editOperationsManager);
        statisticControl.setEditOperationsManager(editOperationsManager);
    }
    public EditControl getEditControl() {
        return editControl;
    }

    public SaveLoadControl getSaveLoadControl() {
        return saveLoadControl;
    }

    public BuildingSettingsControl getBuildingSettingsControl() {
        return buildingSettingsControl;
    }

    public NodeSettingsControl getNodeSettingsControl() {
        return nodeSettingsControl;
    }

    public RoadSettingsControl getRoadSettingsControl() {
        return roadSettingsControl;
    }

    public TrafficLightSettingsControl getTrafficLightSettingsControl() {
        return trafficLightSettingsControl;
    }
}
