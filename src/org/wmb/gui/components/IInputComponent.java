package org.wmb.gui.components;

import org.wmb.gui.input.MouseButton;
import org.wmb.gui.input.MouseButtonAction;

import java.awt.*;

public interface IInputComponent {

    void mouseButtonEvent(MouseButton button, MouseButtonAction action, Point position);
}
