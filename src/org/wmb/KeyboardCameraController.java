package org.wmb;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.wmb.rendering.Camera;

public final class KeyboardCameraController {

    // Units per second
    private static final float CAMERA_MOVE_SPEED = 4.0f;
    private static final float CAMERA_ROTATE_SPEED = 135.0f;

    private final WmbInstance instance;

    public KeyboardCameraController(WmbInstance instance) {
        this.instance = instance;
    }

    public void update(float deltaTime) {
        float moveDelta = CAMERA_MOVE_SPEED * deltaTime;
        float rotateDelta = CAMERA_ROTATE_SPEED * deltaTime;
        Camera camera = instance.getCamera();

        Matrix4f rotationMatrix = camera.getLookYawRotationMatrix();
        Vector4f forward = rotationMatrix.transform(new Vector4f(0.0f, 0.0f, -moveDelta, 1.0f));
        Vector4f right = rotationMatrix.transform(new Vector4f(moveDelta, 0.0f, 0.0f, 1.0f));
        Vector4f up = rotationMatrix.transform(new Vector4f(0.0f, moveDelta, 0.0f, 1.0f));

        if (instance.isKeyPressed(GLFW.GLFW_KEY_W))     // Forwards
            camera.move(forward);
        if (instance.isKeyPressed(GLFW.GLFW_KEY_S))     // Backwards
            camera.move(forward.mul(-1.0f));
        if (instance.isKeyPressed(GLFW.GLFW_KEY_D))     // Right
            camera.move(right);
        if (instance.isKeyPressed(GLFW.GLFW_KEY_A))     // Left
            camera.move(right.mul(-1.0f));
        if (instance.isKeyPressed(GLFW.GLFW_KEY_Q))     // Up
            camera.move(up);
        if (instance.isKeyPressed(GLFW.GLFW_KEY_Z))     // Down (American layout Z!)
            camera.move(up.mul(-1.0f));
        if (instance.isKeyPressed(GLFW.GLFW_KEY_UP))    // Look up
            camera.rotatePitch(-rotateDelta);
        if (instance.isKeyPressed(GLFW.GLFW_KEY_DOWN))  // Look down
            camera.rotatePitch(rotateDelta);
        if (instance.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) // Look right
            camera.rotateYaw(rotateDelta);
        if (instance.isKeyPressed(GLFW.GLFW_KEY_LEFT))  // Look left
            camera.rotateYaw(-rotateDelta);

    }
}
