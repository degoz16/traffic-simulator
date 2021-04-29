package ru.nsu.fit.traffic.model.globalmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RegionsMap {
  //private Map<String, RectRegion> regions = new HashMap<>();
  private List<RectRegion> regions = new ArrayList<>();

  public void addRegion(RectRegion region) {
    //regions.put(region.getName(), region);
    regions.add(region);
  }

//  public Map<String, RectRegion> getRegions() {
//    return regions;
//  }

//  public void setRegions(Map<String, RectRegion> regions) {
//    this.regions = regions;
//  }

  public void foreachRegion(Consumer<RectRegion> consumer) {
    regions.forEach(consumer);
  }

  public RectRegion getRegion(int id) {
    return regions.get(id);
  }

  public int getRegionCount() {
    return regions.size();
  }
}
