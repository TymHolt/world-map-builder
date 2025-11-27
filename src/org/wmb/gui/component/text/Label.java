package org.wmb.gui.component.text;

import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.font.FontDefinition;
import org.wmb.rendering.Color;

public class Label extends TextComponent {

    public Label(String text, Align align) {
        this(text, align, Theme.FONT_PLAIN);
    }

    public Label(String text, Align align, FontDefinition fontDefinition) {
        super(text, align);
        setFont(fontDefinition);
        setBackground(Color.TRANSPARENT);
    }
}
