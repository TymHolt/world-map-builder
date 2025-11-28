package org.wmb.gui.component;

import org.wmb.gui.input.Cursor;
import org.wmb.gui.input.KeyClickEvent;
import org.wmb.gui.input.MouseClickEvent;
import org.wmb.gui.input.MouseMoveEvent;
import org.wmb.gui.GuiGraphics;
import org.wmb.gui.input.MouseScrollEvent;
import org.wmb.rendering.Color;

import java.awt.*;
import java.util.Objects;

public abstract class Component {

    private final Rectangle bounds;
    private Color background;
    private Border border;

    public Component() {
        this.bounds = new Rectangle(0, 0, 1, 1);
        this.background = Color.BLACK;
        this.border = Border.none();
    }

    public void setBounds(Rectangle bounds) {
        Objects.requireNonNull(bounds, "Bounds is null");
        setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void setBounds(int x, int y, int width, int height) {
        this.bounds.setBounds(x, y, width, height);
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    public void setBackground(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.background = color;
    }

    public Color getBackground() {
        return this.background;
    }

    public void setBorder(Border border) {
        Objects.requireNonNull(border, "Border is null");
        this.border = new Border(border);
    }

    public Border getBorder() {
        return this.border;
    }

    public Rectangle getInnerBounds() {
        return this.border.getInner(this.bounds);
    }

    public Dimension getRequestedSize() {
        return new Dimension(1, 1);
    }

    public void draw(GuiGraphics graphics) {
        Objects.requireNonNull(graphics, "Graphics is null");
        graphics.fillQuadColor(this.border.getInner(bounds), this.background);
        this.border.draw(graphics, this.bounds);
    }

    public void onMouseClick(MouseClickEvent event) {

    }

    public void onMouseMove(MouseMoveEvent event) {

    }

    public void onGainFocus() {

    }

    public void onLooseFocus() {

    }

    public void onMouseScroll(MouseScrollEvent event) {

    }

    public Cursor getCursor(int mouseX, int mouseY) {
        return Cursor.DEFAULT;
    }

    public void onTextInput(char c) {

    }

    public void onKeyClick(KeyClickEvent event) {

    }

    public boolean handleTabThrough() {
        return false;
    }
}
