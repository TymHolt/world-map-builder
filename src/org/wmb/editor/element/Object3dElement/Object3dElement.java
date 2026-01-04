package org.wmb.editor.element.Object3dElement;

import org.wmb.Log;
import org.wmb.editor.element.Element;
import org.wmb.gui.component.elementinspector.Inspector;
import org.wmb.gui.icon.Icon;
import org.wmb.loading.ResourceLoader;
import org.wmb.rendering.AllocatedMeshData;
import org.wmb.rendering.AllocatedTexture;
import org.wmb.rendering.math.ObjectTransform;

import java.io.IOException;
import java.util.Objects;

public final class Object3dElement extends Element {

    private static final String TAG = "Object3dElement";

    private final ObjectTransform transform;
    private String requestedMeshResourcePath;
    private String currentMeshResourcePath;
    private AllocatedMeshData meshData;
    private AllocatedTexture texture;

    public Object3dElement(Element parent) throws IOException {
        super("Object3dElement", parent);
        this.transform = new ObjectTransform(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

        try {
            setMeshData(newTestMeshData(), null);
        } catch (IOException exception) {
            Log.error(TAG, "Test mesh data failed to load");
            throw exception;
        }

        try {
            setTexture(newTestTexture());
        } catch (IOException exception) {
            this.meshData.delete();
            Log.error(TAG, "Test texture failed to load");
            throw exception;
        }
    }

    public ObjectTransform getTransform() {
        return this.transform;
    }

    public AllocatedMeshData getMeshData() {
        return this.meshData;
    }

    public AllocatedTexture getTexture() {
        return this.texture;
    }

    public void setRequestedMeshResourcePath(String resourcePath) {
        this.requestedMeshResourcePath = resourcePath;
    }

    public String getRequestedMeshResourcePath() {
        return this.requestedMeshResourcePath;
    }

    public void setMeshData(AllocatedMeshData meshData) {
        setMeshData(meshData, null);
    }

    public void setMeshData(AllocatedMeshData meshData, String resourcePath) {
        Objects.requireNonNull(meshData);
        this.meshData = meshData;
        this.currentMeshResourcePath = resourcePath;
    }

    public void setTexture(AllocatedTexture texture) {
        Objects.requireNonNull(texture);
        this.texture = texture;
    }

    public void syncResources() throws IOException {
        if (this.requestedMeshResourcePath == null && this.currentMeshResourcePath != null) {
            this.meshData.delete();
            setMeshData(newTestMeshData(), null);
        } else if (this.requestedMeshResourcePath != null &&
            !this.requestedMeshResourcePath.equals(this.currentMeshResourcePath)) {
            this.meshData.delete();
            setMeshData(AllocatedMeshData.fromFile(requestedMeshResourcePath),
                requestedMeshResourcePath);
        }
    }

    @Override
    public Icon getIcon() {
        return Icon.CUBE;
    }

    @Override
    public Inspector getInspector() {
        return new Object3dElementInspector(this);
    }

    public void deleteResources() {
        this.meshData.delete();
        this.texture.delete();
    }

    public static AllocatedMeshData newTestMeshData() throws IOException {
        final String path = "/org/wmb/editor/element/Object3dElement/cube.obj";
        return AllocatedMeshData.fromResource(path);
    }

    public static AllocatedTexture newTestTexture() throws IOException {
        final String path = "/org/wmb/editor/element/Object3dElement/cube_texture.png";
        return new AllocatedTexture(ResourceLoader.loadResourceImage(path));
    }
}
