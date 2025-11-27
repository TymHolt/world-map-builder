package org.wmb.editor.element.Object3dElement;

import org.wmb.gui.component.VerticalPadding;
import org.wmb.gui.component.elementinspector.BasicInspector;
import org.wmb.gui.component.elementinspector.InspectorViewComponent;
import org.wmb.gui.component.elementinspector.controls.ControlXYZ;
import org.wmb.rendering.math.ObjectPosition;
import org.wmb.rendering.math.ObjectRotation;
import org.wmb.rendering.math.ObjectTransform;

public final class Object3dElementInspector extends BasicInspector {

    private final Object3dElement element;
    private final ControlXYZ positionControl;
    private final ControlXYZ rotationControl;

    Object3dElementInspector(Object3dElement element) {
        super(element);
        this.element = element;
        this.positionControl = new ControlXYZ("Position");
        this.rotationControl = new ControlXYZ("Rotation");
    }

    @Override
    public void init(InspectorViewComponent inspectorView) {
        super.init(inspectorView);
        inspectorView.addComponent(new VerticalPadding(5));
        inspectorView.addComponent(this.positionControl);
        inspectorView.addComponent(new VerticalPadding(5));
        inspectorView.addComponent(this.rotationControl);
    }

    @Override
    public void read() {
        super.read();

        final ObjectTransform transform = this.element.getTransform();
        final ObjectPosition position = transform.getPosition();
        this.positionControl.setX(position.getX());
        this.positionControl.setY(position.getY());
        this.positionControl.setZ(position.getZ());

        final ObjectRotation rotation = transform.getRotation();
        this.rotationControl.setX(rotation.getPitch());
        this.rotationControl.setY(rotation.getYaw());
        this.rotationControl.setZ(rotation.getRoll());
    }

    @Override
    public void write() {
        super.write();

        final ObjectTransform transform = this.element.getTransform();
        final ObjectPosition position = transform.getPosition();
        position.setX(this.positionControl.getX());
        position.setY(this.positionControl.getY());
        position.setZ(this.positionControl.getZ());

        final ObjectRotation rotation = transform.getRotation();
        rotation.setPitch(this.rotationControl.getX());
        rotation.setYaw(this.rotationControl.getY());
        rotation.setRoll(this.rotationControl.getZ());
    }
}
