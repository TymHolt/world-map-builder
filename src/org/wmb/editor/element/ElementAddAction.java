package org.wmb.editor.element;

import org.wmb.WmbContext;
import org.wmb.editor.element.Object3dElement.Object3dElement;
import org.wmb.gui.component.menu.MenuAction;

import java.util.Objects;

public final class ElementAddAction implements MenuAction {

    private final WmbContext context;
    private final Element parent;

    public ElementAddAction(Element parent, WmbContext context) {
        Objects.requireNonNull(context, "Context is null");
        Objects.requireNonNull(parent, "Parent is null");
        this.context = context;
        this.parent = parent;
    }

    @Override
    public void execute() {
        final Object3dElement object3dElement = new Object3dElement(context);
        parent.addChild(object3dElement);
    }
}
