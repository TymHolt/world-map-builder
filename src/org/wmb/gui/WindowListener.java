package org.wmb.gui;

import org.wmb.gui.input.MouseClickEvent;
import org.wmb.gui.input.MouseMoveEvent;

public interface WindowListener {

    void mouseClick(MouseClickEvent event);
    void mouseMove(MouseMoveEvent event);
}
