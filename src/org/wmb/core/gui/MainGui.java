package org.wmb.core.gui;

import org.wmb.core.WmbContext;
import org.wmb.core.gui.component.ElementInspectorComponent;
import org.wmb.core.gui.component.SceneTreeComponent;
import org.wmb.core.gui.component.SceneView3dComponent;
import org.wmb.editor.Scene3d;
import org.wmb.editor.element.Object3dElement.Object3dElement;
import org.wmb.common.gui.component.CompassContainerComponent;
import org.wmb.common.gui.component.MenuBarComponent;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class MainGui {

    private final WmbContext context;
    private final GuiGraphics graphics;
    private final CompassContainerComponent container;
    private final SceneView3dComponent sceneViewComponent;
    private Scene3d scene;

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

        this.sceneViewComponent = new SceneView3dComponent();
        this.container.setCenter(this.sceneViewComponent);

        this.scene = new Scene3d();
        this.scene.getChildren().add(new Object3dElement(this.scene));
    }

    public void resize(Dimension dimension) {
        Objects.requireNonNull(dimension, "Dimension is null");
        this.container.setBounds(0, 0, dimension.width, dimension.height);

        final Dimension minSize = this.container.getRequestedSize();
        this.context.getWindow().setMinimumSize(minSize.width, minSize.height);
    }

    public void draw() {
        this.sceneViewComponent.renderScene(this.scene);

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
