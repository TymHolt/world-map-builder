package org.wmb.gui;

import org.wmb.rendering.gui.GuiRenderer;

public interface IGuiComponent {

    void setBounds(int x, int y, int width, int height);
    void render(GuiRenderer guiRenderer);
}
