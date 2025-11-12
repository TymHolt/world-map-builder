package org.wmb.gui.component;

import org.wmb.core.WmbGraphics;
import org.wmb.rendering.Color;

import java.awt.*;
import java.util.Objects;

public class Component {

    private final Rectangle bounds;
    private Color background;
    private Border border;

    public Component() {
        this.bounds = new Rectangle(0, 0, 1, 1);
        this.background = Color.BLACK;
        this.border = new Border();
    }

    public void setBounds(Rectangle bounds) {
        Objects.requireNonNull(bounds, "Bounds is null");
        this.bounds.setBounds(bounds);
    }

    public void setBounds(int x, int y, int width, int height) {
        this.bounds.setBounds(x, y, width, height);
    }

    public void setBackground(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.background = color;
    }

    public void setBorder(Border border) {
        Objects.requireNonNull(border, "Border is null");
        this.border = new Border(border);
    }

    public void draw(WmbGraphics graphics) {
        Objects.requireNonNull(graphics, "Graphics is null");
        graphics.fillQuadColor(this.border.getInner(bounds), this.background);
        this.border.draw(graphics, this.bounds);
    }
}
