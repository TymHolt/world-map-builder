package org.wmb.rendering;

public final class Color {

    public static final Color TRANSPARENT = new Color(0.0f, 0.0f, 0.0f, 0.0f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color GREY_DARK = new Color(0.34f, 0.34f, 0.34f, 1.0f);
    public static final Color GREY= new Color(0.5f, 0.5f, 0.5f, 1.0f);
    public static final Color GREY_LIGHT = new Color(0.67f, 0.67f, 0.67f, 1.0f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);

    private final float red, green, blue, alpha;

    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getRed() {
        return this.red;
    }

    public float getGreen() {
        return this.green;
    }

    public float getBlue() {
        return this.blue;
    }

    public float getAlpha() {
        return this.alpha;
    }
}
