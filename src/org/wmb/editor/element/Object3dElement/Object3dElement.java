package org.wmb.editor.element.Object3dElement;

import org.wmb.editor.element.Element;
import org.wmb.gui.component.elementinspector.Inspector;
import org.wmb.gui.icon.Icon;
import org.wmb.rendering.math.ObjectTransform;

public final class Object3dElement extends Element {

    private final ObjectTransform transform;

    public Object3dElement(Element parent) {
        super("Object3dElement", parent);
        this.transform = new ObjectTransform(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public ObjectTransform getTransform() {
        return this.transform;
    }

    @Override
    public Icon getIcon() {
        return Icon.CUBE;
    }

    @Override
    public Inspector getInspector() {
        return new Object3dElementInspector(this);
    }
}
