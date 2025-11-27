package org.wmb.gui.component.text;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.Border;
import org.wmb.gui.font.FontDefinition;
import org.wmb.gui.input.ClickAction;
import org.wmb.gui.input.Cursor;
import org.wmb.gui.input.KeyClickEvent;
import org.wmb.rendering.Color;

import java.awt.Point;
import java.util.Objects;

public class TextField extends TextComponent {

    private int textCursorLocation;
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
        setFocusBackground(Theme.BACKGROUND_LIGHT);
        setFocusForeground(Theme.FOREGROUND);
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
        this.textCursorLocation = -1;
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
        this.oldBackground = getBackground();
        this.oldForeground = getForeground();
        setBackground(this.focusBackground);
        setForeground(this.focusForeground);
        this.textCursorLocation = 0;
    }

    @Override
    public void onLooseFocus() {
        setBackground(this.oldBackground);
        setForeground(this.oldForeground);
        this.textCursorLocation = -1;
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        return Cursor.HAND;
    }

    private void setCursorLocation(int index) {
        this.textCursorLocation = Math.max(0, Math.min(this.getText().length(), index));
    }

    @Override
    public void onTextInput(char c) {
        final String text = getText();
        final String preCursor = text.substring(0, this.textCursorLocation);
        final String postCursor = text.substring(this.textCursorLocation);
        setText(preCursor + c + postCursor);
        setCursorLocation(this.textCursorLocation + 1);
    }

    @Override
    public void onKeyClick(KeyClickEvent event) {
        if (event.action != ClickAction.PRESS && event.action != ClickAction.REPEAT)
            return;

        final String text = getText();
        switch (event.button) {
            case LEFT:
                setCursorLocation(this.textCursorLocation - 1);
                break;
            case RIGHT:
                setCursorLocation(this.textCursorLocation + 1);
                break;
            case BACKSPACE:
                if (this.textCursorLocation > 0) {
                    final String preCursor = text.substring(0, this.textCursorLocation - 1);
                    final String postCursor = text.substring(this.textCursorLocation);
                    setText(preCursor + postCursor);
                    setCursorLocation(this.textCursorLocation - 1);
                }
                break;
            case DELETE:
                if (this.textCursorLocation < text.length()) {
                    final String preCursor = text.substring(0, this.textCursorLocation);
                    final String postCursor = text.substring(this.textCursorLocation + 1);
                    setText(preCursor + postCursor);
                    setCursorLocation(this.textCursorLocation - 1);
                }
                break;
        }
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);

        final String text = this.getText();
        if (this.textCursorLocation < 0 || this.textCursorLocation > text.length())
            return;

        final String textBeforeCursor = text.substring(0, this.textCursorLocation);
        final FontDefinition font = getFont();
        final int textCursorOffsetX = font.getTextSize(textBeforeCursor).width;
        final Point textOffset = getTextDrawLocation();
        final int cursorX = textCursorOffsetX + textOffset.x;

        graphics.fillQuadColor(cursorX, textOffset.y, 2, font.textHeight, getForeground());
    }
}
