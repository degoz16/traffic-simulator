package ru.nsu.fit.traffic.javafx.controller.edit;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.nsu.fit.traffic.controller.ControlsInitializer;
import ru.nsu.fit.traffic.controller.edit.EditControl;
import ru.nsu.fit.traffic.controller.SceneElementsControl;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapperButton;

import ru.nsu.fit.traffic.javafx.controller.menubar.MenuBarController;
import ru.nsu.fit.traffic.javafx.controller.settings.*;
import ru.nsu.fit.traffic.javafx.controller.statistic.StatisticsController;
import ru.nsu.fit.traffic.view.ViewUpdater;

/**
 * Контроллер основной сцены, на которой располагаются все остальные.
 */
public class MainController {

    @FXML
    private Pane statistics;
    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private Pane mainPane;
    @FXML
    private AnchorPane basePane;
    @FXML
    private Pane numberOfLanesPane;
    @FXML
    private Pane roadSignPane;
    @FXML
    private TextField backLanesTextField;
    @FXML
    private TextField forwardLanesTextField;
    @FXML
    private ComboBox<Integer> speedComboBox;
    @FXML
    private Group scrollPaneContent;
    @FXML
    private VBox centeredField;
    @FXML
    private BuildingController buildingSettingsController;
    @FXML
    private TrafficLightController trafficLightController;
    @FXML
    private MenuBarController menuBarController;
    @FXML
    private RoadSettingsController roadSettingsController;
    @FXML
    private StatisticsController statisticsController;
    @FXML
    private NodeSettingsController nodeSettingsController;
    @FXML
    private Slider timeLineSlider;

    private Stage stage;

    private double scaleValue = 1;
    private Rectangle selectRect;
    private ControlsInitializer controlsInitializer;
    private EditControl editControl;
    private SceneElementsControl sceneElementsControl = new SceneElementsControl() {
        @Override
        public void updateStatistic(int carSpawnersCnt, int streetsCnt, int roadsCnt, int buildingCnt, int connectivity, List<String> streets) {
            statisticsController.getCarSpawners().setText(String.valueOf(carSpawnersCnt));
            statisticsController.getStreets().setText(String.valueOf(streetsCnt));
            statisticsController.getRoads().setText(String.valueOf(roadsCnt));
            statisticsController.getBuildings().setText(String.valueOf(buildingCnt));
            statisticsController.getConnectivity().setText(String.valueOf(connectivity));
            streets.forEach(s -> {
                Text text = new Text(s);
                text.setFill(Paint.valueOf("#ffffff"));
                statisticsController.getStreetsView().getChildren().add(text);
            });
        }

        @Override
        public void removeSelectRect() {
            mainPane.getChildren().remove(selectRect);
        }

        @Override
        public void addSelectRect(double x, double y) {
            selectRect.setX(x);
            selectRect.setY(y);
            mainPane.getChildren().add(selectRect);
        }

        @Override
        public void resizeSelectRect(double x, double y) {
            double width = x - selectRect.getX();
            double height = y - selectRect.getY();
            if (width < 0) {
                selectRect.setTranslateX(width);
                selectRect.setWidth(-width);
            } else {
                selectRect.setTranslateX(0);
                selectRect.setWidth(width);
            }
            if (height < 0) {
                selectRect.setTranslateY(height);
                selectRect.setHeight(-height);
            } else {
                selectRect.setTranslateY(0);
                selectRect.setHeight(height);
            }
        }

        @Override
        public void buildingSettingsSetVisible(boolean status) {
            buildingSettingsController.getPane().setVisible(status);
        }

        @Override
        public void buildingSettingsSetPos(double x, double y) {
            buildingSettingsController.getPane().setLayoutX(x);
            buildingSettingsController.getPane().setLayoutY(y);
        }

        @Override
        public void buildingSettingsSetParams(double weight, int parkingPlaces) {
            buildingSettingsController.getSlider().setValue(weight);
            buildingSettingsController.getParkingPlaces().setText
                    (String.valueOf(parkingPlaces));
        }

        @Override
        public void roadSettingsSetVisible(boolean status) {
            roadSettingsController.getRoadSettingsPane().setVisible(status);
        }

        @Override
        public void roadSettingsSetPos(double x, double y) {
            roadSettingsController.getRoadSettingsHelperPane().setLayoutX(x);
            roadSettingsController.getRoadSettingsHelperPane().setLayoutY(y);
        }

        @Override
        public void roadSettingsSetParams(int lanesNum, String street) {
            roadSettingsController.getLanesTextField().setText(String.valueOf(lanesNum));
            roadSettingsController.getStreetName().setText(street);
        }

        @Override
        public void roadSignMenuSetVisible(boolean status) {
            roadSignPane.setVisible(status);
        }

        @Override
        public void nodeSettingsSetVisible(boolean status) {
            nodeSettingsController.getNodeSettingPane().setVisible(status);
        }

        @Override
        public void nodeSettingsSetPos(double x, double y) {
            nodeSettingsController.getNodeSettingPane().setLayoutX(x);
            nodeSettingsController.getNodeSettingPane().setLayoutY(y);
        }

        @Override
        public void nodeSettingsSetParams(LocalTime start, LocalTime end, int spawnRate) {
            nodeSettingsController.getStartTime().setValue(start);
            nodeSettingsController.getEndTime().setValue(end);
            nodeSettingsController.getSpawnerRate().setText(String.valueOf(spawnRate));
        }

        @Override
        public void numberOfLanesPaneSetVisible(boolean status) {
            numberOfLanesPane.setVisible(status);
        }

        @Override
        public void trafficLightSettingsSetVisible(boolean status) {
            trafficLightController.getTrafficLightPane().setVisible(status);
        }

        @Override
        public void trafficLightSettingsSetPos(double x, double y) {
            trafficLightController.getTrafficLightPane().setLayoutX(x);
            trafficLightController.getTrafficLightPane().setLayoutY(y);
        }

        @Override
        public void trafficLightSettingsSetParams(int greenDelay, int redDelay) {
            trafficLightController.getGreenDelay().setText(String.valueOf(greenDelay));
            trafficLightController.getRedDelay().setText(String.valueOf(redDelay));
        }

        @Override
        public void statisticSwitchVisible() {
            statistics.setVisible(!statistics.isVisible());
        }

        @Override
        public void timeLineSliderInit(int windowsListSize, Function<Integer, Long> endGetter) {
            //timeLineSlider.setMax(Math.max(reportStruct.getWindowList().size() - 1, 0));
            timeLineSlider.setMax(Math.max(windowsListSize - 1, 0));
            timeLineSlider.setLabelFormatter(new StringConverter<>() {
                @Override
                public String toString(Double object) {
                    int id = (int) Math.round(object);
                    if (id < windowsListSize) {
                        return String.valueOf(endGetter.apply(id));
                        //todo отображение нормального времени
                    }
                    return "";
                }

                @Override
                public Double fromString(String string) {
                    return null;
                }
            });
        }

        @Override
        public void timeLineSliderSetMax(double max) {
            timeLineSlider.setMax(max);
        }
    };

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Rectangle getSelectRect() {
        Rectangle selectRect = new Rectangle(0, 0, 0, 0);
        selectRect.setFill(Color.TRANSPARENT);
        selectRect.setStroke(Color.valueOf("#656565"));
        selectRect.setStrokeWidth(4);
        StringBuilder style = new StringBuilder();
        style
                .append("{")
                .append("-fx-stroke-width: 7;")
                .append("-fx-stroke-dash-array: 12 2 4 2;")
                .append("-fx-stroke-dash-offset: 6;")
                .append("-fx-stroke-line-cap: butt;")
                .append("}");
        selectRect.setStyle(style.toString());
        return selectRect;
    }

