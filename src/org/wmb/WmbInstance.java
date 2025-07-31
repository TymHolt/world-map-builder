package org.wmb;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.wmb.rendering.*;
import org.wmb.world.ObjectTransform;
import org.wmb.world.WorldObject;
import org.wmb.world.ObjectPosition;

import java.awt.*;
import java.util.ArrayList;

public final class WmbInstance {

    private static final ArrayList<WmbInstance> instances = new ArrayList<>();

    public static boolean hasInstances() {
        return !instances.isEmpty();
    }

    public static void updateAllInstances() {
        // Copy list so it can be updated while iterating
        for (WmbInstance instance : new ArrayList<>(instances))
            instance.update();

        GLFW.glfwPollEvents();
    }

    private final long windowId;
    private final Dimension windowSize;
    private boolean resizeHappened;
    private long lastUpdateTime = System.currentTimeMillis();
    private final Renderer renderer;
    private final Camera camera;
    private final KeyboardCameraController cameraController;
    private final ArrayList<WorldObject> objectList = new ArrayList<>();
    private final AllocatedVertexData testData;
    private final AllocatedTexture texture;

    public WmbInstance() {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.windowSize = new Dimension(screenSize.width / 2, screenSize.height / 2);

        this.windowId = GLFW.glfwCreateWindow(this.windowSize.width, this.windowSize.height,
            "World Map Builder", 0, 0);
        if (this.windowId == 0)
            throw new IllegalStateException("GLFW window could not be created");

        GLFW.glfwSetFramebufferSizeCallback(this.windowId, (window, width, height) -> {
            this.windowSize.width = width;
            this.windowSize.height = height;
            this.resizeHappened = true;
        });

        // Center the window
        GLFW.glfwSetWindowPos(this.windowId,
            (screenSize.width - this.windowSize.width) / 2,
            (screenSize.height - this.windowSize.height) / 2);

        GLFW.glfwMakeContextCurrent(this.windowId);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(this.windowId);
        GL.createCapabilities();

        this.renderer = new Renderer();
        this.camera = new Camera(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 90.0f, 0.1f, 1024.0f);
        this.cameraController = new KeyboardCameraController(this);

        this.testData = new AllocatedVertexData(new float[] {
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f
            }, new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
            }, new short[] {
                0, 1, 2,
                2, 3, 0
            });
        this.texture = new AllocatedTexture(TextureUtil.getDebugBufferedImage());

        this.objectList.add(new WorldObject(testData, texture,
            new ObjectTransform(0.0f, 0.0f, 0.0f, 45.0f, 0.0f, 45.0f)));

        WmbInstance.instances.add(this);
    }

    public void requestClose() {
        GLFW.glfwSetWindowShouldClose(this.windowId, true);
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(this.windowId, title);
    }

    public boolean isKeyPressed(int keyCode) {
        return GLFW.glfwGetKey(this.windowId, keyCode) == GLFW.GLFW_PRESS;
    }

    public Camera getCamera() {
        return camera;
    }

    private void update() {
        GLFW.glfwMakeContextCurrent(this.windowId);
        GL30.glClearColor(0.33f, 0.33f, 0.7f, 1.0f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if (this.resizeHappened) {
            // Handle resizing buffers etc.
            this.resizeHappened = false;
        }

        long currentUpdateTime = System.currentTimeMillis();
        float deltaTime = (float) (currentUpdateTime - this.lastUpdateTime) / 1000.0f;
        this.lastUpdateTime = currentUpdateTime;

        // --- Begin Logic ---

        this.cameraController.update(deltaTime);

        this.renderer.begin();
        this.renderer.uniformCamera(this.camera, (float) this.windowSize.width / (float) this.windowSize.height);

        for (WorldObject object : this.objectList) {
            object.getTransform().getRotation().setYaw(object.getTransform().getRotation().getYaw() + 130.0f * deltaTime);
            this.renderer.render(object);
        }

        this.renderer.end();

        // --- End Logic ---

        GLFW.glfwSwapBuffers(this.windowId);

        if (GLFW.glfwWindowShouldClose(this.windowId)) {
            Callbacks.glfwFreeCallbacks(windowId);
            GLFW.glfwDestroyWindow(this.windowId);
            WmbInstance.instances.remove(this);
            this.renderer.delete();
            this.testData.delete();
            this.texture.delete();
        }
    }
}
