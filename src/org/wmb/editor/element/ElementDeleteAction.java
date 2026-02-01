package org.wmb.editor.element;

import org.wmb.gui.component.menu.MenuAction;

import java.util.Objects;

public final class ElementDeleteAction implements MenuAction {

    private final Element element;

    public ElementDeleteAction(Element element) {
        Objects.requireNonNull(element, "Element is null");
        this.element = element;
    }

    @Override
    public void execute() {
        final Element parent = this.element.getParent();
        if (parent != null)
            parent.removeChild(this.element);
    }
}