    private MouseEventWrapperButton getWrapperButton(MouseEvent event) {
        switch (event.getButton()) {
            case NONE -> {
                return MouseEventWrapperButton.NONE;
            }
            case PRIMARY -> {
                return MouseEventWrapperButton.PRIMARY;
            }
            case SECONDARY -> {
                return MouseEventWrapperButton.SECONDARY;
            }
        }
        return MouseEventWrapperButton.NONE;
    }

    private MouseEventWrapper getMouseEventWrapper(MouseEvent event) {
        return new MouseEventWrapper(
                event.getX(), event.getY(), getWrapperButton(event)) {
            @Override
            public void consume() {
                event.consume();
            }
        };
    }

    /**
     * инициализация.
     */
    @FXML
    public void initialize() {
        controlsInitializer = new ControlsInitializer(sceneElementsControl);
        editControl = controlsInitializer.getEditControl();
        ViewUpdater viewUpdater = new ViewUpdater(
                (shape, placeOfInterest) ->
                        shape.setOnMouseClicked(event ->
                                editControl
                                        .onPoiClicked(placeOfInterest, getMouseEventWrapper(event))),
                (shape, road, i) ->
                        shape.setOnMouseClicked(event ->
                                editControl
                                        .onRoadClick(road, i, getMouseEventWrapper(event))),
                (shape, node) ->
                        shape.setOnMouseClicked(event ->
                                editControl
                                        .onNodeClick(node, getMouseEventWrapper(event))),
                mainPane
        );
        controlsInitializer.initialize(viewUpdater::updateMapView);
        //menuBarController.setMap(currMap);
        menuBarController.setStage(stage);

        selectRect = getSelectRect();
        trafficLightController.setTrafficLightSettingsControl(
                controlsInitializer.getTrafficLightSettingsControl()
        );
        roadSettingsController.setRoadSettingsControl(
                controlsInitializer.getRoadSettingsControl()
        );
        nodeSettingsController.setNodeSettingsControl(
                controlsInitializer.getNodeSettingsControl()
        );
        buildingSettingsController.setBuildingSettingsControl(
                controlsInitializer.getBuildingSettingsControl()
        );
        menuBarController.setSaveLoadControl(
                controlsInitializer.getSaveLoadControl()
        );

        statistics.setVisible(false);
        numberOfLanesPane.setVisible(false);
        buildingSettingsController.getPane().setVisible(false);
        roadSettingsController.getRoadSettingsPane().setVisible(false);
        trafficLightController.getTrafficLightPane().setVisible(false);
        nodeSettingsController.getNodeSettingPane().setVisible(false);
        roadSignPane.setVisible(false);
        speedComboBox.setItems(FXCollections.observableArrayList(20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120));
        speedComboBox.setValue(60);

        mainScrollPane.setPannable(false);

        timeLineSlider.setMin(0);

        timeLineSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                editControl.onTimeLineSliderChange(newValue));

