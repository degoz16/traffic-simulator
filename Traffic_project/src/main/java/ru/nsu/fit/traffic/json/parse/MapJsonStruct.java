package ru.nsu.fit.traffic.json.parse;

import ru.nsu.fit.traffic.model.map.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapJsonStruct {
  private final List<NodeJsonStruct> nodes = new ArrayList<>();
  private final List<RoadJsonStruct> roads = new ArrayList<>();
  private final List<PlaceOfInterestJsonStruct> pointsOfInterest = new ArrayList<>();
  private double width;
  private double height;
  private String start = "09:00";
  private String end = "19:00";

  public MapJsonStruct(TrafficMap map) {
    start = map.getStart();
    end = map.getEnd();
    width = map.getWidth();
    height = map.getHeight();
    map.forEachNode(
        node -> {
          List<Integer> roadsFrom = new ArrayList<>();
          List<Integer> roadsTo = new ArrayList<>();
          List<SpawnerJsonStruct> structs = null;
          if (node.getSpawners() != null) {
            structs = new ArrayList<>();
            List<SpawnerJsonStruct> finalStructs = structs;
            node.getSpawners().forEach(spawner -> finalStructs.add(new SpawnerJsonStruct(
                spawner.getStartString(), spawner.getEndString(), spawner.getSpawnRate())));
          }
          node.foreachRoadIn(road -> roadsFrom.add(map.indexOfRoad(road)));
          node.foreachRoadOut(road -> roadsTo.add(map.indexOfRoad(road)));
          RegionConnectorJson conn = null;
          if (node.getConnector() != null) {
            conn = new RegionConnectorJson(
                node.getConnector().getRegionId(),
                node.getConnector().getConnectorId());
          }
          if (node.getTrafficLight() != null) {
            TrafficLight trafficLight = node.getTrafficLight();
            List<TrafficLightConfigJsonStruct> configToTraffic = new ArrayList<>();
            trafficLight.getConfig().forEach(x -> {
              List<Integer> roads = new ArrayList<>();
              for (Road road: x.getRoads()){
                roads.add(map.getRoads().indexOf(road));
              }
              configToTraffic.add(new TrafficLightConfigJsonStruct(x.getDelay(), roads));
            });
            nodes.add(
                new NodeJsonStruct(
                    node.getX(),
                    node.getY(),
                    roadsFrom,
                    roadsTo,
                    structs,
                    configToTraffic,
                    conn));

          } else {
            nodes.add(
                new NodeJsonStruct(
                    node.getX(),
                    node.getY(),
                    roadsFrom,
                    roadsTo,
                    structs,
                    null,
                    conn));

          }
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
          pointsOfInterest.add(
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
   */
  public void toTrafficMap(TrafficMap trafficMap) {
    RoadSignCreator creator = new RoadSignCreator();
    List<Road> mapRoads = new ArrayList<>();
    List<Node> mapNodes = new ArrayList<>();
    List<PlaceOfInterest> mapPois = new ArrayList<>();
    trafficMap.setWidth(width);
    trafficMap.setHeight(height);
    nodes.forEach(
        node -> {
          Node mapNode = new Node(node.getX(), node.getY());
          List<Spawner> spawners = null;
          if (node.getPeriodsOfSpawn() != null) {
            spawners = new ArrayList<>();
            List<Spawner> finalSpawners = spawners;
            node.getPeriodsOfSpawn().forEach(period ->
                finalSpawners.add(new Spawner(period.getStart(), period.getEnd(), period.getSpawnerRate())));
          }
          mapNode.setSpawners(spawners);
          if (node.getConnector() != null) {
            mapNode.setConnector(new Connector(
                node.getConnector().getRegionId(),
                node.getConnector().getConnectorId()));
          }
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
      if (nodeJsonStruct.getTrafficLight() != null) {
        List<TrafficLightConfig> configs = new ArrayList<>();
        nodeJsonStruct.getTrafficLight().forEach(config -> {
          List<Road> roads = new ArrayList<>();
          config.getRoads().forEach(road -> roads.add(mapRoads.get(road)));
          configs.add(new TrafficLightConfig(config.getDelay(), roads));
        });
        node.setTrafficLight(new TrafficLight(configs));
      }
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

    pointsOfInterest.forEach(
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

    trafficMap.setEnd(end);
    trafficMap.setStart(start);
    trafficMap.setNodes(mapNodes);
    trafficMap.setRoads(mapRoads);
    trafficMap.setPlacesOfInterest(mapPois);
  }
}
