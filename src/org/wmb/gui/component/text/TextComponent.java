package org.wmb.gui.component.text;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.Border;
import org.wmb.gui.component.Component;
import org.wmb.gui.font.FontDefinition;
import org.wmb.rendering.Color;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Objects;

public abstract class TextComponent extends Component {

    private Align align;
    private String text;
    private Color foreground;
    private FontDefinition font;

    public TextComponent(String text, Align align) {
        setText(text);
        setAlign(align);
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
        setFont(Theme.FONT_PLAIN);
        setBorder(new Border());
    }

    public void setText(String text) {
        if (text == null)
            text = "null";

        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setForeground(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.foreground = color;
    }

    public Color getForeground() {
        return this.foreground;
    }

    public void setAlign(Align align) {
        Objects.requireNonNull(align, "Align is null");
        this.align = align;
    }

    public Align getAlign() {
        return this.align;
    }

    public void setFont(FontDefinition font) {
        Objects.requireNonNull(font, "Font is null");
        this.font = font;
    }

    public FontDefinition getFont() {
        return this.font;
    }

    @Override
    public Dimension getRequestedSize() {
        return this.font.getTextSize(this.text);
    }

    protected Point getTextDrawLocation() {
        final Dimension textSize = this.font.getTextSize(this.text);
        final Rectangle innerBounds = getBorder().getInner(getBounds());
        final int x = innerBounds.x;
        final int textHeight = textSize.height != 0 ? textSize.height : innerBounds.height;
        final int y = innerBounds.y + ((innerBounds.height - textHeight) / 2);

        return switch (align) {
            case LEFT -> new Point(x, y);
            case RIGHT -> new Point(x + (innerBounds.width - textSize.width), y);
            case CENTER -> new Point(x + (innerBounds.width - textSize.width) / 2, y);
        };
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);
        final Point drawLocation = getTextDrawLocation();
        graphics.fillText(this.text, drawLocation.x, drawLocation.y, this.foreground, this.font);
    }
}