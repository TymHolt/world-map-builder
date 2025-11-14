package org.wmb.gui.component;

import org.wmb.core.gui.GuiGraphics;
import org.wmb.rendering.Color;

import java.awt.*;
import java.util.Objects;

public class Border {

    private Color color;
    private int top;
    private int bottom;
    private int left;
    private int right;

    public Border() {
        this(0, Color.WHITE);
    }

    public Border(int thickness, Color color) {
        setColor(color);
        setTop(thickness);
        setBottom(thickness);
        setLeft(thickness);
        setRight(thickness);
    }

    public Border(Border border) {
        this.color = border.color;
        this.top = border.top;
        this.bottom = border.bottom;;
        this.left = border.left;
        this.right = border.right;
    }

    public void setColor(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.color = color;
    }

    public void setTop(int thickness) {
        this.top = Math.max(thickness, 0);
    }

    public void setBottom(int thickness) {
        this.bottom = Math.max(thickness, 0);
    }

    public void setLeft(int thickness) {
        this.left = Math.max(thickness, 0);
    }

    public void setRight(int thickness) {
        this.right = Math.max(thickness, 0);
    }

    public Rectangle getInner(Rectangle bounds) {
        Objects.requireNonNull(bounds, "Bounds is null");

        final int x = Math.max(bounds.x + left, 0);
        final int y = Math.max(bounds.y + top, 0);
        final int width = Math.max(bounds.width - left - right, 0);
        final int height = Math.max(bounds.height - top - bottom, 0);
        return new Rectangle(x, y, width, height);
    }

    public void draw(GuiGraphics graphics, Rectangle bounds) {
        Objects.requireNonNull(graphics, "Graphics is null");
        Objects.requireNonNull(bounds, "Bounds is null");

        if (top > 0)
            graphics.fillQuadColor(bounds.x, bounds.y, bounds.width, top, this.color);

        if (bottom > 0)
            graphics.fillQuadColor(bounds.x, bounds.y + bounds.height - bottom, bounds.width,
                bottom, this.color);

        if (left > 0)
            graphics.fillQuadColor(bounds.x, bounds.y, left, bounds.height, this.color);

        if (right > 0)
            graphics.fillQuadColor(bounds.x + bounds.width - right, bounds.y, right, bounds.height,
                this.color);
    }
}
