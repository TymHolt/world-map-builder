package org.wmb.gui.component;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.rendering.Color;

import java.awt.*;
import java.util.Objects;

public class Label extends Component {

    private String text;
    private Color foreground;
    private Align align;
    private Dimension lastTextSize;
    private boolean bold;

    public Label(String text, Align align) {
        this(text, align, false);
    }

    public Label(String text, Align align, boolean bold) {
        setBackground(Color.TRANSPARENT);
        setText(text);
        setForeground(Theme.FOREGROUND);
        setAlign(align);
        this.lastTextSize = new Dimension(1, 1);
        this.bold = bold;
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

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    @Override
    public Dimension getRequestedSize() {
        return this.lastTextSize;
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);

        final Dimension textSize = graphics.getTextSize(this.text, this.bold);
        this.lastTextSize = textSize;
        final Rectangle innerBounds = getBorder().getInner(getBounds());
        final int x = innerBounds.x;
        final int y = innerBounds.y + ((innerBounds.height - textSize.height) / 2);

        switch (align) {
            case LEFT -> graphics.fillText(this.text, x, y, this.foreground, this.bold);
            case RIGHT -> graphics.fillText(this.text, x + (innerBounds.width - textSize.width),
                y, this.foreground, this.bold);
            case CENTER -> graphics.fillText(this.text,
                x + (innerBounds.width - textSize.width) / 2, y, this.foreground, this.bold);
        }
    }
}
