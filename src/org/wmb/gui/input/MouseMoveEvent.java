package org.wmb.gui.input;

public final class MouseMoveEvent {

    public final int xFrom;
    public final int yFrom;
    public final int xTo;
    public final int yTo;

    public MouseMoveEvent(int xFrom, int yFrom, int xTo, int yTo) {
        this.xFrom = xFrom;
        this.yFrom = yFrom;
        this.xTo = xTo;
        this.yTo = yTo;
    }
}
