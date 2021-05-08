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
    Rectangle rect = new Rectangle(x-20, y-20, 40,40);
   /* System.out.println(String.valueOf(rect.getX())+" " +  String.valueOf(rect.getY())+" " +
            String.valueOf((rect.getWidth() + rect.getX()))+ " " +String.valueOf((rect.getY() + rect.getHeight())));*/
    List<RectRegion> regions = new ArrayList<>();
    for (RectRegion region: this.regions){
      /*System.out.println(String.valueOf(region.getX())+" " +  String.valueOf(region.getY())+" " +
            String.valueOf((region.getWidth() + region.getX()))+ " " +String.valueOf((region.getY() + region.getHeight())));*/
      if (rect.intersects(region.getX(), region.getY(), region.getHeight(), region.getWidth())){
        regions.add(region);
      }
    }
    return regions;
  }
}
