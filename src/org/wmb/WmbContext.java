package org.wmb;

import org.wmb.editor.Scene3d;
import org.wmb.editor.element.Element;
import org.wmb.editor.element.Object3dElement.Object3dElement;
import org.wmb.gui.MainGui;
import org.wmb.gui.Window;
import org.wmb.gui.data.Size;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class WmbContext {

    private static final String TAG = "WmbContext";

    private Scene3d scene;
    private Element selectedElement;
    private final Window window;
    private final MainGui gui;
    private boolean active;

    WmbContext() throws IOException {
        this.active = true;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.window = new Window(screenSize.width * 2 / 3, screenSize.height * 2 / 3,
            "World Map Builder");
        this.window.makeContextCurrent();

        this.scene = new Scene3d();
        this.scene.getChildren().add(new Object3dElement(this.scene));

        try {
            this.gui = new MainGui(this);
        } catch (IOException exception) {
            this.window.close();
            Log.error(TAG, "Main GUI failed to load");
            throw exception;
        }

        addContext(this);
    }

    public Window getWindow() {
        return this.window;
    }

    public Scene3d getScene() {
        return this.scene;
    }

    public MainGui getGui() {
        return this.gui;
    }

    public void setSelectedElement(Element element) {
        this.selectedElement = element;
        this.gui.notifyElementSelected(element);
    }

    public Element getSelectedElement() {
        return this.selectedElement;
    }

    private int lastWidth = -1;
    private int lastHeight = -1;

    private void update() throws IOException {
        if (!this.active)
            return;

        if (this.window.wasCloseRequested()) {
            this.active = false;
            this.window.makeContextCurrent();
            this.gui.delete();
            this.window.close();
            removeContext(this);
            return;
        }

        this.window.makeContextCurrent();
        final Size windowSize = this.window.getSize();
        final int windowWidth = windowSize.getWidth();
        final int windowHeight = windowSize.getHeight();
        if (this.lastWidth != windowWidth || this.lastHeight != windowHeight) {
            this.lastWidth = windowWidth;
            this.lastHeight = windowHeight;
            this.gui.resize(windowSize);
        }

        try {
            this.gui.draw();
        } catch (IOException exception) {
            this.active = false;
            this.window.makeContextCurrent();
            this.gui.delete();
            this.window.close();
            removeContext(this);
            Log.error(TAG, "Exception during draw call");
            throw exception;
        }

        this.window.update();
    }

    private static final List<WmbContext> contextList = new ArrayList<>();

    static void updateAll() throws IOException {
        List<WmbContext> listCopy;
        synchronized (WmbContext.contextList) {
            listCopy = new ArrayList<>(contextList);
        }

        for (WmbContext context : listCopy)
            context.update();
    }

    static boolean activeContextExists() {
        synchronized (WmbContext.contextList) {
            return !WmbContext.contextList.isEmpty();
        }
    }

    private static void addContext(WmbContext context) {
        synchronized (WmbContext.contextList) {
            WmbContext.contextList.add(context);
        }
    }

    private static void removeContext(WmbContext context) {
        synchronized (WmbContext.contextList) {
            WmbContext.contextList.remove(context);
        }
    }
}
