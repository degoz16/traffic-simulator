package ru.nsu.fit.traffic.event.wrappers;

public abstract class MouseEventWrapper {
    private final double x;
    private final double y;
    private final MouseEventWrapperButton button;

    public MouseEventWrapper(double x, double y, MouseEventWrapperButton button) {
        this.x = x;
        this.y = y;
        this.button = button;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public MouseEventWrapperButton getButton(){
        return button;
    }
    public abstract void consume();

}
