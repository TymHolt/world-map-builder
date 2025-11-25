package org.wmb.gui.component;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.font.FontDefinition;
import org.wmb.rendering.Color;

import java.awt.*;
import java.util.Objects;

public class Label extends Component {

    private String text;
    private Color foreground;
    private Align align;
    private FontDefinition fontDefinition;

    public Label(String text, Align align) {
        this(text, align, Theme.FONT_PLAIN);
    }

    public Label(String text, Align align, FontDefinition fontDefinition) {
        setBackground(Color.TRANSPARENT);
        setText(text);
        setForeground(Theme.FOREGROUND);
        setAlign(align);
        setFont(fontDefinition);
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

    public void setFont(FontDefinition fontDefinition) {
        Objects.requireNonNull(fontDefinition, "Font is null");
        this.fontDefinition = fontDefinition;
    }

    @Override
    public Dimension getRequestedSize() {
        return this.fontDefinition.getTextSize(this.text);
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);

        final Dimension textSize = this.fontDefinition.getTextSize(this.text);
        final Rectangle innerBounds = getBorder().getInner(getBounds());
        final int x = innerBounds.x;
        final int y = innerBounds.y + ((innerBounds.height - textSize.height) / 2);

        switch (align) {
            case LEFT -> graphics.fillText(this.text, x, y, this.foreground, this.fontDefinition);
            case RIGHT -> graphics.fillText(this.text, x + (innerBounds.width - textSize.width),
                y, this.foreground, this.fontDefinition);
            case CENTER -> graphics.fillText(this.text,
                x + (innerBounds.width - textSize.width) / 2, y, this.foreground, this.fontDefinition);
        }
    }
}
