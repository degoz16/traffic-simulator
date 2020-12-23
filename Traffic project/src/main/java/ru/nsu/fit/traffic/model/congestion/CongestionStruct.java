package ru.nsu.fit.traffic.model.congestion;

public class CongestionStruct {
    private int roadId = 0;
    private int congestion = 0;

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public int getCongestion() {
        return congestion;
    }

    public void setCongestion(int congestion) {
        this.congestion = congestion;
    }
}