        mainScrollPane.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.MIDDLE) mainScrollPane.setPannable(true);
        });
        mainScrollPane.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.MIDDLE) mainScrollPane.setPannable(false);
        });

        centeredField.setOnScroll(event -> {
            zoom(event);
            event.consume();
        });

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            int text_size = change.getControlNewText().length();

            if (input.matches("[0-5]*") && text_size <= 1) {
                return change;
            }
            return null;
        };

        backLanesTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        backLanesTextField.setText("1");
        backLanesTextField.textProperty().addListener((observable, oldValue, newValue) ->
                editControl.onRightLanesTextFieldChange(newValue));

        roadSettingsController.getLanesTextField().setTextFormatter(new TextFormatter<String>(integerFilter));
        roadSettingsController.getLanesTextField().setText("1");

        forwardLanesTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        forwardLanesTextField.setText("1");
        forwardLanesTextField.textProperty().addListener((observable, oldValue, newValue) ->
                editControl.onLeftLanesTextFieldChange(newValue));

        mainPane.setOnMousePressed(event ->
                editControl.onMainPanePressed(getMouseEventWrapper(event)));
        mainPane.setOnMouseClicked(event ->
                editControl.onMainPaneClicked(getMouseEventWrapper(event)));
        mainPane.setOnMouseDragged(event ->
                editControl.onMainPaneDrag(getMouseEventWrapper(event)));
        mainPane.setOnMouseReleased(event ->
                editControl.onMainPaneReleased(getMouseEventWrapper(event)));

        basePane.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
                editControl.onBasePaneClickedFilter(
                        getMouseEventWrapper(event),
                        basePane.getWidth(),
                        basePane.getHeight(),
                        100,100
                ));

    }

    @FXML
    public void startSimulation() {
        editControl.startSimulation();
    }

    @FXML
    public void pauseSimulation() {
        editControl.pauseSimulation();
    }

    @FXML
    public void stopSimulation() {
        editControl.stopSimulation();
    }

    @FXML
    public void roadButtonClicked() {
        editControl.roadButtonClicked();
    }

    @FXML
    public void trafficLightButtonClicked() {
        editControl.trafficLightButtonClicked();
    }

    @FXML
    public void buildingButtonClicked() {
        editControl.buildingButtonClicked();
    }

    @FXML
    public void carButtonClicked() {
        editControl.carButtonClicked();
    }

    @FXML
    public void roadSignButtonClicked() {
        editControl.roadSignButtonClicked();
    }

    @FXML
    public void showStatistic() {
        //statistics.setVisible(!statistics.isVisible());
        editControl.showStatistic();
    }

    @FXML
    public void setSpeedSign() {
        editControl.setSpeedSign(speedComboBox.getValue());
    }

    @FXML
    public void setMainRoad() {
        editControl.setMainRoad();
    }

    private void zoom(ScrollEvent event) {
        double zoomFactor = Math.exp(event.getDeltaY() * 0.001);

        Bounds innerBounds = scrollPaneContent.getLayoutBounds();
        Bounds viewportBounds = mainScrollPane.getViewportBounds();

        double valX = mainScrollPane.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = mainScrollPane.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        scaleValue = scaleValue * zoomFactor;
        mainPane.setScaleX(scaleValue);
        mainPane.setScaleY(scaleValue);
        mainScrollPane.layout();

        Point2D posInZoomTarget = mainPane
                .parentToLocal(scrollPaneContent
                        .parentToLocal(new Point2D(
                                event.getX(),
                                event.getY())));

        Point2D adjustment = mainPane.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        Bounds updatedInnerBounds = scrollPaneContent.getBoundsInLocal();
        mainScrollPane.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        mainScrollPane.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }


}