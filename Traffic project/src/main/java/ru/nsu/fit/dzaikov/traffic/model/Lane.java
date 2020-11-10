package ru.nsu.fit.dzaikov.traffic.model;

import java.util.List;
import java.util.Map;

public class Lane {
//Необходимости нет, так как сейчас дороги в разные стороны это разные ребра.
/*
    public enum roadDirection{
        FORWARD,
        BACK
    }
*/

    private List<Map<String, Integer>> signs;

   //private roadDirection dir;
    public Lane(){

    }
/*
    public roadDirection getDir(){
        return dir;
    }
*/
}
