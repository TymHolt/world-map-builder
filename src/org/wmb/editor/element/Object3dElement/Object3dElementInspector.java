package org.wmb.editor.element.Object3dElement;

import org.wmb.ResourceManager;
import org.wmb.WmbContext;
import org.wmb.gui.component.VerticalPadding;
import org.wmb.gui.component.elementinspector.BasicInspector;
import org.wmb.gui.component.elementinspector.InspectorViewComponent;
import org.wmb.gui.component.elementinspector.controls.ControlXYZ;
import org.wmb.gui.component.elementinspector.controls.FileResourceControl;
import org.wmb.rendering.math.ObjectPosition;
import org.wmb.rendering.math.ObjectRotation;
import org.wmb.rendering.math.ObjectTransform;

import javax.swing.JOptionPane;
import java.io.IOException;

public final class Object3dElementInspector extends BasicInspector {

    private final Object3dElement element;
    private final ControlXYZ positionControl;
    private final ControlXYZ rotationControl;
    private final FileResourceControl modelControl;
    private final FileResourceControl textureControl;
    private WmbContext context;

    Object3dElementInspector(Object3dElement element, WmbContext context) {
        super(element);
        this.element = element;
        this.positionControl = new ControlXYZ("Position");
        this.rotationControl = new ControlXYZ("Rotation");
        this.modelControl = new FileResourceControl("Model", context);
        this.textureControl = new FileResourceControl("Texture", context);
    }

    @Override
    public void init(InspectorViewComponent inspectorView) {
        super.init(inspectorView);
        this.context = inspectorView.getContext();

        inspectorView.addComponent(new VerticalPadding(5));
        inspectorView.addComponent(this.positionControl);
        inspectorView.addComponent(new VerticalPadding(5));
        inspectorView.addComponent(this.rotationControl);
        inspectorView.addComponent(new VerticalPadding(5));
        inspectorView.addComponent(this.modelControl);
        inspectorView.addComponent(new VerticalPadding(5));
        inspectorView.addComponent(this.textureControl);
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

        this.modelControl.setSelectedPath(this.element.modelPath);
        this.textureControl.setSelectedPath(this.element.texturePath);
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

        if (this.modelControl.hasSelectedPath()) {
            final String path = this.modelControl.getSelectedPath();
            final ResourceManager resourceManager = this.context.getResourceManager();

            if (!resourceManager.hasModelLoaded(path)) {
                try {
                    resourceManager.loadModel(path);
                    this.element.modelPath = path;
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(),
                        "Failed to load model", JOptionPane.ERROR_MESSAGE);
                }
            } else
                this.element.modelPath = path;
        } else
            this.element.modelPath = null;

        if (this.textureControl.hasSelectedPath()) {
            final String path = this.textureControl.getSelectedPath();
            final ResourceManager resourceManager = this.context.getResourceManager();

            if (!resourceManager.hasTextureLoaded(path)) {
                try {
                    resourceManager.loadTexture(path);
                    this.element.texturePath = path;
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(),
                        "Failed to load texture", JOptionPane.ERROR_MESSAGE);
                }
            } else
                this.element.texturePath = path;
        } else
            this.element.texturePath = null;
    }
}
