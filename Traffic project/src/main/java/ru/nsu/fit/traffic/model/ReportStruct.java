package ru.nsu.fit.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class ReportStruct {
    private List<ReportWindowStruct> windowList = new ArrayList<>();

    public List<ReportWindowStruct> getWindowList() {
        return windowList;
    }

    public void setWindowList(List<ReportWindowStruct> windowList) {
        this.windowList = windowList;
    }
}
