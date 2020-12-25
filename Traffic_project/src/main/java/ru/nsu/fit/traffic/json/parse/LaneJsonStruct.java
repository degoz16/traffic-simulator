package ru.nsu.fit.traffic.json.parse;

import java.util.List;
import java.util.Map;

public class LaneJsonStruct {
    private List<Map<String, String>> signs;

    public LaneJsonStruct(List<Map<String, String>> signs) {
        this.signs = signs;
    }

    public List<Map<String, String>> getSigns() {
        return signs;
    }
}
