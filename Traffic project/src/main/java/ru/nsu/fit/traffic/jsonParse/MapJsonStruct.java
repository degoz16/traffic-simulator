package ru.nsu.fit.traffic.jsonParse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ru.nsu.fit.traffic.model.Lane;
import ru.nsu.fit.traffic.model.Node;
import ru.nsu.fit.traffic.model.Road;
import ru.nsu.fit.traffic.model.TrafficMap;

public class MapJsonStruct {
  private List<NodeJsonStruct> nodes = new ArrayList<>();
  private List<RoadJsonStruct> roads = new ArrayList<>();

  public MapJsonStruct(TrafficMap map) {
    map.forEachNode(
        node -> {
          List<Integer> roadsFrom = new ArrayList<>();
          List<Integer> roadsTo = new ArrayList<>();
          node.foreachRoadIn(road -> roadsFrom.add(map.indexOfRoad(road)));
          node.foreachRoadOut(road -> roadsTo.add(map.indexOfRoad(road)));
          nodes.add(
              new NodeJsonStruct(
                  node.getX(),
                  node.getY(),
                  roadsFrom,
                  roadsTo,
                  node.isSpawner(),
                  node.getTrafficLight()));
        });
    map.forEachRoad(
        road -> {
          List<LaneJsonStruct> lanes = new ArrayList<>();
          road.forEachLane(
              lane -> {
                List<Map<String, String>> signs = new ArrayList<>();
                lane.getSigns().forEach(sign -> signs.add(sign.getSettings()));
                LaneJsonStruct laneJsonStruct = new LaneJsonStruct(signs);
                lanes.add(laneJsonStruct);
              });
          RoadJsonStruct roadJsonStruct =
              new RoadJsonStruct(
                  map.indexOfNode(road.getFrom()), map.indexOfNode(road.getTo()), lanes);
          roads.add(roadJsonStruct);
        });
  }


  /**
   * Меняет переданный traffic map новыми данными.
   * @param trafficMap
   */
  public void toTrafficMap(TrafficMap trafficMap) {
    RoadSignCreator creator = new RoadSignCreator();
    List<Road> mapRoads = new ArrayList<>();
    List<Node> mapNodes = new ArrayList<>();
    nodes.forEach(
        node -> {
          Node mapNode = new Node(node.getX(), node.getY());
          mapNodes.add(mapNode);
        });
    roads.forEach(
        roadJsonStruct -> {
          Road mapRoad = new Road();
          List<Lane> lanes = new ArrayList<>();
          roadJsonStruct
              .getLanes()
              .forEach(
                  laneJsonStruct -> {
                    Lane lane = new Lane();
                    laneJsonStruct
                        .getSigns()
                        .forEach(
                            map -> {
                              lane.addSign(creator.createSign(map));
                            });
                    lanes.add(lane);
                  });
          mapRoad.setLanes(lanes);
          Node nodeFrom =
              mapNodes.get(roadJsonStruct.getFrom()); // FIXME: 25.11.2020 FOR TESTING PURPOSES
          Node nodeTo =
              mapNodes.get(roadJsonStruct.getTo()); // FIXME: 25.11.2020 FOR TESTING PURPOSES
          mapRoad.setTo(nodeTo);
          mapRoad.setFrom(nodeFrom);

          mapRoads.add(mapRoad);
        });

    int i = 0;
    for (Node node : mapNodes) {
      NodeJsonStruct nodeJsonStruct = nodes.get(i++);
      assert nodeJsonStruct.getRoadsIn().size() == nodeJsonStruct.getRoadsOut().size();
      for (int j = 0; j < nodeJsonStruct.getRoadsIn().size(); j++) {
        Road roadIn = mapRoads.get(nodeJsonStruct.getRoadsIn().get(j));
        assert roadIn.getTo() == node; // FIXME: 25.11.2020 FOR TESTING PURPOSES

        Road roadOut = mapRoads.get(nodeJsonStruct.getRoadsOut().get(j));
        assert roadOut.getFrom() == node; // FIXME: 25.11.2020 FOR TESTING PURPOSES

        node.addRoadOut(roadOut);
        node.addRoadIn(roadIn);

        roadIn.setBackRoad(roadOut);
        roadOut.setBackRoad(roadIn);
      }
    }

    trafficMap.setNodes(mapNodes);
    trafficMap.setRoads(mapRoads);
  }
}
