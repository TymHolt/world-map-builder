package org.wmb;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

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
    private Dimension windowSize;
    private boolean resizeHappened;

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

        WmbInstance.instances.add(this);
    }

    public void requestClose() {
        GLFW.glfwSetWindowShouldClose(this.windowId, true);
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(this.windowId, title);
    }

    private void update() {
        GLFW.glfwMakeContextCurrent(this.windowId);
        GL30.glClearColor(0.33f, 0.33f, 0.7f, 1.0f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if (resizeHappened) {
            // Handle resizing buffers etc.
            resizeHappened = false;
        }

        // Do logic...

        GLFW.glfwSwapBuffers(this.windowId);

        if (GLFW.glfwWindowShouldClose(this.windowId)) {
            Callbacks.glfwFreeCallbacks(windowId);
            GLFW.glfwDestroyWindow(this.windowId);
            WmbInstance.instances.remove(this);
        }
    }
}
