package org.wmb.gui;

import org.wmb.Log;
import org.wmb.editor.element.Element;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.menu.Menu;
import org.wmb.gui.component.scenetree.TreeViewComponent;
import org.wmb.gui.component.text.Label;
import org.wmb.gui.data.Bounds;
import org.wmb.gui.data.DynamicSize;
import org.wmb.gui.data.Size;
import org.wmb.gui.input.KeyClickEvent;
import org.wmb.gui.input.MouseClickEvent;
import org.wmb.gui.input.MouseMoveEvent;
import org.wmb.WmbContext;
import org.wmb.gui.component.elementinspector.ElementInspectorComponent;
import org.wmb.gui.component.sceneview3d.SceneView3dComponent;
import org.wmb.gui.component.container.CompassContainerComponent;
import org.wmb.gui.component.MenuBarComponent;
import org.wmb.gui.input.MouseScrollEvent;
import org.wmb.rendering.OpenGLStateException;

import java.io.IOException;
import java.util.Objects;

public final class MainGui {

    private static final String TAG = "MainGui";

    private final WmbContext context;
    private final GuiGraphics graphics;
    private final CompassContainerComponent container;
    private final TreeViewComponent treeViewComponent;
    private final SceneView3dComponent sceneViewComponent;
    private final ElementInspectorComponent elementInspector;
    private Menu openedMenu;

    public MainGui(WmbContext context) throws IOException {
        Objects.requireNonNull(context, "Context is null");
        this.context = context;

        try {
            this.graphics = new GuiGraphics(this.context, 2);
        } catch (IOException exception) {
            Log.error(TAG, "Graphics failed to load");
            throw exception;
        }

        try {
            this.sceneViewComponent = new SceneView3dComponent(this.context);
        } catch (IOException exception) {
            this.graphics.delete();
            Log.error(TAG, "SceneView3d failed to load");
            throw exception;
        }

        this.container = new CompassContainerComponent();
        this.container.setCenter(this.sceneViewComponent);

        final MenuBarComponent menuBar = new MenuBarComponent();
        this.container.setNorth(menuBar);

        // -----------------------------------------------------------------------------------------

        final CompassContainerComponent treeViewContainer = new CompassContainerComponent();
        treeViewContainer.setBorder(0, 0, 0, 3);

        final Label label = new Label("Scene Tree", Align.CENTER, Theme.FONT_BOLD);
        label.setBackground(Theme.BACKGROUND);
        treeViewContainer.setNorth(label);

        this.treeViewComponent = new TreeViewComponent(context);
        treeViewContainer.setCenter(this.treeViewComponent);

        this.container.setWest(treeViewContainer);

        // -----------------------------------------------------------------------------------------

        this.elementInspector = new ElementInspectorComponent(context);
        this.container.setEast(this.elementInspector);

        this.context.getWindow().setInputListener(new WindowListener() {

            @Override
            public void mouseClick(MouseClickEvent event) {
                if (openedMenu != null) {
                    final Bounds menuBounds = openedMenu.getOuterBounds();
                    if (menuBounds.contains(event.x, event.y)) {
                        openedMenu.onMouseClick(event);
                        return;
                    } else
                        openedMenu = null;
                }

                container.onMouseClick(event);
            }

            @Override
            public void mouseMove(MouseMoveEvent event) {
                container.onMouseMove(event);

                if (openedMenu != null && openedMenu.contains(event.xTo, event.yTo))
                    context.getWindow().setCursor(openedMenu.getCursor(event.xTo, event.yTo));
                else
                    context.getWindow().setCursor(container.getCursor(event.xTo, event.yTo));
            }

            @Override
            public void mouseScroll(MouseScrollEvent event) {
                container.onMouseScroll(event);
            }

            @Override
            public void textInput(char c) {
                container.onTextInput(c);
            }

            @Override
            public void keyClick(KeyClickEvent event) {
                container.onKeyClick(event);
            }
        });
    }

    public void resize(Size size) {
        this.container.setBounds(0, 0, size.getWidth(), size.getHeight());

        final DynamicSize minSize = this.container.getRequestedSize();
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

    public void notifyReadScene() {
        this.elementInspector.notifyReadScene();
    }

    public void notifyWriteScene() {
        this.elementInspector.notifyWriteScene();
    }

    public void onElementAdd() {
        this.treeViewComponent.onElementAdd();
    }

    public void onElementRemove(Element element) {
        Objects.requireNonNull(element, "Element is null");
        this.treeViewComponent.onElementRemove(element);
    }

    public void setOpenedMenu(Menu menu) {
        this.openedMenu = menu;
    }

    private long count = -1L;

    public void draw() throws IOException {
        // Some components compute parts of their bounds in their draw() method
        // This will ensure the bounds get recalculated from time to time
        if (count++ % 100 == 0)
            recalculateLayout();

        try {
            this.sceneViewComponent.renderScene(this.context.getScene());
        } catch (IOException exception) {
            Log.error(TAG, "Exception during scene view rendering");
            throw exception;
        }

        try {
            this.graphics.preparePipeline();
        } catch (OpenGLStateException exception) {
            Log.error(TAG, "Exception during GUI graphics pipeline prepare");
            throw exception;
        }

        this.graphics.clear();
        this.container.draw(this.graphics);

        if (this.openedMenu != null)
            this.openedMenu.draw(this.graphics);

        this.graphics.resetPipeline();
    }

    public void delete() {
        this.sceneViewComponent.delete();
        this.graphics.delete();
    }

    public boolean isListeningForKeyboard() {
        return this.container.isListeningForKeyboard();
    }
}
