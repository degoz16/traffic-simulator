package ru.nsu.fit.traffic.model;

import java.util.List;

public class ReportWindowStruct {
    private int start;
    private int end;
    private List<Integer> congestionList;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<Integer> getCongestionList() {
        return congestionList;
    }

    public void setCongestionList(List<Integer> congestionList) {
        this.congestionList = congestionList;
    }
}
