package org.wmb.rendering;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public final class Camera {

    private float x, y, z, fov, near, far, pitch, yaw;

    public Camera(float x, float y, float z, float pitch, float yaw, float fov, float near, float far) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.fov = fov;
        this.near = near;
        this.far = far;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return this.x;
    }

    public void moveX(float delta) {
        this.x += delta;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return this.y;
    }

    public void moveY(float delta) {
        this.y += delta;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getZ() {
        return this.z;
    }

    public void moveZ(float delta) {
        this.z += delta;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void rotatePitch(float delta) {
        this.pitch += delta;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void rotateYaw(float delta) {
        this.yaw += delta;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public float getFov() {
        return this.fov;
    }

    public void setNear(float near) {
        this.near = near;
    }

    public float getNear() {
        return this.near;
    }

    public void setFar(float far) {
        this.far = far;
    }

    public float getFar() {
        return this.far;
    }

    // Vector4f to make calculations easier. w is ignored
    public void move(Vector4f direction) {
        moveX(direction.x);
        moveY(direction.y);
        moveZ(direction.z);
    }

    public Matrix4f getRotationMatrix() {
        return new Matrix4f().identity()
            .rotate((float) Math.toRadians(this.pitch), 1.0f, 0.0f, 0.0f)
            .rotate((float) Math.toRadians(this.yaw), 0.0f, 1.0f, 0.0f);
    }

    public Matrix4f getLookYawRotationMatrix() {
        return new Matrix4f().identity()
                .rotate((float) Math.toRadians(-yaw), 0.0f, 1.0f, 0.0f);
    }

    public Matrix4f getViewMatrix() {
        return getRotationMatrix().mul(new Matrix4f().identity().translate(-this.x, -this.y, -this.z));
    }

    public Matrix4f getProjectionMatrix(float aspect) {
        return new Matrix4f().identity().perspective(fov, aspect, near, far);
    }
}
