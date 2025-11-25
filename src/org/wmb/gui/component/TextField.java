package org.wmb.gui.component;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.input.Cursor;
import org.wmb.rendering.Color;

import java.awt.*;
import java.util.Objects;

public final class TextField extends Component {

    private Align align;
    private String text;
    private Color foreground;
    private Dimension lastTextSize;

    public TextField() {
        this("", Align.CENTER);
    }

    public TextField(String text, Align align) {
        setText(text);
        setAlign(align);
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
        this.lastTextSize = new Dimension(1, 1);
        setBorder(new Border(1, Theme.BORDER));
    }

    public void setText(String text) {
        if (text == null)
            text = "null";

        this.text = text;
    }

    public void setForeground(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.foreground = color;
    }

    public void setAlign(Align align) {
        Objects.requireNonNull(align, "Align is null");
        this.align = align;
    }

    @Override
    public Dimension getRequestedSize() {
        return Theme.FONT_PLAIN.getTextSize(this.text);
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);

        final Dimension textSize = Theme.FONT_PLAIN.getTextSize(this.text);
        this.lastTextSize = textSize;
        final Rectangle innerBounds = getBorder().getInner(getBounds());
        final int x = innerBounds.x;
        final int y = innerBounds.y + ((innerBounds.height - textSize.height) / 2);

        switch (align) {
            case LEFT -> graphics.fillText(this.text, x, y, this.foreground, Theme.FONT_PLAIN);
            case RIGHT -> graphics.fillText(this.text, x + (innerBounds.width - textSize.width),
                y, this.foreground, Theme.FONT_PLAIN);
            case CENTER -> graphics.fillText(this.text,
                x + (innerBounds.width - textSize.width) / 2, y, this.foreground, Theme.FONT_PLAIN);
        }
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        return Cursor.HAND;
    }
}
