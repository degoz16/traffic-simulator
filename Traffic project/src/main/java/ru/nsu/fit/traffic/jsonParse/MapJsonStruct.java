package ru.nsu.fit.traffic.jsonParse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ru.nsu.fit.traffic.model.road.Lane;
import ru.nsu.fit.traffic.model.node.Node;
import ru.nsu.fit.traffic.model.PlaceOfInterest;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.node.Spawner;
import ru.nsu.fit.traffic.model.TrafficMap;

public class MapJsonStruct {
  private final List<NodeJsonStruct> nodes = new ArrayList<>();
  private final List<RoadJsonStruct> roads = new ArrayList<>();
  private final List<PlaceOfInterestJsonStruct> pois = new ArrayList<>();

  public MapJsonStruct(TrafficMap map) {
    map.forEachNode(
        node -> {
          List<Integer> roadsFrom = new ArrayList<>();
          List<Integer> roadsTo = new ArrayList<>();
          SpawnerJsonStruct spawnerJsonStruct =
              (node.getSpawner() != null)
                  ? new SpawnerJsonStruct(
                      node.getSpawner().getStartString(),
                      node.getSpawner().getEndString(),
                      node.getSpawner().getSpawnRate())
                  : null;
          node.foreachRoadIn(road -> roadsFrom.add(map.indexOfRoad(road)));
          node.foreachRoadOut(road -> roadsTo.add(map.indexOfRoad(road)));

          nodes.add(
              new NodeJsonStruct(
                  node.getX(),
                  node.getY(),
                  roadsFrom,
                  roadsTo,
                  spawnerJsonStruct,
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
    map.forEachPlaceOfInterest(
        poi -> {
          List<Integer> nodes = new ArrayList<>();
          poi.foreachNodeIn(node -> nodes.add(map.indexOfNode(node)));
          pois.add(
              new PlaceOfInterestJsonStruct(
                  poi.getX(),
                  poi.getY(),
                  poi.getWidth(),
                  poi.getHeight(),
                  poi.getNumberOfParkingPlaces(),
                  poi.getWeight(),
                  nodes));
        });
  }

  /**
   * Меняет переданный traffic map новыми данными.
   *
   * @param trafficMap
   */
  public void toTrafficMap(TrafficMap trafficMap) {
    RoadSignCreator creator = new RoadSignCreator();
    List<Road> mapRoads = new ArrayList<>();
    List<Node> mapNodes = new ArrayList<>();
    List<PlaceOfInterest> mapPois = new ArrayList<>();

    nodes.forEach(
        node -> {
          Node mapNode = new Node(node.getX(), node.getY());
          Spawner spawner =
              node.getSpawner() != null
                  ? new Spawner(
                      node.getSpawner().getStart(),
                      node.getSpawner().getEnd(),
                      node.getSpawner().getSpawnerRate())
                  : null;
          mapNode.setSpawner(spawner);
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
          Node nodeFrom = mapNodes.get(roadJsonStruct.getFrom());
          Node nodeTo = mapNodes.get(roadJsonStruct.getTo());
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
        assert roadIn.getTo() == node;

        Road roadOut = mapRoads.get(nodeJsonStruct.getRoadsOut().get(j));
        assert roadOut.getFrom() == node;

        node.addRoadOut(roadOut);
        node.addRoadIn(roadIn);

        roadIn.setBackRoad(roadOut);
        roadOut.setBackRoad(roadIn);
      }
    }

    pois.forEach(
        poiJsonStruct -> {
          PlaceOfInterest place =
              new PlaceOfInterest(
                  poiJsonStruct.getX(),
                  poiJsonStruct.getY(),
                  poiJsonStruct.getWidth(),
                  poiJsonStruct.getHeight(),
                  poiJsonStruct.getCapacity(),
                  poiJsonStruct.getWeight());
          poiJsonStruct
              .getNodes()
              .forEach(
                  nodeNum -> {
                    Node node = mapNodes.get(nodeNum);
                    node.setPlaceOfInterest(place);
                    place.addNode(node);
                  });
          mapPois.add(place);
        });

    trafficMap.setNodes(mapNodes);
    trafficMap.setRoads(mapRoads);
    trafficMap.setPlacesOfInterest(mapPois);
  }
}
