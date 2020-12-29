package ru.nsu.fit.traffic.model.map;

import java.util.ArrayList;
import java.util.List;

public class Lane {

    private List<RoadSign> signs;

    public Lane() {
        signs = new ArrayList<>();
    }

    public Lane getCopyLane() {
        Lane copyLane = new Lane();
        signs.forEach(sign -> copyLane.addSign(sign.getCopySign()));
        return copyLane;
    }

    public List<RoadSign> getSigns() {
        return signs;
    }

    public void addSign(RoadSign sign) {
        //Не больше одного знака определенного типа
        signs.removeIf(s -> s.getSignType() == sign.getSignType());
        signs.add(sign);
    }

}
