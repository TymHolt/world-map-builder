package org.wmb.gui.input;

import java.util.Objects;

public final class MouseScrollEvent {

    public final ScrollDirection direction;
    public final int x;
    public final int y;

    public MouseScrollEvent(ScrollDirection direction, int x, int y) {
        Objects.requireNonNull(direction, "Direction is null");

        this.direction = direction;
        this.x = x;
        this.y = y;
    }
}
