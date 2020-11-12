package ru.nsu.fit.traffic.model;

import java.util.List;

public class TrafficLight {
    private int delayGreen;
    private int delayRed;
    //Тут по соответствующему индексу дороги из списков fromRoads и toRoads (см. Node)
    //будет лежать индекс парной ей дороги, каждая такая пара регулируется светофором,
    //то есть по этой паре ездят машины когда горит зеленый.
    private List<Integer> pairsOfRoad;

    public TrafficLight(int delayGreen, int delayRed, List<Integer> pairsOfRoad) {
        this.delayGreen = delayGreen;
        this.delayRed = delayRed;
        this.pairsOfRoad = pairsOfRoad;
    }
}
