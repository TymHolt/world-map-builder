package org.wmb.gui.components;

import org.wmb.WmbGui;
import org.wmb.gui.input.MouseButton;
import org.wmb.gui.input.MouseButtonAction;
import org.wmb.rendering.Color;
import org.wmb.rendering.ITexture;
import org.wmb.rendering.gui.GuiRenderer;

import java.awt.*;

public class IconToggleComponent implements IGuiComponent, IInputComponent {

    private int x, y, width, height;
    private boolean onState = false;
    private ITexture iconOn, iconOff;
    private Color colorOn = WmbGui.FOREGROUND;
    private Color colorOff = WmbGui.FOREGROUND;
    private Color backColorOn = WmbGui.BACKGROUND;
    private Color backColorOff = WmbGui.BACKGROUND;

    public IconToggleComponent(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(GuiRenderer guiRenderer) {
        if (this.onState) {
            guiRenderer.fillQuad(this.x, this.y, this.width, this.height, this.backColorOn);

            if (this.iconOn != null)
                guiRenderer.fillQuad(this.x, this.y, this.width, this.height, this.iconOn, this.colorOn);

        } else {
            guiRenderer.fillQuad(this.x, this.y, this.width, this.height, this.backColorOff);

            if (this.iconOff != null)
                guiRenderer.fillQuad(this.x, this.y, this.width, this.height, this.iconOff, this.colorOff);
        }
    }

    @Override
    public void mouseButtonEvent(MouseButton button, MouseButtonAction action, Point position) {
        if (action != MouseButtonAction.PRESS || button != MouseButton.LEFT)
            return;

        if (x <= position.x && y <= position.y && x + width > position.x && y + height > position.y)
            this.onState = !this.onState;
    }

    public void setOn(boolean onState) {
        this.onState = onState;
    }

    public boolean isOn() {
        return this.onState;
    }

    public void setIconOn(ITexture iconOn) {
        this.iconOn = iconOn;
    }

    public void setIconOff(ITexture iconOff) {
        this.iconOff = iconOff;
    }

    public void setColorOn(Color colorOn) {
        this.colorOn = colorOn;
    }

    public void setColorOff(Color colorOff) {
        this.colorOff = colorOff;
    }

    public void setBackgroundColorOn(Color backColorOn) {
        this.backColorOn = backColorOn;
    }

    public void setBackgroundColorOff(Color backColorOff) {
        this.backColorOff = backColorOff;
    }
}
