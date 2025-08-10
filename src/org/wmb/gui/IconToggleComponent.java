package org.wmb.gui;

import org.wmb.rendering.AllocatedTexture;
import org.wmb.rendering.gui.GuiRenderer;

public class IconToggleComponent implements IGuiComponent {

    private int x, y, width, height;
    private boolean onState = false;
    private final AllocatedTexture iconOn, iconOff;

    public IconToggleComponent(int x, int y, int width, int height,
        AllocatedTexture iconOn, AllocatedTexture iconOff) {
        setBounds(x, y, width, height);
        this.iconOn = iconOn;
        this.iconOff = iconOff;
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
        guiRenderer.fillQuad(this.x, this.y, this.width, this.height, 0.5f, 0.5f, 0.5f, 1.0f);
        guiRenderer.fillQuad(this.x, this.y, this.width, this.height,
            this.onState ? this.iconOn : this.iconOff);
    }

    public void setOn(boolean onState) {
        this.onState = onState;
    }

    public boolean isOn() {
        return this.onState;
    }
}
