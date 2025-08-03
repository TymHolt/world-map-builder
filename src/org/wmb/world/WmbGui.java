package org.wmb.world;

import org.wmb.WmbInstance;
import org.wmb.gui.TabComponent;
import org.wmb.gui.WorldViewComponent;
import org.wmb.rendering.gui.GuiRenderer;

public final class WmbGui {

    private final GuiRenderer guiRenderer = new GuiRenderer();
    private final WorldViewComponent mainView;
    private final TabComponent toolPanel;

    public WmbGui(int width, int height, WmbInstance instance) {
        this.mainView = new WorldViewComponent(0, 0, 1, 1, instance.getObjectList());
        this.toolPanel = new TabComponent(0, 0, 1, 1);

        resize(width, height);
    }

    public void resize(int width, int height) {
        int viewBorder = (width * 4) / 5;
        this.mainView.setBounds(0, 0, viewBorder, height);
        this.toolPanel.setBounds(viewBorder, 0, width - viewBorder, height);
    }

    public void render() {
        this.mainView.render();

        this.guiRenderer.begin();
        this.toolPanel.render(this.guiRenderer);
        this.guiRenderer.end();
    }

    public void delete() {
        this.mainView.delete();
        this.guiRenderer.delete();
    }

    public WorldViewComponent getMainView() {
        return this.mainView;
    }

    public TabComponent getToolPanel() {
        return this.toolPanel;
    }
}
