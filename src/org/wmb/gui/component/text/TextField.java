package org.wmb.gui.component.text;

import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.Border;
import org.wmb.gui.input.Cursor;
import org.wmb.rendering.Color;

import java.util.Objects;

public final class TextField extends TextComponent {

    private boolean focused;
    private Color focusBackground;
    private Color focusForeground;
    private Color oldBackground;
    private Color oldForeground;

    public TextField() {
        this("", Align.CENTER);
    }

    public TextField(String text, Align align) {
        super(text, align);
        setBorder(new Border(1, Theme.BORDER));
        this.focused = false;
        setFocusBackground(Theme.BACKGROUND_LIGHT);
        setFocusForeground(Theme.FOREGROUND);
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
    }

    public void setFocusBackground(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.focusBackground = color;
    }

    public Color getFocusBackground() {
        return this.focusBackground;
    }

    public void setFocusForeground(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.focusForeground = color;
    }

    public Color getFocusForeground() {
        return this.focusForeground;
    }

    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
    }

    @Override
    public void onGainFocus() {
        this.focused = true;
        this.oldBackground = getBackground();
        this.oldForeground = getForeground();
        setBackground(this.focusBackground);
        setForeground(this.focusForeground);
    }

    @Override
    public void onLooseFocus() {
        this.focused = false;
        setBackground(this.oldBackground);
        setForeground(this.oldForeground);
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        return Cursor.HAND;
    }
}
