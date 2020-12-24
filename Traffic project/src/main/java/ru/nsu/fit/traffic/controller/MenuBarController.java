package ru.nsu.fit.traffic.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.jsonParse.MapJsonStruct;
import ru.nsu.fit.traffic.model.map.TrafficMap;

public class MenuBarController {
  @FXML private MenuItem saveAs;
  @FXML private MenuItem newProject;
  @FXML private MenuItem openProject;
  @FXML private MenuItem save;

  private MainController mainController;
  private Stage stage;
  private TrafficMap map;
  private String pathToProjectDir = null;

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void setMap(TrafficMap map) {
    this.map = map;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /** инициализация. */
  @FXML
  public void initialize() {

    saveAs.setOnAction(saveAsHandler());
    pathToProjectDir = getDocsPath();
    newProject.setOnAction(
        event -> {
          event.consume();
          newProjectHandler();
          mainController.getViewUpdater()
                  .updateMapView(mainController.getEditOperationManager());
        });
    openProject.setOnAction(
        event -> {
          event.consume();
          openProjectHandler();
          mainController.getViewUpdater()
                  .updateMapView(mainController.getEditOperationManager());
        });
    save.setOnAction(saveHandler());
  }

  private String getDocsPath() {
    String myDocuments = null;
    try {
      Process p =
          Runtime.getRuntime()
              .exec(
                  "reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion"
                      + "\\Explorer\\Shell Folders\" /v personal");
      p.waitFor();
      InputStream in = p.getInputStream();
      byte[] b = new byte[in.available()];
      in.read(b);
      in.close();
      myDocuments = new String(b);
      myDocuments = myDocuments.split("\\s\\s+")[4];
      int num = 0;
      String projectName = "myTrafficProject";
      File file = new File(myDocuments, projectName + ".tsp");
      while (file.exists()) {
        file = new File(myDocuments, projectName + (num++) + ".tsp");
      }
      if (num == 0) {
        myDocuments += "\\" + projectName + ".tsp";
      } else {
        myDocuments += "\\" + projectName + "(" + (num - 1) + ").tsp";
      }
    } catch (Throwable t) {
      assert false;
    }
    return myDocuments;
  }

  void newProjectHandler() {
    File file =
        new File(pathToProjectDir); // TODO Должно быть диалоговое окно с выбором СОХРАНиТЬ/НЕТ
    saveCurrentProject(file);
    map.clearMap();
    pathToProjectDir = getDocsPath();
  }

  void openProjectHandler() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save project");
    fileChooser
        .getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("Traffic Simulator Projects", "*.tsp"));
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      try {
        Reader fileReader = new FileReader(file);
        MapJsonStruct mapJsonStruct = gson.fromJson(fileReader, MapJsonStruct.class);
        File oldFile =
            new File(pathToProjectDir); // TODO Должно быть диалоговое окно с выбором СОХРАНиТЬ/НЕТ
        saveCurrentProject(oldFile);
        mapJsonStruct.toTrafficMap(map);
        pathToProjectDir = file.getAbsolutePath();
      } catch (FileNotFoundException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  EventHandler<ActionEvent> saveAsHandler() {
    return event -> {
      event.consume();
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save project");
      fileChooser
          .getExtensionFilters()
          .addAll(new FileChooser.ExtensionFilter("Traffic Simulator Projects", "*.tsp"));
      File file = fileChooser.showSaveDialog(stage);

      if (file != null) {
        saveCurrentProject(file);
        pathToProjectDir = file.getAbsolutePath();
      }
    };
  }

  EventHandler<ActionEvent> saveHandler() {
    return event -> {
      event.consume();
      File file = new File(pathToProjectDir);
      saveCurrentProject(file);
    };
  }

  private void saveCurrentProject(File file) {
    try {
      Writer writer = new FileWriter(file);
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String jsonMap = gson.toJson(new MapJsonStruct(map));
      writer.write(jsonMap);
      writer.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
