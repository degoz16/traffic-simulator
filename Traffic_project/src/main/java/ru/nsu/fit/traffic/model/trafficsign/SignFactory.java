package ru.nsu.fit.traffic.model.trafficsign;

public class SignFactory {
    public RoadSign getMainRoadSign() {
        return new MainRoadSign();
    }

    public RoadSign getSpeedLimitSign(int limit) {
        return new SpeedLimitSign(limit);
    }
}
