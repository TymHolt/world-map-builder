package org.wmb.common.gui;

import org.wmb.common.gui.input.MouseClickEvent;
import org.wmb.common.gui.input.MouseMoveEvent;

public interface WindowListener {

    void mouseClick(MouseClickEvent event);
    void mouseMove(MouseMoveEvent event);
}
