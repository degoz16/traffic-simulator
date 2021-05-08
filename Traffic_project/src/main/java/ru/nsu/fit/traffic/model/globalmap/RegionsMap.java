package ru.nsu.fit.traffic.model.globalmap;

import javafx.scene.shape.Rectangle;

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

  public List<RectRegion> getRegionsInThePoint(double x, double y){
    List<RectRegion> regions = new ArrayList<>();
    for (RectRegion region: this.regions){

      Rectangle rect = new Rectangle(region.getX(), region.getY(), region.getWidth(), region.getHeight());
      if (rect.intersects(x - 20, y-20, 40, 40)){
        regions.add(region);
      }
    }
    return regions;
  }
}
