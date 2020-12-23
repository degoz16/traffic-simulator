package ru.nsu.fit.traffic.model.congestion;

import java.util.List;

public class ReportWindowStruct {
    private int start;
    private int end;
    private List<CongestionStruct> congestionList;
    private List<Integer> congestionListFilled;

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

    public List<Integer> getCongestionListFilled() {
        return congestionListFilled;
    }

    public void setCongestionListFilled(List<Integer> congestionListFilled) {
        this.congestionListFilled = congestionListFilled;
    }

    public List<CongestionStruct> getCongestionList() {
        return congestionList;
    }

    public void setCongestionList(List<CongestionStruct> congestionList) {
        this.congestionList = congestionList;
    }
}
