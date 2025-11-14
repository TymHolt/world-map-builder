package org.wmb.core.gui;

import org.wmb.core.WmbContext;
import org.wmb.core.gui.component.ElementInspectorComponent;
import org.wmb.core.gui.component.SceneTreeComponent;
import org.wmb.gui.component.CompassContainerComponent;
import org.wmb.gui.component.MenuBarComponent;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class MainGui {

    private final WmbContext context;
    private final GuiGraphics graphics;
    private final CompassContainerComponent container;

    public MainGui(WmbContext context) throws IOException {
        Objects.requireNonNull(context, "Context is null");

        this.context = context;
        this.graphics = new GuiGraphics(context);

        this.container = new CompassContainerComponent();

        final MenuBarComponent menuBar = new MenuBarComponent();
        menuBar.setBackground(Theme.BACKGROUND);
        menuBar.getBorder().setColor(Theme.BORDER);
        this.container.setNorth(menuBar);

        final SceneTreeComponent sceneTree = new SceneTreeComponent();
        this.container.setWest(sceneTree);

        final ElementInspectorComponent elementInspector = new ElementInspectorComponent();
        this.container.setEast(elementInspector);
    }

    public void resize(Dimension dimension) {
        Objects.requireNonNull(dimension, "Dimension is null");
        this.container.setBounds(0, 0, dimension.width, dimension.height);

        final Dimension minSize = this.container.getRequestedSize();
        this.context.getWindow().setMinimumSize(minSize.width, minSize.height);
    }

    public void draw() {
        this.graphics.preparePipeline();
        this.graphics.clear();
        this.container.draw(graphics);
        this.graphics.resetPipeline();
    }

    public void dispose() {
        this.graphics.deleteResources();
    }
}
