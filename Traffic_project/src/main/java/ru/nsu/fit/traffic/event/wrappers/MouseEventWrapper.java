package ru.nsu.fit.traffic.event.wrappers;

import javafx.scene.input.MouseEvent;

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

    public static MouseEventWrapperButton getWrapperButton(MouseEvent event) {
        switch (event.getButton()) {
            case NONE -> {
                return MouseEventWrapperButton.NONE;
            }
            case PRIMARY -> {
                return MouseEventWrapperButton.PRIMARY;
            }
            case SECONDARY -> {
                return MouseEventWrapperButton.SECONDARY;
            }
        }
        return MouseEventWrapperButton.NONE;
    }

    public static MouseEventWrapper getMouseEventWrapper(MouseEvent event) {
        return new MouseEventWrapper(
            event.getX(), event.getY(), getWrapperButton(event)) {
            @Override
            public void consume() {
                event.consume();
            }
        };
    }
}
