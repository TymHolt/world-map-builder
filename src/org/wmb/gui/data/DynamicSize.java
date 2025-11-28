package org.wmb.gui.data;

public class DynamicSize implements Size {

    public int width;
    public int height;

    public DynamicSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void set(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void set(Size size) {
        set(size.getWidth(), size.getHeight());
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}
