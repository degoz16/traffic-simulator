package ru.nsu.fit.traffic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import ru.nsu.fit.traffic.model.TrafficMap;
import ru.nsu.fit.traffic.model.node.Node;
import ru.nsu.fit.traffic.model.road.Road;
import ru.nsu.fit.traffic.model.road.Street;

import java.util.ArrayList;
import java.util.List;

public class StatisticsController {
    @FXML
    private Text carSpawners;
    @FXML
    private Text streets;
    @FXML
    private Text roads;
    @FXML
    private Text buildings;
    @FXML
    private Text connectivity;
    @FXML
    private VBox streetsView;
    @FXML
    private Pane statistics;
    @FXML
    private ScrollPane scrollView;
    private MainController mainController;
    private TrafficMap trafficMap;
    private DFS dfs = new DFS();
    @FXML
    public void initialize() {
        scrollView.setStyle("-fx-background: #454545;\n -fx-background-color: #454545");
        scrollView.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollView.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        carSpawners.setFill(Paint.valueOf("white"));
        streets.setFill(Paint.valueOf("white"));
        roads.setFill(Paint.valueOf("white"));
        buildings.setFill(Paint.valueOf("white"));
        connectivity.setFill(Paint.valueOf("white"));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        this.trafficMap = mainController.getCurrMap();
    }

    public Pane getStatistics() {
        return statistics;
    }

    public void updateStatistics() {
        int car = 0;
        streets.setText(String.valueOf(trafficMap.getStreets().size()));
        streetsView.getChildren().clear();
        for (Street s : trafficMap.getStreets()) {
            Text text = new Text(s.getName());
            text.setFill(Paint.valueOf("#ffffff"));
            streetsView.getChildren().add(text);
        }
        int roads = 0;
        for (int i = 0; i < trafficMap.getRoadCount(); i++) {
            Road r = trafficMap.getRoad(i);
            if (trafficMap.getRoads().contains(r.getBackRoad()) && trafficMap.getRoads().indexOf(r.getBackRoad()) < i)
                continue;
            roads++;
        }
        this.roads.setText(String.valueOf(roads));
        buildings.setText(String.valueOf(trafficMap.getPlaceOfInterest().size()));
        for (Node n : trafficMap.getNodes()) {
            if (n.getSpawners() != null && n.getSpawners().size() != 0) {
                car++;
            }
        }
        carSpawners.setText(String.valueOf(car));
        connectivity.setText(String.valueOf(dfs.find_comps()));
    }

    private class DFS {
        int n;
        int[][] graph;
        boolean[] used;
        List<Integer> comp;

        void dfs(int v) {
            used[v] = true;
            comp.add(v);
            for (int i = 0; i < graph[v].length; ++i) {
                int to = graph[v][i];
                if (!used[to])
                    dfs(to);
            }
        }

        void createGraph() {
            int i = 0;
            graph = new int[trafficMap.getRoads().size()][];
            for (Road r : mainController.getCurrMap().getRoads()) {
                graph[i] = new int[r.getTo().getRoadsOutNum() + r.getFrom().getRoadsInNum()];
                int j = 0;
                for (Object roadTo : (r.getTo().getRoadOutStream().toArray())) {
                    graph[i][j++] = mainController.getCurrMap().getRoads().indexOf(roadTo);
                }
                for (Object roadTo : (r.getFrom().getRoadInStream().toArray())) {
                    graph[i][j++] = mainController.getCurrMap().getRoads().indexOf(roadTo);
                }
                i++;
            }
            /*for (i = 0; i < g.length; ++i){
                System.out.println(Arrays.toString(g[i]));
            }*/
        }

        int find_comps() {
            if (mainController.getCurrMap().getRoads().size() == 0) return 0;
            createGraph();
            int numberOfComps = 0;
            n = trafficMap.getRoadCount();
            used = new boolean[n];
            comp = new ArrayList<>();
            for (int i = 0; i < n; ++i)
                if (!used[i]) {
                    comp.clear();
                    numberOfComps++;
                    dfs(i);
                }
            //System.out.println(comp.toString());
            return numberOfComps;
        }
    }
}
