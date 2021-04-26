package ru.nsu.fit.traffic.model.globalmap;

import java.util.ArrayList;
import java.util.List;

public class RegionsMap {
  private List<RectRegion> regions = new ArrayList<>();

  public void addRegion(RectRegion region) {
    regions.add(region);
  }

  public List<RectRegion> getRegions() {
    return regions;
  }

  public void setRegions(List<RectRegion> regions) {
    this.regions = regions;
  }
}
