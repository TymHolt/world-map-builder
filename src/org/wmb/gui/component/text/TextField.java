package org.wmb.gui.component.text;

import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.Border;
import org.wmb.gui.input.Cursor;

public final class TextField extends TextComponent {

    public TextField() {
        this("", Align.CENTER);
    }

    public TextField(String text, Align align) {
        super(text, align);
        setBorder(new Border(1, Theme.BORDER));
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        return Cursor.HAND;
    }
}
