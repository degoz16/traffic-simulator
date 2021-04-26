package ru.nsu.fit.traffic.model.globalmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionsMap {
  private Map<String, RectRegion> regions = new HashMap<>();

  public void addRegion(RectRegion region) {
    regions.put(region.getName(), region);
  }

  public Map<String, RectRegion> getRegions() {
    return regions;
  }

  public void setRegions(Map<String, RectRegion> regions) {
    this.regions = regions;
  }
}
