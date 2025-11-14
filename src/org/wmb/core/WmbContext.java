package org.wmb.core;

import org.wmb.core.gui.MainGui;
import org.wmb.common.gui.Window;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class WmbContext {

    private final org.wmb.common.gui.Window window;
    private final MainGui gui;
    private boolean active;

    WmbContext() throws IOException {
        this.active = true;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.window = new org.wmb.common.gui.Window(screenSize.width * 2 / 3, screenSize.height * 2 / 3,
            "World Map Builder");

        this.window.makeContextCurrent();
        this.gui = new MainGui(this);

        addContext(this);
    }

    public Window getWindow() {
        return this.window;
    }

    private int lastWidth = -1;
    private int lastHeight = -1;

    private void update() {
        if (!this.active)
            return;

        if (this.window.wasCloseRequested()) {
            this.active = false;
            this.window.makeContextCurrent();
            this.gui.dispose();
            this.window.close();
            removeContext(this);
            return;
        }

        this.window.makeContextCurrent();
        final Dimension windowSize = this.window.getSize();;
        if (this.lastWidth != windowSize.width || this.lastHeight != windowSize.height) {
            this.lastWidth = windowSize.width;
            this.lastHeight = windowSize.height;
            this.gui.resize(windowSize);
        }

        this.gui.draw();
        this.window.update();
    }

    private static final List<WmbContext> contextList = new ArrayList<>();

    static void updateAll() {
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
