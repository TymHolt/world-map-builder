package org.wmb.gui.component.container;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.component.Border;
import org.wmb.gui.component.Component;
import org.wmb.gui.input.Cursor;
import org.wmb.gui.input.MouseClickEvent;
import org.wmb.gui.input.MouseMoveEvent;
import org.wmb.gui.input.MouseScrollEvent;
import org.wmb.rendering.Color;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ContainerComponent extends Component {

    private final List<Component> componentList;
    private Component focusedComponent;

    public ContainerComponent() {
        super();
        setBorder(new Border());
        setBackground(Color.TRANSPARENT);
        this.componentList = new ArrayList<>();
    }

    protected void componentListUpdated() {
        if (this.focusedComponent == null)
            return;

        if (!this.componentList.contains(this.focusedComponent)) {
            this.focusedComponent.onLooseFocus();
            this.focusedComponent = null;
        }
    }

    protected List<Component> getComponentList() {
        return this.componentList;
    }

    public void addComponent(Component component) {
        Objects.requireNonNull(component);

        if (this.componentList.contains(component))
            return;

        this.componentList.add(component);
        componentListUpdated();
    }

    public void removeComponent(Component component) {
        this.componentList.remove(component);
        componentListUpdated();
    }

    public void clearComponents() {
        this.componentList.clear();
        componentListUpdated();
    }

    @Override
    public void onMouseClick(MouseClickEvent event) {
        // Focused component clicked?
        if (this.focusedComponent != null) {
            if (this.focusedComponent.getBounds().contains(event.x, event.y)) {
                this.focusedComponent.onMouseClick(event);
                return;
            }
        }

        // Not the focused component clicked, test others
        for (Component component : this.componentList) {
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

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        if (this.focusedComponent == null)
            return;

        final Rectangle bounds = this.focusedComponent.getBounds();
        if (bounds.contains(event.xFrom, event.yFrom) || bounds.contains(event.xTo, event.yTo))
            this.focusedComponent.onMouseMove(event);
    }

    @Override
    public void onMouseScroll(MouseScrollEvent event) {
        for (Component component : this.componentList) {
            if (component == null)
                continue;

            if (component.getBounds().contains(event.x, event.y)) {
                component.onMouseScroll(event);
                return;
            }
        }
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        for (Component component : this.componentList) {
            if (component == null)
                continue;

            if (component.getBounds().contains(mouseX, mouseY))
                return component.getCursor(mouseX, mouseY);
        }

        return Cursor.DEFAULT;
    }

    @Override
    public void onLooseFocus() {
        if (this.focusedComponent == null)
            return;

        this.focusedComponent.onLooseFocus();
        this.focusedComponent = null;
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);
        for (Component component : getComponentList())
            component.draw(graphics);
    }
}
