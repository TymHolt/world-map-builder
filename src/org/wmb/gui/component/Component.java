package org.wmb.gui.component;

import org.wmb.gui.data.Bounds;
import org.wmb.gui.data.DynamicBounds;
import org.wmb.gui.data.DynamicSides;
import org.wmb.gui.data.DynamicSize;
import org.wmb.gui.data.Sides;
import org.wmb.gui.Theme;
import org.wmb.gui.input.Cursor;
import org.wmb.gui.input.KeyClickEvent;
import org.wmb.gui.input.MouseClickEvent;
import org.wmb.gui.input.MouseMoveEvent;
import org.wmb.gui.GuiGraphics;
import org.wmb.gui.input.MouseScrollEvent;
import org.wmb.rendering.Color;

import java.util.Objects;

public abstract class Component {

    private final DynamicBounds bounds;
    private final DynamicBounds innerBounds;
    private final DynamicBounds outerBounds;
    private final DynamicSides padding;
    private final DynamicSides margin;
    private final DynamicSides border;
    private Color borderColor;
    private Color background;

    public Component() {
        this.bounds = new DynamicBounds(0, 0, 1, 1);
        this.innerBounds = new DynamicBounds(0, 0, 1, 1);
        this.outerBounds = new DynamicBounds(0, 0, 1, 1);
        this.padding = new DynamicSides(0, 0, 0, 0);
        this.margin = new DynamicSides(0, 0, 0, 0);
        this.border = new DynamicSides(0, 0, 0, 0);
        this.borderColor = Theme.BORDER;
        this.background = Theme.BACKGROUND;
        recalculateBounds();
    }

    public void setBounds(Bounds bounds) {
        setBounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    public void setBounds(int x, int y, int width, int height) {
        this.bounds.set(x, y, width, height);
        recalculateBounds();
    }

    public void setX(int x) {
        this.bounds.position.x = x;
        recalculateBounds();
    }

    public void setY(int y) {
        this.bounds.position.y = y;
        recalculateBounds();
    }

    public void setWidth(int width) {
        this.bounds.size.width = width;
        recalculateBounds();
    }

    public void setHeight(int height) {
        this.bounds.size.height = height;
        recalculateBounds();
    }

    public void setBorder(Sides border) {
        setBorder(border.getTop(), border.getBottom(), border.getLeft(), border.getRight());
    }

    public void setBorder(int top, int bottom, int left, int right) {
        this.border.set(top, bottom, left, right);
        recalculateBounds();
    }

    public void setBorderTop(int top) {
        this.border.top = top;
        recalculateBounds();
    }

    public void setBorderBottom(int bottom) {
        this.border.bottom = bottom;
        recalculateBounds();
    }

    public void setBorderLeft(int left) {
        this.border.left = left;
        recalculateBounds();
    }

    public void setBorderRight(int right) {
        this.border.right = right;
        recalculateBounds();
    }

    public void setPadding(Sides padding) {
        setPadding(padding.getTop(), padding.getBottom(), padding.getLeft(), padding.getRight());
    }

    public void setPadding(int top, int bottom, int left, int right) {
        this.padding.set(top, bottom, left, right);
    }

    public void setPaddingTop(int top) {
        this.padding.top = top;
        recalculateBounds();
    }

    public void setPaddingBottom(int bottom) {
        this.padding.bottom = bottom;
        recalculateBounds();
    }

    public void setPaddingLeft(int left) {
        this.padding.left = left;
        recalculateBounds();
    }

    public void setPaddingRight(int right) {
        this.padding.right = right;
        recalculateBounds();
    }

    public void setMargin(Sides margin) {
        setMargin(margin.getTop(), margin.getBottom(), margin.getLeft(), margin.getRight());
    }

    public void setMargin(int top, int bottom, int left, int right) {
        this.margin.set(top, bottom, left, right);
    }

    public void setMarginTop(int top) {
        this.margin.top = top;
        recalculateBounds();
    }

    public void setMarginBottom(int bottom) {
        this.margin.bottom = bottom;
        recalculateBounds();
    }

    public void setMarginLeft(int left) {
        this.margin.left = left;
        recalculateBounds();
    }

    public void setMarginRight(int right) {
        this.margin.right = right;
        recalculateBounds();
    }

    private void recalculateBounds() {
        this.border.reduce(this.bounds, this.innerBounds);
        this.padding.reduce(this.innerBounds, this.innerBounds);
        this.margin.expand(this.bounds, this.outerBounds);
    }

    public Bounds getBounds() {
        return this.bounds;
    }

    public Bounds getInnerBounds() {
        return this.innerBounds;
    }

    public Bounds getOuterBounds() {
        return this.outerBounds;
    }

    public Sides getBorder() {
        return this.border;
    }

    public Sides getPadding() {
        return this.padding;
    }

    public Sides getMargin() {
        return this.margin;
    }

    public void setBackground(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.background = color;
    }

    public Color getBackground() {
        return this.background;
    }

    public void setBorderColor(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.borderColor = color;
    }

    public Color getBorderColor() {
        return this.borderColor;
    }

    public void getRequestedSize(DynamicSize destination) {
        destination.width = this.outerBounds.size.width;
        destination.height = this.outerBounds.size.height;
    }

    public DynamicSize getRequestedSize() {
        final DynamicSize requestedSize = new DynamicSize(0, 0);
        getRequestedSize(requestedSize);
        return requestedSize;
    }

    public void draw(GuiGraphics graphics) {
        graphics.fillQuadColor(this.bounds, this.background);

        if (this.border.top > 0) {
            graphics.fillQuadColor(
                this.bounds.position.x,
                this.bounds.position.y,
                this.bounds.size.width,
                this.border.top,
                this.borderColor
            );
        }

        if (this.border.bottom > 0) {
            graphics.fillQuadColor(
                this.bounds.position.x,
                this.bounds.position.y + this.bounds.size.height - this.border.bottom,
                this.bounds.size.width,
                this.border.bottom,
                this.borderColor
            );
        }

        if (this.border.left > 0) {
            graphics.fillQuadColor(
                this.bounds.position.x,
                this.bounds.position.y,
                this.border.left,
                this.bounds.size.height,
                this.borderColor
            );
        }

        if (this.border.right > 0) {
            graphics.fillQuadColor(
                this.bounds.position.x + this.bounds.size.width - this.border.right,
                this.bounds.position.y,
                this.border.right,
                this.bounds.size.height,
                this.borderColor
            );
        }
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
