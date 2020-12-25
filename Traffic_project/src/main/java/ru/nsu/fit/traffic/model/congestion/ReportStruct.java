package ru.nsu.fit.traffic.model.congestion;

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

    public void fillCongestionList(int roadCnt) {
        if (windowList.size() > 0) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < roadCnt; i++) {
                list.add(0);
            }
            windowList.get(0).setCongestionListFilled(list);
            windowList.get(0).getCongestionList().forEach(
                    c -> {
                        if (c.getRoadId() < roadCnt) {
                            windowList.get(0).getCongestionListFilled().set(c.getRoadId(), c.getCongestion());
                        }
                    });
        }
        for (int i = 1; i < windowList.size(); i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < roadCnt; j++) {
                list.add(windowList.get(i - 1).getCongestionListFilled().get(j));
            }
            windowList.get(i).setCongestionListFilled(list);
            final int i1 = i;
            windowList.get(i).getCongestionList().forEach(
                    c -> {
                        if(c.getRoadId() < roadCnt) {
                            windowList.get(i1).getCongestionListFilled().set(c.getRoadId(), c.getCongestion());
                        }
                    });
        }
    }
}
