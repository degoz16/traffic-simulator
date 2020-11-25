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
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import ru.nsu.fit.traffic.jsonParse.MapJsonStruct;
import ru.nsu.fit.traffic.model.TrafficMap;

public class ProjectController {
  private Stage stage;
  private TrafficMap map;
  private String pathToProjectDir = null;

  ProjectController(Stage stage, TrafficMap map) {
    this.stage = stage;
    this.map = map;
    this.pathToProjectDir = getDocsPath();
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
        new File(pathToProjectDir); // TODO Должно быть диалоговое окно с выбором СОХРАНИТЬ/НЕТ
    saveCurrentProject(file);
    map.setRoads(new ArrayList<>());
    map.setNodes(new ArrayList<>());
    pathToProjectDir = getDocsPath();
  }

  void openProjectHandler() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save project");
    fileChooser
        .getExtensionFilters()
        .addAll(new ExtensionFilter("Traffic Simulator Projects", "*.tsp"));
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      try {
        Reader fileReader = new FileReader(file);
        MapJsonStruct mapJsonStruct = gson.fromJson(fileReader, MapJsonStruct.class);
        File oldFile =
            new File(pathToProjectDir); // TODO Должно быть диалоговое окно с выбором СОХРАНИТЬ/НЕТ
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
          .addAll(new ExtensionFilter("Traffic Simulator Projects", "*.tsp"));
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
