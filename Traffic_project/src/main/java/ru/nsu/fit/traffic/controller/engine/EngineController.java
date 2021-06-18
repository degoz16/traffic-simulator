package ru.nsu.fit.traffic.controller.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import ru.nsu.fit.traffic.controller.BaseControl;
import ru.nsu.fit.traffic.controller.SceneElementsControl;

public class EngineController extends BaseControl {

  private String heatMapPath;
  private String carStatePath;
  private String enginePath;
  private String mapPath;
  private String optimizedMapPath;
  private Process engineProcess;
  private Thread thread;
  private Thread rThread;

  public EngineController(String enginePath, SceneElementsControl sceneElementsControl) {
    super(sceneElementsControl);
    this.enginePath = enginePath;
  }

  public String getMapPath() {
    return mapPath;
  }

  public void setMapPath(String mapPath) {
    this.mapPath = mapPath;
  }

  public String getOptimizedMapPath() {
    return optimizedMapPath;
  }

  public void setOptimizedMapPath(String optimizedMapPath) {
    this.optimizedMapPath = optimizedMapPath;
  }

  public String getHeatMapPath() {
    try {
      if (thread.isAlive()) {
        thread.join();
      }
    } catch (InterruptedException e) {
      assert false;
    }
    return heatMapPath;
  }

  public void setHeatMapPath(String heatMapPath) {
    this.heatMapPath = heatMapPath;
  }

  public String getCarStatePath() {
    try {
      if (thread.isAlive()) {
        thread.join();
      }
    } catch (InterruptedException e) {
      assert false;
    }
    return carStatePath;
  }

  public void setCarStatePath(String carStatePath) {
    this.carStatePath = carStatePath;
  }

  public void setEnginePath(String enginePath) {
    this.enginePath = enginePath;
  }

  public void startEngine() {
    thread = new Thread(() -> {
      try {
        engineProcess = Runtime.getRuntime().exec(
          "java -jar \""
            + enginePath
            + "\" sim \""
            + mapPath
            + "\" \""
            + heatMapPath
            + "\" \""
            + carStatePath + "\""
        );
        System.out.println("START");
        System.out.println(enginePath);
        System.out.println(mapPath);
        System.out.println(heatMapPath);
        System.out.println(carStatePath);
        getCommands();
        engineProcess.waitFor();
        //DEBUG
        //Thread.sleep(5000);
        //DEBUG
        System.out.println(engineProcess.exitValue());
        rThread.interrupt();
        rThread.join();
        System.out.println("END");
        sceneElementsControl.simulationEndModeEnable();
      } catch (InterruptedException | IOException e) {
        System.err.println(e.getMessage());
        assert false;
      }
    });
    thread.start();
  }

  public void startOptimizing() {
    thread = new Thread(() -> {
      try {
        engineProcess = Runtime.getRuntime().exec(
          "java -jar \""
            + enginePath
            + "\" opt \""
            + mapPath
            + "\" \""
            + optimizedMapPath + "\""
        );
        System.out.println(mapPath);
        System.out.println(optimizedMapPath);
        System.out.println("START OPTIMIZATION");
        getCommands();
        engineProcess.waitFor();
        //DEBUG
        //Thread.sleep(5000);
        //DEBUG
        System.out.println(engineProcess.exitValue());
        rThread.interrupt();
        rThread.join();
        System.out.println("END");
        //sceneElementsControl.simulationEndModeEnable();
        sceneElementsControl.optimizingModeEnabled(optimizedMapPath);
      } catch (InterruptedException | IOException e) {
        System.err.println(e.getMessage());
        assert false;
      }
    });
    thread.start();
  }

  public void continueEngine() {
    sendCommand(EngineCommand.CONTINUE);
  }

  public void pauseEngine() {
    sendCommand(EngineCommand.PAUSE);
  }

  public void stopEngine() {
    sendCommand(EngineCommand.STOP);
  }

  private void getCommands() {
    rThread = new Thread(() -> {

      BufferedReader r = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
      BufferedReader err = new BufferedReader(new InputStreamReader(engineProcess.getErrorStream()));

      try {
        while (!Thread.interrupted()) {
          String string = r.readLine();
          if (string != null) {
            System.out.println(r.readLine());
          }
        }
      } catch (IOException e) {
        e.printStackTrace();

      }
    });
    rThread.start();
  }


  private void sendCommand(EngineCommand command) {
    if (engineProcess != null && engineProcess.isAlive()) {
      try {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(engineProcess.getOutputStream()));
        w.write(command.toString() + "\n");
        w.flush();
      } catch (IOException e) {
        System.err.println(e.getMessage());
        assert false;
      }
    }
  }
}
