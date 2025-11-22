package org.wmb.gui.input;

import java.util.Objects;

public final class KeyClickEvent {

    public final KeyButton button;
    public final ClickAction action;

    public KeyClickEvent(KeyButton button, ClickAction action) {
        Objects.requireNonNull(button, "Button is null");
        Objects.requireNonNull(action, "Action is null");

        this.button = button;
        this.action = action;
    }
}
