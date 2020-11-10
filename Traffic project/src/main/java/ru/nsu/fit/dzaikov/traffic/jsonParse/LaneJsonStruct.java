package ru.nsu.fit.dzaikov.traffic.jsonParse;

import java.util.List;
import java.util.Map;

public class LaneJsonStruct {
    private List<Map<String, String>> signs;

    public LaneJsonStruct(List<Map<String, String>> signs) {
        this.signs = signs;
    }
}
