package org.wmb.gui;

import org.wmb.gui.component.Component;
import org.wmb.gui.font.FontDefinition;
import org.wmb.rendering.Color;

import java.awt.*;
import java.util.Objects;

public final class BadgeCharComponent extends Component {

    private Color foreground;
    private FontDefinition fontDefinition;

    public BadgeCharComponent(char c, Color background) {
        Objects.requireNonNull(background);

        setForeground(Color.WHITE);
        setBackground(background);
    }

    public void setForeground(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.foreground = color;
    }

    public void setFont(FontDefinition font) {
        Objects.requireNonNull(font, "Font is null");
        this.fontDefinition = font;
    }

    @Override
    public Dimension getRequestedSize() {

        return super.getRequestedSize();
    }
}
