package org.wmb.world;

import org.joml.Matrix4f;

public final class WorldPosition {

    private final float x, y, z;

    public WorldPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public Matrix4f getAsMatrix() {
        return new Matrix4f().identity().translate(this.x, this.y, this.z);
    }
}
