package org.wmb.rendering.math;

import org.joml.Matrix4f;

public final class ObjectTransform {

    private final ObjectPosition position;
    private final ObjectRotation rotation;

    public ObjectTransform(float x, float y, float z, float pitch, float yaw, float roll) {
        this.position = new ObjectPosition(x, y, z);
        this.rotation = new ObjectRotation(pitch, yaw, roll);
    }

    public ObjectPosition getPosition() {
        return this.position;
    }

    public ObjectRotation getRotation() {
        return this.rotation;
    }

    public Matrix4f getAsMatrix() {
        return this.position.getAsMatrix().mul(this.rotation.getAsMatrix());
    }
}
