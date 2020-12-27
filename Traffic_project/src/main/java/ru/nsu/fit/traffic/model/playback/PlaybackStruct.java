package ru.nsu.fit.traffic.model.playback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.fit.traffic.model.map.TrafficMap;


public class PlaybackStruct {
  private Map<Integer, List<CarState>> timeToCarStates = new HashMap<>();
  private Map<Integer, CarState> idToCurrentState = new HashMap<>();
  private int lastTime = 0;
  private TrafficMap map;

  public PlaybackStruct(TrafficMap map) {
    this.map = map;
  }

  private void addToMap(CarState carState) {
    int time = carState.getTime();

    if (!timeToCarStates.containsKey(time)) {
      timeToCarStates.put(time, new ArrayList<>());
    }

    timeToCarStates.get(time).add(carState);
  }

  public int getMinTime() {
    int min = 999999999;
    for (Integer integer : timeToCarStates.keySet()) {
      if (integer < min) {
        min = integer;
      }
    }
    assert min != 999999999;
    return min;
  }

  public int getMaxTime() {
    int max = -1;
    for (Integer integer : timeToCarStates.keySet()) {
      if (integer > max) {
        max = integer;
      }
    }
    assert max != -1;
    return max;
  }

  public List<CarState> getCarStates(int time) {
    if (time < lastTime) {
      lastTime = 0;
      idToCurrentState.clear();
    }
    for (; lastTime <= time; lastTime++) {
      if (timeToCarStates.containsKey(lastTime)) {
        List<CarState> carStates = timeToCarStates.get(lastTime);
        carStates.forEach(car -> {
          if (!car.isDraw()) {
            idToCurrentState.remove(car.getCarId());
          } else {
            idToCurrentState.put(car.getCarId(), car);
          }
        });
      }
    }
    return List.copyOf(idToCurrentState.values());

  }

  public void fillInTimeMap(String playbackFilePath) {
    File file = new File(playbackFilePath);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try {
      Reader fileReader = new FileReader(file);
      List<CarState> carStates = gson.fromJson(
        fileReader, new TypeToken<List<CarState>>() {
        }.getType());
      carStates.forEach(carState -> {
        carState.initCoords(map.getRoad(carState.getCurrentRoad()));
        carState.setTime(carState.getTime() / 1000);
        addToMap(carState);
      });
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    }
  }


}
