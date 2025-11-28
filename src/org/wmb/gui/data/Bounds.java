package org.wmb.gui.data;

public interface Bounds {

    int getX();
    int getY();
    int getWidth();
    int getHeight();
    boolean contains(int x, int y);
    boolean contains(Position position);
}
