package ru.nsu.fit.traffic.controller.statistic;

import ru.nsu.fit.traffic.controller.BaseControl;
import ru.nsu.fit.traffic.controller.SceneElementsControl;
import ru.nsu.fit.traffic.model.map.Node;
import ru.nsu.fit.traffic.model.map.Road;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticControl extends BaseControl {

    private final DFS dfs = new DFS();

    public StatisticControl(
            SceneElementsControl sceneElementsControl) {
        super(sceneElementsControl);
    }

    private class DFS {
        private int n;
        private int[][] graph;
        private boolean[] used;
        private List<Integer> comp;

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
            graph = new int[editOperationsManager.getMap().getRoads().size()][];
            for (Road r : editOperationsManager.getMap().getRoads()) {
                graph[i] = new int[r.getTo().getRoadsOutNum() + r.getFrom().getRoadsInNum()];
                int j = 0;
                for (Road roadTo : (r.getTo().getRoadOutStream().collect(Collectors.toList()))) {
                    graph[i][j++] = editOperationsManager.getMap().getRoads().indexOf(roadTo);
                }
                for (Road roadTo : (r.getFrom().getRoadInStream().collect(Collectors.toList()))) {
                    graph[i][j++] = editOperationsManager.getMap().getRoads().indexOf(roadTo);
                }
                i++;
            }
            /*for (i = 0; i < g.length; ++i){
                System.out.println(Arrays.toString(g[i]));
            }*/
        }

        int find_comps() {
            if (editOperationsManager.getMap().getRoads().size() == 0) return 0;
            createGraph();
            int numberOfComps = 0;
            n = editOperationsManager.getMap().getRoadCount();
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
    public void updateStatistics() {
        int carSpawnersCnt = 0;
        int streetsCnt;
        int roadsCnt;
        int buildingCnt;
        int connectivity;
        List<String> streets = new ArrayList<>();

        //streets.setText(String.valueOf(trafficMap.getStreets().size()));
        streetsCnt = editOperationsManager.getMap().getStreets().size();

        //streetsView.getChildren().clear();
        editOperationsManager.getMap().getStreets().forEach(s -> {
            //Text text = new Text(s.getName());
            //text.setFill(Paint.valueOf("#ffffff"));
            //streetsView.getChildren().add(text);
            streets.add(s.getName());
        });
        roadsCnt = editOperationsManager.getMap().getRoadCount() / 2;
        //this.roads.setText(String.valueOf(roads));
        buildingCnt = editOperationsManager.getMap().getPlaceOfInterest().size();
        //buildings.setText(String.valueOf(trafficMap.getPlaceOfInterest().size()));
        for (Node n : editOperationsManager.getMap().getNodes()) {
            if (n.getSpawners() != null && n.getSpawners().size() != 0) {
                carSpawnersCnt++;
            }
        }
        //carSpawners.setText(String.valueOf(car));
        connectivity = dfs.find_comps();
        //connectivity.setText(String.valueOf());
        sceneElementsControl.updateStatistic(
                carSpawnersCnt,
                streetsCnt,
                roadsCnt,
                buildingCnt,
                connectivity,
                streets
        );
    }
}
