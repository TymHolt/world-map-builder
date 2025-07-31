package org.wmb.world;

import org.joml.Matrix4f;

public final class ObjectPosition {

    private float x, y, z;

    public ObjectPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return this.x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return this.y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getZ() {
        return this.z;
    }

    public Matrix4f getAsMatrix() {
        return new Matrix4f().identity().translate(this.x, this.y, this.z);
    }
}
