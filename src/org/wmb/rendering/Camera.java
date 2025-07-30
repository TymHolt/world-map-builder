package org.wmb.rendering;

import org.joml.Matrix4f;

public final class Camera {

    private float x, y, z, fov, near, far;

    public Camera(float x, float y, float z, float fov, float near, float far) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Matrix4f getViewMatrix() {
        return new Matrix4f().identity().translate(-this.x, -this.y, -this.z);
    }

    public Matrix4f getProjectionMatrix(float aspect) {
        return new Matrix4f().identity().perspective(fov, aspect, near, far);
    }
}
