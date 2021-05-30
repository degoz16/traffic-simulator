package ru.nsu.fit.traffic.javafx.controller.edit;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.controller.GlobalMapEditControlInitializer;
import ru.nsu.fit.traffic.controller.GlobalMapSelectorInitializer;
import ru.nsu.fit.traffic.event.wrappers.MouseEventWrapper;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapControlInitializerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapEditControlInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorControllerInterface;
import ru.nsu.fit.traffic.interfaces.control.GlobalMapSelectorInitializerInterface;
import ru.nsu.fit.traffic.model.globalmap.RectRegion;
import ru.nsu.fit.traffic.model.globalmap.RegionsMap;
import ru.nsu.fit.traffic.model.globalmap.RoadConnector;
import ru.nsu.fit.traffic.model.logic.GlobalMapEditOpManager;
import ru.nsu.fit.traffic.model.logic.GlobalMapUpdateObserver;
import ru.nsu.fit.traffic.utils.Pair;
import ru.nsu.fit.traffic.view.GlobalMapEditorViewUpdater;
import ru.nsu.fit.traffic.view.GlobalMapObjectPainter;

public class GlobalSelectorController {
  private Stage stage;
  @FXML private ScrollPane mainScrollPane;
  @FXML private Group scrollPaneContent;
  @FXML private Pane mainPane;
  @FXML private AnchorPane basePane;
  @FXML private CheckBox checkBox;
  private GlobalMapObjectPainter painter;
  private GlobalMapSelectorControllerInterface selectorControl;
  private GlobalMapEditOpManager editOpManager = null;
  private GlobalMapUpdateObserver updateObserver = null;

  public void setCurrentMap(RegionsMap regionsMap){
    for (int i = 0; i < regionsMap.getRegionCount(); ++i){
      RectRegion region = regionsMap.getRegion(i);
      Node reg = painter.paintRegion(region);

      final int id = i;
      reg.setOnMouseClicked(event ->{
        selectorControl.onRegionClick(id, MouseEventWrapper.getMouseEventWrapper(event));
      });

      mainPane.getChildren().add(reg);

      for (RoadConnector connector: region.getConnectorList()){
        mainPane.getChildren().add(painter.paintConnector(connector,true));
      }
    }
  }

  public void setStage(Stage stage){
    this.stage = stage;
  }

  @FXML
  public void checkBoxClicked(){
    if (checkBox.isSelected()){
      // todo: show whole map
      System.out.println("show whole map");
    }else{
      // todo: hide map
      System.out.println("hide map");
    }
  }

  @FXML
  public void initialize() {
    painter = new GlobalMapObjectPainter();
    GlobalMapSelectorInitializerInterface initializer =
        new GlobalMapSelectorInitializer();
    selectorControl = initializer.getSelectorControl();

    GlobalMapEditorViewUpdater viewUpdater =
        new GlobalMapEditorViewUpdater(
            ((rect, id, regW, regH) -> {
              rect.setOnMouseClicked(event -> {
                selectorControl.onRegionClick(id, MouseEventWrapper.getMouseEventWrapper(event));
              });
            }),
            mainPane);
    initializer.initialize(viewUpdater::updateMapView);
  }
}
