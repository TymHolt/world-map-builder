package org.wmb.rendering;

public final class DynamicColor implements Color {

    private float red, green, blue, alpha;

    public DynamicColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public void set(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getRed() {
        return this.red;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getGreen() {
        return this.green;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public float getBlue() {
        return this.blue;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return this.alpha;
    }
}
