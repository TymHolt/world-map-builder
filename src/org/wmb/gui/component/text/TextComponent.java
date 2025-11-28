package org.wmb.gui.component.text;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.Component;
import org.wmb.gui.data.Bounds;
import org.wmb.gui.data.DynamicPosition;
import org.wmb.gui.data.DynamicSize;
import org.wmb.gui.data.Position;
import org.wmb.gui.font.FontDefinition;
import org.wmb.rendering.Color;

import java.util.Objects;

public abstract class TextComponent extends Component {

    private final DynamicSize textSize;
    private final DynamicPosition textPosition;
    private Align align;
    private String text;
    private Color foreground;
    private FontDefinition font;

    public TextComponent(String text, Align align) {
        super();
        this.textSize = new DynamicSize(0, 0);
        this.textPosition = new DynamicPosition(0, 0);

        // Default values to enable recalculations calling the following setters
        this.text = "";
        this.align = Align.CENTER;

        setFont(Theme.FONT_PLAIN);
        setAlign(align);
        setText(text);
        setForeground(Theme.FOREGROUND);
    }

    public void setText(String text) {
        if (text == null)
            text = "null";

        this.text = text;
        recalculateTextSize();
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
        recalculateTextPosition();
    }

    public Align getAlign() {
        return this.align;
    }

    public void setFont(FontDefinition font) {
        Objects.requireNonNull(font, "Font is null");
        this.font = font;
        recalculateTextSize();
    }

    public FontDefinition getFont() {
        return this.font;
    }

    private void recalculateTextSize() {
        this.font.getTextSize(this.text, this.textSize);
        recalculateTextPosition();
    }

    private void recalculateTextPosition() {
        final Bounds innerBounds = getInnerBounds();
        final int innerBoundsWidth = innerBounds.getWidth();
        final int innerBoundsHeight = innerBounds.getHeight();

        final int x = innerBounds.getX();
        final int textHeight = this.textSize.height != 0 ? this.textSize.height : innerBoundsHeight;
        final int y = innerBounds.getY() + ((innerBoundsHeight - textHeight) / 2);

        switch (align) {
            case LEFT -> this.textPosition.set(x, y);
            case RIGHT -> this.textPosition.set(x + (innerBoundsWidth - this.textSize.width), y);
            case CENTER -> this.textPosition.set(
                x + (innerBoundsWidth - this.textSize.width) / 2, y);
        };
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        recalculateTextPosition();
    }

    @Override
    public void getRequestedSize(DynamicSize destination) {
        destination.set(this.textSize);
    }

    protected Position getTextPosition() {
        return this.textPosition;
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);
        graphics.fillText(this.text, this.textPosition.x, this.textPosition.y, this.foreground,
            this.font);
    }
}