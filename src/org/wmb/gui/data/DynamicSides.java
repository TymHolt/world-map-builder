package org.wmb.gui.data;

public class DynamicSides implements Sides {

    public int top;
    public int bottom;
    public int left;
    public int right;

    public DynamicSides(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public void set(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public void expand(Bounds bounds, DynamicBounds destination) {
        destination.set(
            bounds.getX() - this.left,
            bounds.getY() - this.top,
            bounds.getWidth() + this.left + this.right,
            bounds.getHeight() + this.top + this.bottom
        );
    }

    public void reduce(Bounds bounds, DynamicBounds destination) {
        destination.set(
            bounds.getX() + this.left,
            bounds.getY() + this.top,
            bounds.getWidth() - this.left - this.right,
            bounds.getHeight() - this.top - this.bottom
        );
    }

    @Override
    public int getTop() {
        return this.top;
    }

    @Override
    public int getBottom() {
        return this.bottom;
    }

    @Override
    public int getLeft() {
        return this.left;
    }

    @Override
    public int getRight() {
        return this.right;
    }
}
