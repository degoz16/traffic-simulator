package ru.nsu.fit.dzaikov.traffic.model;

public class Lane {

    public enum roadDirection{
        FORWARD,
        BACK
    }

    private roadDirection dir;
    public Lane(roadDirection dir){
        this.dir = dir;
    }

    public roadDirection getDir(){
        return dir;
    }
}
