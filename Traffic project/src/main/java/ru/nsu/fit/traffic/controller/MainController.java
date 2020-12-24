package ru.nsu.fit.traffic.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.time.LocalTime;
import java.util.List;
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
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.nsu.fit.traffic.model.PlaceOfInterest;
import ru.nsu.fit.traffic.model.map.TrafficMap;
import ru.nsu.fit.traffic.model.congestion.ReportStruct;
import ru.nsu.fit.traffic.model.congestion.ReportWindowStruct;
import ru.nsu.fit.traffic.model.logic.EditOperation;
import ru.nsu.fit.traffic.model.logic.EditOperationsManager;
import ru.nsu.fit.traffic.model.node.Node;
import ru.nsu.fit.traffic.model.road.Lane;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.trafficsign.MainRoadSign;
import ru.nsu.fit.traffic.model.trafficsign.RoadSign;
import ru.nsu.fit.traffic.model.trafficsign.SignType;
import ru.nsu.fit.traffic.model.trafficsign.SpeedLimitSign;
import ru.nsu.fit.traffic.view.ViewUpdater;

/**
 * Контроллер основной сцены, на которой располагаются все остальные.
 */
public class MainController {

    @FXML private Pane statistics;
    @FXML private ScrollPane mainScrollPane;
    @FXML private Pane mainPane;
    @FXML private AnchorPane basePane;
    @FXML private Pane numberOfLanesPane;
    @FXML private Pane roadSignPane;
    @FXML private TextField backLanesTextField;
    @FXML private TextField forwardLanesTextField;
    @FXML private ComboBox<Integer> speedComboBox;
    @FXML private Group scrollPaneContent;
    @FXML private VBox centeredField;
    @FXML private BuildingController buildingSettingsController;
    @FXML private TrafficLightController trafficLightController;
    @FXML private MenuBarController menuBarController;
    @FXML private RoadSettingsController roadSettingsController;
    @FXML private StatisticsController statisticsController;
    @FXML private NodeSettingsController nodeSettingsController;
    @FXML private Slider timeLineSlider;

    private final TrafficMap currMap = new TrafficMap();
    private Road lastRoadClicked = null;
    private Node lastNodeClicked = null;
    private PlaceOfInterest lastPOIClicked = null;
    private double lastXbase = 0d;
    private double lastYbase = 0d;
    private Stage stage;

    private long startTime = 0;
    private long endTime = 10;
    private double scaleValue = 1;
    private double lastClickX = 0;
    private double lastClickY = 0;
    private Rectangle selectRect;
    private ReportStruct reportStruct = new ReportStruct();

    private ViewUpdater viewUpdater;
    private EditOperationsManager editOperationsManager;

    public PlaceOfInterest getLastPOIClicked() {
        return lastPOIClicked;
    }

    public Road getLastRoadClicked() {
        return lastRoadClicked;
    }

    public Node getLastNodeClicked() {
        return lastNodeClicked;
    }

    public TrafficMap getCurrMap() {
        return currMap;
    }

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

    public ViewUpdater getViewUpdater() {
        return viewUpdater;
    }

    public void updateStatistics() {
        statisticsController.updateStatistics();
    }

    private void stopOperation() {
        numberOfLanesPane.setVisible(false);
        editOperationsManager.resetLastNode();
        editOperationsManager.setCurrentOperation(EditOperation.NONE);
        nodeSettingsController.getNodeSettingPane().setVisible(false);

    }

