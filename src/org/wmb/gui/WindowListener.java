package org.wmb.gui;

import org.wmb.gui.input.KeyClickEvent;
import org.wmb.gui.input.MouseClickEvent;
import org.wmb.gui.input.MouseMoveEvent;
import org.wmb.gui.input.MouseScrollEvent;

public interface WindowListener {

    void mouseClick(MouseClickEvent event);
    void mouseMove(MouseMoveEvent event);
    void mouseScroll(MouseScrollEvent event);
    void textInput(char c);
    void keyClick(KeyClickEvent event);
}
