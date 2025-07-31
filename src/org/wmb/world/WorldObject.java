package org.wmb.world;

import org.joml.Matrix4f;
import org.wmb.debug.Assert;
import org.wmb.rendering.AllocatedTexture;
import org.wmb.rendering.AllocatedVertexData;
import org.wmb.rendering.IRenderObject;

public final class WorldObject implements IRenderObject {

    private final AllocatedVertexData model;
    private final AllocatedTexture material;
    private final ObjectTransform transform;

    public WorldObject(AllocatedVertexData model, AllocatedTexture material,
        ObjectTransform transform) {

        Assert.argNotNull(model, "model");
        Assert.argNotNull(material, "material");
        Assert.argNotNull(transform, "transform");

        this.model = model;
        this.material = material;
        this.transform = transform;
    }

    public ObjectTransform getTransform() {
        return this.transform;
    }

    @Override
    public AllocatedVertexData getVertexData() {
        return this.model;
    }

    @Override
    public AllocatedTexture getTexture() {
        return this.material;
    }

    @Override
    public Matrix4f getRotationMatrix() {
        return this.transform.getRotation().getAsMatrix();
    }

    @Override
    public Matrix4f getTransformMatrix() {
        return this.transform.getAsMatrix();
    }
}
