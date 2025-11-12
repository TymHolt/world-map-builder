package org.wmb.core;

import org.wmb.gui.component.Border;
import org.wmb.gui.component.Component;
import org.wmb.rendering.Color;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class WmbContext {

    private final Window window;
    private final WmbGraphics graphics;
    private boolean active;

    WmbContext() throws IOException {
        this.active = true;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.window = new Window(screenSize.width / 2, screenSize.height / 2, "World Map Builder");
        this.window.makeContextCurrent();
        this.graphics = new WmbGraphics(this);
        addContext(this);
    }

    public Window getWindow() {
        return this.window;
    }

    public WmbGraphics getGraphics() {
        return this.graphics;
    }

    private void update() {
        if (!this.active)
            return;

        if (this.window.wasCloseRequested()) {
            this.active = false;
            this.window.makeContextCurrent();
            this.graphics.deleteResources();
            this.window.close();
            removeContext(this);
            return;
        }

        this.window.makeContextCurrent();

        this.graphics.preparePipeline();

        final Component component = new Component();
        component.setBackground(Color.GREY);
        component.setBounds(10, 10, 100, 100);
        component.setBorder(new Border(3, Color.WHITE));

        component.draw(this.graphics);

        this.graphics.resetPipeline();

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
