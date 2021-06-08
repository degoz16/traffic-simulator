package ru.nsu.fit.traffic.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
  private String lastIp = "localhost:8080";
  private int lastGlobalMap = -1;
  private int lastFragment = -1;

  public void load(String path) {
    Properties properties = new Properties();
    try {
      properties.load(new FileReader(path));
    } catch (IOException e) {
      e.printStackTrace();
    }
    lastIp = properties.getProperty("lastIp", "localhost:8080");
    lastGlobalMap = Integer.parseInt(properties.getProperty("lastGlobalMap", "-1"));
    lastFragment = Integer.parseInt(properties.getProperty("lastFragment", "-1"));
  }

  public void save(String path) {
    Properties properties = new Properties();
    properties.setProperty("lastIp", lastIp);
    properties.setProperty("lastGlobalMap", Integer.toString(lastGlobalMap));
    properties.setProperty("lastFragment", Integer.toString(lastFragment));
    try {
      properties.store(new FileWriter(path), "TRAFFIC EDITOR CONFIG");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getLastIp() {
    return lastIp;
  }

  public void setLastIp(String lastIp) {
    this.lastIp = lastIp;
  }

  public int getLastGlobalMap() {
    return lastGlobalMap;
  }

  public void setLastGlobalMap(int lastGlobalMap) {
    this.lastGlobalMap = lastGlobalMap;
  }

  public int getLastFragment() {
    return lastFragment;
  }

  public void setLastFragment(int lastFragment) {
    this.lastFragment = lastFragment;
  }
}
