package org.wmb.gui.input;

public enum ScrollDirection {

    UP,
    DOWN;

    public static ScrollDirection fromValue(double direction) {
        if (direction < 0)
            return DOWN;
        else
            return UP;
    }
}
