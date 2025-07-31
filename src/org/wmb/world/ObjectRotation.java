package org.wmb.world;

import org.joml.Matrix4f;

public final class ObjectRotation {

    private float pitch, yaw, roll;

    public ObjectRotation(float pitch, float yaw, float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getRoll() {
        return this.roll;
    }

    public Matrix4f getAsMatrix() {
        return new Matrix4f().identity()
                .rotate((float) Math.toRadians(yaw), 0.0f, 1.0f, 0.0f)
                .rotate((float) Math.toRadians(pitch), 1.0f, 0.0f, 0.0f)
                .rotate((float) Math.toRadians(this.roll), 0.0f, 0.0f, 1.0f);
    }
}
