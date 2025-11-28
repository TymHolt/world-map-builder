package org.wmb.gui.data;

public class DynamicBounds implements Bounds {

    public final DynamicPosition position;
    public final DynamicSize size;

    public DynamicBounds(int x, int y, int width, int height) {
        this.position = new DynamicPosition(x, y);
        this.size = new DynamicSize(width, height);
    }

    public void set(int x, int y, int width, int height) {
        this.position.x = x;
        this.position.y = y;
        this.size.width = width;
        this.size.height = height;
    }

    @Override
    public int getX() {
        return this.position.x;
    }

    @Override
    public int getY() {
        return this.position.y;
    }

    @Override
    public int getWidth() {
        return this.size.width;
    }

    @Override
    public int getHeight() {
        return this.size.height;
    }

    @Override
    public boolean contains(int x, int y) {
        final int minX = position.x;;
        final int minY = position.y;
        final int maxX = position.x + size.width;
        final int maxY = position.y + size.height;
        return x >= minX && y >= minY && x < maxX && y < maxY;
    }

    @Override
    public boolean contains(Position position) {
        return contains(position.getX(), position.getY());
    }
}