    /**
     * инициализация.
     */
    @FXML
    public void initialize() {
        viewUpdater = new ViewUpdater(
                this::poiObserver,
                this::roadObserver,
                this::nodeObserver,
                mainPane);
        editOperationsManager =
                new EditOperationsManager(currMap, viewUpdater::updateMapView);
        menuBarController.setMainController(this);
        menuBarController.setMap(currMap);
        menuBarController.setStage(stage);

        selectRect = getSelectRect();

        roadSettingsController.setMainController(this);
        trafficLightController.setMainController(this);
        buildingSettingsController.setMainController(this);
        nodeSettingsController.setMainController(this);
        statisticsController.setMainController(this);

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
        timeLineSlider.setMax(Math.max(reportStruct.getWindowList().size() - 1, 0));
        timeLineSlider.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double object) {
                int id = (int) Math.round(object);
                if (id < reportStruct.getWindowList().size()) {
                    return String.valueOf(reportStruct.getWindowList().get(id).getEnd());
                }
                return "";
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        });
        timeLineSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (reportStruct.getWindowList().size() > (int) Math.round(newValue.doubleValue())) {
                editOperationsManager.updateCongestions(reportStruct.getWindowList().get((int) Math.round(newValue.doubleValue())));
            }
        });

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
                editOperationsManager.setLanesNumRight(Integer.parseInt(newValue)));

        roadSettingsController.getLanesTextField().setTextFormatter(new TextFormatter<String>(integerFilter));
        roadSettingsController.getLanesTextField().setText("1");

        forwardLanesTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        forwardLanesTextField.setText("1");
        forwardLanesTextField.textProperty().addListener((observable, oldValue, newValue) ->
                editOperationsManager.setLanesNumLeft(Integer.parseInt(newValue)));

        mainPane.setOnMousePressed(event -> {
            switch (event.getButton()) {
                case PRIMARY -> {
                    lastClickX = event.getX();
                    lastClickY = event.getY();
                    switch (editOperationsManager.getCurrentOperation()) {
                        case POI_CREATION -> {
                            selectRect.setX(event.getX());
                            selectRect.setY(event.getY());
                            selectRect.setScaleX(-1);
                            mainPane.getChildren().add(selectRect);
                        }
                    }
                }
            }
        });
        mainPane.setOnMouseClicked(this::onMainPaneClicked);

        mainPane.setOnMouseDragged(this::onMainPaneDrag);

        mainPane.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                switch (editOperationsManager.getCurrentOperation()) {
                    case POI_CREATION -> {
                        mainPane.getChildren().remove(selectRect);
                        double x = Math.min(event.getX(), lastClickX);
                        double y = Math.min(event.getY(), lastClickY);
                        double width = Math.abs(event.getX() - lastClickX);
                        double height = Math.abs(event.getY() - lastClickY);
                        editOperationsManager.addPlaceOfInterest(x, y, width, height);
                        statisticsController.updateStatistics();
                    }
                }
            }
        });

        basePane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            lastXbase = (Math.min(
                    event.getX(),
                    basePane.getWidth() - roadSettingsController.getRoadSettingsPane().getWidth()));
            lastYbase = (Math.min(
                    event.getY(),
                    basePane.getHeight() - roadSettingsController.getRoadSettingsPane().getHeight()));
        });

    }

    @FXML
    public void startSimulation() {
        File file = new File("heatMap.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Reader fileReader = new FileReader(file);
            List<ReportWindowStruct> reportWindowStructList = gson.fromJson(
                    fileReader, new TypeToken<List<ReportWindowStruct>>() {
                    }.getType());
            reportStruct.setWindowList(reportWindowStructList);
            reportStruct.fillCongestionList(currMap.getRoadCount());
            editOperationsManager.setCurrentOperation(EditOperation.REPORT_SHOWING);
            if (reportStruct.getWindowList().size() > 0) {
                editOperationsManager.updateCongestions(reportStruct.getWindowList().get(0));
            }
            timeLineSlider.setMax(Math.max(reportStruct.getWindowList().size() - 1, 0));
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void pauseSimulation() {
        System.out.println("пауза");
    }

    @FXML
    public void stopSimulation() {
        editOperationsManager.setCurrentOperation(EditOperation.NONE);
    }

    @FXML
    public void roadButtonClicked() {
        closeAllSettings();
        switch (editOperationsManager.getCurrentOperation()) {
            case ROAD_CREATION -> {
                numberOfLanesPane.setVisible(false);
                editOperationsManager.resetLastNode();
                editOperationsManager.setCurrentOperation(EditOperation.NONE);
            }
            default -> {
                numberOfLanesPane.setVisible(true);
                editOperationsManager.resetLastNode();
                editOperationsManager.setCurrentOperation(EditOperation.ROAD_CREATION);
            }
        }
    }

    @FXML
    public void trafficLightButtonClicked() {
        System.out.println("OUTED");
        switch (editOperationsManager.getCurrentOperation()) {
            case TRAFFIC_LIGHT_CREATION -> {
                editOperationsManager.setCurrentOperation(EditOperation.NONE);
            }
            default -> {
                closeAllSettings();
                editOperationsManager.setCurrentOperation(EditOperation.TRAFFIC_LIGHT_CREATION);
            }
        }
    }

    @FXML
    public void buildingButtonClicked() {
        closeAllSettings();
        switch (editOperationsManager.getCurrentOperation()) {
            case POI_CREATION -> {
                editOperationsManager.setCurrentOperation(EditOperation.NONE);
            }
            default -> {
                closeAllSettings();
                editOperationsManager.setCurrentOperation(EditOperation.POI_CREATION);
            }
        }
    }

    @FXML
    public void carButtonClicked() {
        System.out.println("машина");
    }

    @FXML
    public void roadSignButtonClicked() {
        closeAllSettings();
        switch (editOperationsManager.getCurrentOperation()) {
            case SIGN_CREATION:
                roadSignPane.setVisible(false);
                editOperationsManager.setCurrentOperation(EditOperation.NONE);
                break;
            default:
                roadSignPane.setVisible(true);
                roadSettingsController.getRoadSettingsPane().setVisible(false);
                numberOfLanesPane.setVisible(false);
                editOperationsManager.setCurrentOperation(EditOperation.SIGN_CREATION);
                break;
        }
    }

    @FXML
    public void showStatistic() {
        statistics.setVisible(!statistics.isVisible());
    }

    @FXML
    public void setSpeedSign() {
        editOperationsManager.setCurrSign(new SpeedLimitSign(speedComboBox.getValue()));
    }

    @FXML
    public void setMainRoad() {
        editOperationsManager.setCurrSign(new MainRoadSign());
    }

    private void nodeObserver(Node node, Shape nodeShape) {
        nodeShape.setOnMouseClicked(event -> {
            onNodeClick(node, event);
        });
    }

    private void roadObserver(Road road, Shape roadShape, int i) {
        roadShape.setOnMouseClicked(event ->
                onRoadClick(road, i, event));
    }

    private void poiObserver(PlaceOfInterest placeOfInterest, Shape placeOfInterestShape) {
        placeOfInterestShape.setOnMouseClicked(event -> {
            onPoiClicked(event, placeOfInterest);
        });
    }

    private void onPoiClicked(MouseEvent event, PlaceOfInterest placeOfInterest) {
        lastPOIClicked = placeOfInterest;
        switch (event.getButton()) {
            case PRIMARY -> {
                switch (editOperationsManager.getCurrentOperation()) {
                    case ROAD_CREATION -> {
                        event.consume();
                        editOperationsManager.buildRoadOnPlaceOfInterest(event.getX(), event.getY(), placeOfInterest);
                    }
                    case NONE -> {
                        event.consume();
                        buildingSettingsController.getPane().setLayoutX(event.getX());
                        buildingSettingsController.getPane().setLayoutY(event.getY());
                        buildingSettingsController.getSlider().setValue(lastPOIClicked.getWeight());
                        buildingSettingsController.getParkingPlaces().setText
                                (String.valueOf(lastPOIClicked.getNumberOfParkingPlaces()));
                        buildingSettingsController.getPane().setVisible(true);
                    }
                }
            }
        }
    }

    /**
     * Обработка клика по полю
     *
     * @param event событие
     */
    private void onMainPaneClicked(MouseEvent event) {
        switch (event.getButton()) {
            case PRIMARY -> {
                System.out.println("MAIN PANE CLICK");
                switch (editOperationsManager.getCurrentOperation()) {
                    case ROAD_CREATION -> {
                        event.consume();
                        editOperationsManager.buildRoadOnEmpty(event.getX(), event.getY());
                        updateStatistics();
                    }
                    case SIGN_CREATION -> {
                        event.consume();
                        editOperationsManager.setCurrentOperation(EditOperation.NONE);
                        roadSignPane.setVisible(false);
                    }
                    case NONE -> {
                        roadSettingsController.getRoadSettingsPane().setVisible(false);
                    }
                }
            }

            case SECONDARY -> stopOperation();
        }
    }

    private void onNodeClick(Node node, MouseEvent event) {
        System.out.println("NODE CLICK");
        lastNodeClicked = node;
        switch (event.getButton()) {
            case PRIMARY -> {
                event.consume();
                switch (editOperationsManager.getCurrentOperation()) {
                    case ROAD_CREATION -> {
                        editOperationsManager.buildRoadOnNode(node);
                        updateStatistics();
                    }
                    case TRAFFIC_LIGHT_CREATION -> {
                        if (node.getRoadsInNum() <= 2) {
                            break;
                        }
                        trafficLightController.getTrafficLightPane().setVisible(true);
                        trafficLightController.getTrafficLightPane().setLayoutX(lastXbase);
                        trafficLightController.getTrafficLightPane().setLayoutY(lastYbase);
                        trafficLightController.setLastNodeClicked(node);
                        trafficLightController.updateDelay(node);
                        editOperationsManager.updateRoadsHighLight(node);

                    }
                    case NONE -> {
                        stopOperation();
                        if (node.getPlaceOfInterest() != null) {
                            if (node.getSpawners() != null) {
                                nodeSettingsController.getStartTime().setValue(LocalTime.parse(node.getSpawners().get(0).getStartString()));
                                nodeSettingsController.getEndTime().setValue(LocalTime.parse(node.getSpawners().get(0).getEndString()));
                                nodeSettingsController.getSpawnerRate().setText(String.valueOf(node.getSpawners().get(0).getSpawnRate()));
                            }
                            nodeSettingsController.getNodeSettingPane().setLayoutX(lastXbase);
                            nodeSettingsController.getNodeSettingPane().setLayoutY(lastYbase);
                            nodeSettingsController.getNodeSettingPane().setVisible(true);
                        }
                        updateStatistics();
                    }
                }
            }
        }
        //todo другие операции
    }

    /**
     * Обработка драга на поле
     *
     * @param event событие
     */
    private void onMainPaneDrag(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            switch (editOperationsManager.getCurrentOperation()) {
                case POI_CREATION -> {
                    double width = event.getX() - selectRect.getX();
                    double height = event.getY() - selectRect.getY();
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
            }
        }
    }

    private void onRoadClick(Road road, int i, MouseEvent event) {
        System.out.println("ROAD CLICK");
        Lane lane = road.getLane(i);
        switch (event.getButton()) {
            case PRIMARY -> {
                switch (editOperationsManager.getCurrentOperation()) {
                    case ROAD_CREATION -> {
                        event.consume();
                        editOperationsManager.buildRoadOnRoad(event.getX(), event.getY(), road);
                        statisticsController.updateStatistics();
                    }
                    case NONE -> {
                        event.consume();
                        lastRoadClicked = road;
                        roadSettingsController.updateRoad(road);
                        roadSettingsController.getRoadSettingsPane().setVisible(true);
                        roadSettingsController.getRoadSettingsHelperPane().setLayoutX(lastXbase);
                        roadSettingsController.getRoadSettingsHelperPane().setLayoutY(lastYbase);
                        //roadSettingsController.getRoadSettingsPane().setLayoutX(event.getX());
                        //roadSettingsController.getRoadSettingsPane().setLayoutY(event.getY());
                    }
                    case SIGN_CREATION -> {
                        RoadSign addedSign = editOperationsManager.getCurrSign().getCopySign();
                        lane.addSign(addedSign);
                        if (editOperationsManager.getCurrSign().getSignType() == SignType.MAIN_ROAD) {
                            for (int k = 0; k < road.getLanesNum(); k++) {
                                road.getLane(k).addSign(addedSign);
                            }
                        }
                        viewUpdater.updateMapView(editOperationsManager);
                    }
                }
            }
        }
        //todo другие операции
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

    public EditOperationsManager getEditOperationManager() {
        return editOperationsManager;
    }

    private void closeAllSettings() {
        buildingSettingsController.getPane().setVisible(false);
        nodeSettingsController.getNodeSettingPane().setVisible(false);
        numberOfLanesPane.setVisible(false);
        roadSettingsController.getRoadSettingsPane().setVisible(false);
        trafficLightController.getTrafficLightPane().setVisible(false);
        roadSignPane.setVisible(false);
        //updateMapView();
    }
}