package org.wmb.gui.input;

import java.util.Objects;

public final class MouseClickEvent {

    public final MouseButton button;
    public final MouseButtonAction action;
    public final int x;
    public final int y;

    public MouseClickEvent(MouseButton button, MouseButtonAction action, int x, int y) {
        Objects.requireNonNull(button, "Button is null");
        Objects.requireNonNull(action, "Action is null");

        this.button = button;
        this.action = action;
        this.x = x;
        this.y = y;
    }
}
