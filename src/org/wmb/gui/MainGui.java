package org.wmb.gui;

import org.wmb.editor.element.Element;
import org.wmb.gui.input.MouseClickEvent;
import org.wmb.gui.input.MouseMoveEvent;
import org.wmb.WmbContext;
import org.wmb.gui.component.elementinspector.ElementInspectorComponent;
import org.wmb.gui.component.scenetree.SceneTreeComponent;
import org.wmb.gui.component.sceneview3d.SceneView3dComponent;
import org.wmb.gui.component.CompassContainerComponent;
import org.wmb.gui.component.MenuBarComponent;
import org.wmb.gui.input.MouseScrollEvent;
import org.wmb.rendering.OpenGLStateException;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class MainGui {

    private final WmbContext context;
    private final GuiGraphics graphics;
    private final CompassContainerComponent container;
    private final SceneView3dComponent sceneViewComponent;
    private final ElementInspectorComponent elementInspector;

    public MainGui(WmbContext context) throws IOException {
        Objects.requireNonNull(context, "Context is null");
        this.context = context;

        try {
            this.graphics = new GuiGraphics(this.context, 2);
        } catch (IOException exception) {
            throw new IOException("(GuiGraphics) " + exception.getMessage());
        }

        try {
            this.sceneViewComponent = new SceneView3dComponent(this.context);
        } catch (IOException exception) {
            this.graphics.delete();
            throw new IOException("(SceneViewComponent) " + exception.getMessage());
        }

        this.container = new CompassContainerComponent();
        this.container.setCenter(this.sceneViewComponent);

        final MenuBarComponent menuBar = new MenuBarComponent();
        menuBar.setBackground(Theme.BACKGROUND);
        menuBar.getBorder().setColor(Theme.BORDER);
        this.container.setNorth(menuBar);

        final SceneTreeComponent sceneTree = new SceneTreeComponent(this.context);
        this.container.setWest(sceneTree);

        this.elementInspector = new ElementInspectorComponent();
        this.container.setEast(this.elementInspector);

        this.context.getWindow().setInputListener(new WindowListener() {

            @Override
            public void mouseClick(MouseClickEvent event) {
                container.onMouseClick(event);
            }

            @Override
            public void mouseMove(MouseMoveEvent event) {
                container.onMouseMove(event);

                final Window window = context.getWindow();
                window.setCursor(container.getCursor(event.xTo, event.yTo));
            }

            @Override
            public void mouseScroll(MouseScrollEvent event) {
                container.onMouseScroll(event);
            }
        });
    }

    public void resize(Dimension dimension) {
        Objects.requireNonNull(dimension, "Dimension is null");
        this.container.setBounds(0, 0, dimension.width, dimension.height);

        final Dimension minSize = this.container.getRequestedSize();
        this.context.getWindow().setMinimumSize(minSize.width, minSize.height);
    }

    public void notifyElementSelected(Element element) {
        Objects.requireNonNull(element, "Element is null");
        this.elementInspector.setInspector(element.getInspector());
        recalculateLayout();
    }

    public void recalculateLayout() {
        this.container.setBounds(this.container.getBounds());
    }

    private long count = -1L;

    public void draw() throws OpenGLStateException {
        // Some components compute parts of their bounds in their draw() method
        // This will ensure the bounds get recalculated from time to time
        if (count++ % 100 == 0)
            recalculateLayout();

        this.sceneViewComponent.renderScene(this.context.getScene());

        try {
            this.graphics.preparePipeline();
        } catch (OpenGLStateException exception) {
            throw new OpenGLStateException("(Prepare GuiGraphics)" + exception.getMessage());
        }

        this.graphics.clear();
        this.container.draw(graphics);
        this.graphics.resetPipeline();
    }

    public void delete() {
        this.sceneViewComponent.delete();
        this.graphics.delete();
    }
}
