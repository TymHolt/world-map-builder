package org.wmb.gui.input;

import org.wmb.gui.component.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class InputHandleHelper {

    private List<Component> components;
    private Component focusedComponent;

    public InputHandleHelper() {
        setComponents(null);
    }

    public void setComponents(List<Component> components) {
        if (components == null)
            components = new ArrayList<>();

        this.components = components;

        if (!components.contains(this.focusedComponent)) {
            if (this.focusedComponent != null)
                this.focusedComponent.onLooseFocus();

            this.focusedComponent = null;
        }
    }

    public void handleMouseClick(MouseClickEvent event) {
        // Focused component clicked?
        if (this.focusedComponent != null) {
            if (this.focusedComponent.getBounds().contains(event.x, event.y)) {
                this.focusedComponent.onMouseClick(event);
                return;
            }
        }

        // Not the focused component clicked, test others
        for (Component component : this.components) {
            if (component == null)
                continue;

            if (component.getBounds().contains(event.x, event.y)) {
                if (this.focusedComponent != null)
                    this.focusedComponent.onLooseFocus();

                this.focusedComponent = component;
                component.onGainFocus();
                component.onMouseClick(event);
                return;
            }
        }

        // No contained component clicked
        if (this.focusedComponent != null) {
            this.focusedComponent.onLooseFocus();
            this.focusedComponent = null;
        }
    }

    public void handleMouseMove(MouseMoveEvent event) {
        if (this.focusedComponent == null)
            return;

        final Rectangle bounds = this.focusedComponent.getBounds();
        if (bounds.contains(event.xFrom, event.yFrom) || bounds.contains(event.xTo, event.yTo))
            this.focusedComponent.onMouseMove(event);
    }

    public void handleGainFocus() {

    }

    public void handleLooseFocus() {
        if (this.focusedComponent == null)
            return;

        this.focusedComponent.onLooseFocus();
        this.focusedComponent = null;
    }

    public void handleMouseScroll(MouseScrollEvent event) {
        for (Component component : this.components) {
            if (component == null)
                continue;

            if (component.getBounds().contains(event.x, event.y)) {
                component.onMouseScroll(event);
                return;
            }
        }
    }

    public Cursor handleGetCursor(int mouseX, int mouseY) {
        for (Component component : this.components) {
            if (component == null)
                continue;

            if (component.getBounds().contains(mouseX, mouseY))
                return component.getCursor(mouseX, mouseY);
        }

        return Cursor.DEFAULT;
    }
}
