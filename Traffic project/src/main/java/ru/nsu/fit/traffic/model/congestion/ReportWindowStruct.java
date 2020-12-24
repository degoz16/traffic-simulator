package ru.nsu.fit.traffic.model.congestion;

import java.util.List;

public class ReportWindowStruct {
    private long start;
    private long end;
    private List<CongestionStruct> congestionList;
    private List<Integer> congestionListFilled;

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
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
