package org.wmb;

import org.wmb.gui.Icon;
import org.wmb.gui.components.IconToggleComponent;
import org.wmb.gui.components.TabComponent;
import org.wmb.gui.components.WorldViewComponent;
import org.wmb.gui.input.MouseButton;
import org.wmb.gui.input.MouseButtonAction;
import org.wmb.rendering.Color;
import org.wmb.rendering.gui.GuiRenderer;

import java.awt.*;
import java.io.IOException;

public final class WmbGui {

    public static final Color BACKGROUND = Color.GREY_DARK;
    public static final Color FOREGROUND = Color.WHITE;

    private final GuiRenderer guiRenderer;
    private final WorldViewComponent mainView;
    private final TabComponent toolPanel;
    private final IconToggleComponent toggleComponent;

    public WmbGui(int width, int height, WmbInstance instance) throws IOException {
        this.guiRenderer = new GuiRenderer(instance);

        this.mainView = new WorldViewComponent(0, 0, 1, 1, instance.getObjectList());
        this.toolPanel = new TabComponent(0, 0, 1, 1);
        this.toggleComponent = new IconToggleComponent(0, 0, 1, 1);
        this.toggleComponent.setIconOn(instance.getIcons().getTexture(Icon.EYE_SOLID));
        this.toggleComponent.setIconOff(instance.getIcons().getTexture(Icon.EYE));

        resize(width, height);
    }

    public void resize(int width, int height) {
        int viewBorder = (width * 4) / 5;
        this.mainView.setBounds(0, 0, viewBorder, height);
        this.toolPanel.setBounds(viewBorder, 0, width - viewBorder, height);

        final int padding = 8;
        final int tcSize = 32;
        this.toggleComponent.setBounds(viewBorder - padding - tcSize, padding, tcSize, tcSize);
    }

    public void mouseButtonEvent(MouseButton button, MouseButtonAction action, Point position) {
        this.toggleComponent.mouseButtonEvent(button, action, position);
    }

    public void render() {
        this.mainView.render();

        this.guiRenderer.begin();
        this.toolPanel.render(this.guiRenderer);
        this.toggleComponent.render(this.guiRenderer);
        this.guiRenderer.end();
    }

    public WorldViewComponent getMainView() {
        return this.mainView;
    }

    public TabComponent getToolPanel() {
        return this.toolPanel;
    }

    public IconToggleComponent getToggleComponent() {
        return this.toggleComponent;
    }
}
