package org.wmb.gui;

import org.wmb.gui.input.MouseClickEvent;
import org.wmb.gui.input.MouseMoveEvent;
import org.wmb.WmbContext;
import org.wmb.gui.component.elementinspector.ElementInspectorComponent;
import org.wmb.gui.component.scenetree.SceneTreeComponent;
import org.wmb.gui.component.SceneView3dComponent;
import org.wmb.gui.component.CompassContainerComponent;
import org.wmb.gui.component.MenuBarComponent;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class MainGui {

    private final WmbContext context;
    private final GuiGraphics graphics;
    private final CompassContainerComponent container;
    private final SceneView3dComponent sceneViewComponent;

    public MainGui(WmbContext context) throws IOException {
        Objects.requireNonNull(context, "Context is null");

        this.context = context;
        this.graphics = new GuiGraphics(this.context, 2);

        this.container = new CompassContainerComponent();

        final MenuBarComponent menuBar = new MenuBarComponent();
        menuBar.setBackground(Theme.BACKGROUND);
        menuBar.getBorder().setColor(Theme.BORDER);
        this.container.setNorth(menuBar);

        final SceneTreeComponent sceneTree = new SceneTreeComponent(this.context);
        this.container.setWest(sceneTree);

        final ElementInspectorComponent elementInspector = new ElementInspectorComponent();
        this.container.setEast(elementInspector);

        this.sceneViewComponent = new SceneView3dComponent();
        this.container.setCenter(this.sceneViewComponent);

        this.context.getWindow().setInputListener(new WindowListener() {

            @Override
            public void mouseClick(MouseClickEvent event) {
                container.onMouseClick(event);
            }

            @Override
            public void mouseMove(MouseMoveEvent event) {
                container.onMouseMove(event);

                final Window window = context.getWindow();
                final Point mouse = window.getMousePosition();
                window.setCursor(container.getCursor(mouse.x, mouse.y));
            }
        });
    }

    public void resize(Dimension dimension) {
        Objects.requireNonNull(dimension, "Dimension is null");
        this.container.setBounds(0, 0, dimension.width, dimension.height);

        final Dimension minSize = this.container.getRequestedSize();
        this.context.getWindow().setMinimumSize(minSize.width, minSize.height);
    }

    private long count = -1L;

    public void draw() {
        // Some components compute parts of their bounds in their draw() method
        // This will ensure the bounds get recalculated from time to time
        if (count++ % 100 == 0)
            this.container.setBounds(this.container.getBounds());

        this.sceneViewComponent.renderScene(this.context.getScene());

        this.graphics.preparePipeline();
        this.graphics.clear();
        this.container.draw(graphics);
        this.graphics.resetPipeline();
    }

    public void dispose() {
        this.sceneViewComponent.dispose();
        this.graphics.deleteResources();
    }
}
