package ru.nsu.fit.traffic.controller.settings;

import ru.nsu.fit.traffic.controller.BaseControl;
import ru.nsu.fit.traffic.controller.SceneElementsControl;
import ru.nsu.fit.traffic.controller.edit.EditControl;
import ru.nsu.fit.traffic.controller.statistic.StatisticControl;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.logic.UpdateObserver;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.road.Street;

public class RoadSettingsControl extends BaseControl {
    private final EditControl editControl;
    private final StatisticControl statisticControl;
    public RoadSettingsControl(
            SceneElementsControl sceneElementsControl,
            EditControl editControl,
            StatisticControl statisticControl) {
        super(sceneElementsControl);
        this.editControl = editControl;
        this.statisticControl = statisticControl;
    }

    public void deleteRoad() {
        Road lastRoadClicked = editControl.getLastRoadClicked();
        if (lastRoadClicked.getBackRoad().getLanesNum() == 0) {
            if (lastRoadClicked.getFrom().getRoadsOutNum() <= 1) {
                lastRoadClicked.getFrom().removeFromPlaceOfInterest();
                editOperationsManager.getMap().removeNode(lastRoadClicked.getFrom());
            }
            if (lastRoadClicked.getTo().getRoadsInNum() <= 1) {
                lastRoadClicked.getTo().removeFromPlaceOfInterest();
                editOperationsManager.getMap().removeNode(lastRoadClicked.getTo());
            }
            lastRoadClicked.getBackRoad().disconnect();
            editOperationsManager.getMap().removeRoad(lastRoadClicked.getBackRoad());
            lastRoadClicked.disconnect();
            editOperationsManager.getMap().removeRoad(lastRoadClicked);
        } else {
            lastRoadClicked.clearLanes();
        }
        lastRoadClicked.getTo().setTrafficLight(null);
        update.update(editOperationsManager);
        statisticControl.updateStatistics();
    }

    public void confirmRoadSettings(int newLanesNum, String streetName) {
        Road lastRoadClicked = editControl.getLastRoadClicked();

        int oldLanesNum = lastRoadClicked.getLanesNum();
        //int newLanesNum = Integer.parseInt(lanesTextField.getText());
        if (newLanesNum > 0) {
            if (newLanesNum < oldLanesNum) {
                for (int i = newLanesNum; i < oldLanesNum; i++) {
                    lastRoadClicked.removeLane(i);
                }
            } else {
                for (int i = oldLanesNum; i < newLanesNum; i++) {
                    lastRoadClicked.addLane(i);
                }
            }
            update.update(editOperationsManager);
        } else {
            deleteRoad();
            return;
        }
        if (streetName.equals("") && lastRoadClicked.getStreet() != null) {
            lastRoadClicked.disconnectWithStreet();
        } else if (!streetName.equals("")) {
            if (lastRoadClicked.getStreet() != null && !streetName.equals(lastRoadClicked.getStreet().getName())) {
                lastRoadClicked.disconnectWithStreet();
            }
            Street currStreet = null;
            for (Street s : editOperationsManager.getMap().getStreets()) {
                if (s.getName().equals(streetName)) {
                    currStreet = s;
                    break;
                }
            }
            if (currStreet == null) {
                currStreet = new Street(streetName);
                editOperationsManager.getMap().getStreets().add(currStreet);
            }
            if (lastRoadClicked.getBackRoad() != null) {
                lastRoadClicked.getBackRoad().disconnectWithStreet();
                currStreet.addRoad(lastRoadClicked.getBackRoad());
                lastRoadClicked.getBackRoad().setCurrStreet(currStreet);
            }
            currStreet.addRoad(lastRoadClicked);
            lastRoadClicked.setCurrStreet(currStreet);
            // System.out.println(currStreet);
        }
        sceneElementsControl.roadSettingsSetVisible(false);
        update.update(editOperationsManager);
        statisticControl.updateStatistics();
    }
}
