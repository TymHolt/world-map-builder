package org.wmb;

import java.awt.*;

public final class WmbContext {

    private final Window window;
    private boolean active;

    WmbContext() {
        this.active = true;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.window = new Window(screenSize.width / 2, screenSize.height / 2, "World Map Builder");
    }

    public void update() {
        if (!this.active)
            return;

        if (this.window.wasCloseRequested()) {
            this.active = false;
            this.window.close();
            return;
        }

        this.window.update();
    }

    public boolean isActive() {
        return this.active;
    }
}
