package ru.nsu.fit.traffic.model;

import java.util.List;

public class Lane {
//Необходимости нет, так как сейчас дороги в разные стороны это разные ребра.
/*
    public enum roadDirection{
        FORWARD,
        BACK
    }
*/

    private List<RoadSign> signs;

   //private roadDirection dir;
    public Lane(){

    }

    public List<RoadSign> getSigns() {
        return signs;
    }

/*
    public roadDirection getDir(){
        return dir;
    }
*/
}
