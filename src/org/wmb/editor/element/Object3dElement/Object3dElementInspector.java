package org.wmb.editor.element.Object3dElement;

import org.wmb.gui.component.elementinspector.Inspector;
import org.wmb.gui.component.elementinspector.InspectorViewComponent;
import org.wmb.gui.component.elementinspector.controls.ControlXYZ;

import java.util.Objects;

public final class Object3dElementInspector implements Inspector {

    private final Object3dElement element;

    Object3dElementInspector(Object3dElement element) {
        Objects.requireNonNull(element, "Element is null");
        this.element = element;
    }

    @Override
    public void init(InspectorViewComponent inspectorView) {
        inspectorView.addControl(new ControlXYZ("Position"));
    }

    @Override
    public void read() {

    }

    @Override
    public void write() {

    }
}
