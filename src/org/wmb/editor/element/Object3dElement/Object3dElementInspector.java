package org.wmb.editor.element.Object3dElement;

import org.wmb.gui.component.elementinspector.Inspector;
import org.wmb.gui.component.elementinspector.InspectorViewComponent;
import org.wmb.gui.component.elementinspector.controls.ControlXYZ;
import org.wmb.gui.component.elementinspector.controls.TextControl;

import java.util.Objects;

public final class Object3dElementInspector implements Inspector {

    private final Object3dElement element;
    private final TextControl nameControl;
    private final ControlXYZ positionControl;

    Object3dElementInspector(Object3dElement element) {
        Objects.requireNonNull(element, "Element is null");
        this.element = element;
        this.nameControl = new TextControl("Name");
        this.positionControl = new ControlXYZ("Position");
    }

    @Override
    public void init(InspectorViewComponent inspectorView) {
        inspectorView.addPadding();
        inspectorView.addControl(this.nameControl);
        inspectorView.addPadding();
        inspectorView.addControl(this.positionControl);
        inspectorView.addPadding();
        read();
    }

    @Override
    public void read() {
        this.nameControl.setText(this.element.getName());
        this.positionControl.setX(this.element.getTransform().getPosition().getX());
        this.positionControl.setY(this.element.getTransform().getPosition().getY());
        this.positionControl.setZ(this.element.getTransform().getPosition().getZ());
    }

    @Override
    public void write() {
        this.element.setName(this.nameControl.getText());
        this.element.getTransform().getPosition().setX(this.positionControl.getX());
        this.element.getTransform().getPosition().setY(this.positionControl.getY());
        this.element.getTransform().getPosition().setZ(this.positionControl.getZ());
    }
}
