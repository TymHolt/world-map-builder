package org.wmb.gui;

import org.wmb.rendering.gui.GuiRenderer;

public class TabComponent implements IGuiComponent {

    private int x, y, width, height;

    public TabComponent(int x, int y, int width, int height) {
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
        guiRenderer.fillQuad(this.x, this.y, this.width, this.height, 0.5f, 0.5f, 0.5f, 1.0f);
    }
}
