package org.wmb;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.wmb.gui.input.MouseButton;
import org.wmb.gui.input.MouseButtonAction;

import java.awt.*;

public final class Window {

    static {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit())
            throw new IllegalStateException("GLFW could not be initialized");

        Main.executeOnExit(() -> {
            GLFW.glfwTerminate();
            GLFW.glfwSetErrorCallback(null).free();
        });
    }

    private final long windowId;
    private final Dimension windowSize;

    public Window(int width, int height, String title) {
        if (width < 1 || height < 1)
            throw new IllegalArgumentException("Illegal dimension " + width + "x" + height);

        if (title == null)
            title = "";

        setGlfwFlags();

        this.windowSize = new Dimension(width, height);
        this.windowId = GLFW.glfwCreateWindow(width, height, title, 0, 0);

        centerWindow();

        GLFW.glfwMakeContextCurrent(this.windowId);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(this.windowId);
        GL.createCapabilities();

        GLFW.glfwSetFramebufferSizeCallback(this.windowId, (window, newWidth, newHeight) -> {
            this.windowSize.width = width;
            this.windowSize.height = height;

            // TODO Handle event, WindowListener?
        });

        GLFW.glfwSetMouseButtonCallback(this.windowId, (window, button, action, mods) -> {
            final MouseButton mouseButton = MouseButton.getByGlfwId(button);
            final MouseButtonAction mouseButtonAction = MouseButtonAction.getByGlfwId(action);
            final Point mousePosition = this.getMousePosition();

            if (mouseButton == null || mouseButtonAction == null)
                return;

            // TODO Handle event, WindowListener?
        });
    }

    public void makeContextCurrent() {
        GLFW.glfwMakeContextCurrent(this.windowId);
    }

    public void update() {
        GLFW.glfwSwapBuffers(this.windowId);
        GLFW.glfwPollEvents();
    }

    public boolean wasCloseRequested() {
        return GLFW.glfwWindowShouldClose(this.windowId);
    }

    public void close() {
        Callbacks.glfwFreeCallbacks(windowId);
        GLFW.glfwDestroyWindow(this.windowId);
    }

    public Point getMousePosition() {
        final double[] xPositionPointer = new double[1];
        final double[] yPositionPointer = new double[1];
        GLFW.glfwGetCursorPos(windowId, xPositionPointer, yPositionPointer);
        return new Point((int) xPositionPointer[0], (int) yPositionPointer[0]);
    }

    // TODO Add custom Key class for QWERTY layout
    public boolean isKeyPressed(int keyCode) {
        return GLFW.glfwGetKey(this.windowId, keyCode) == GLFW.GLFW_PRESS;
    }

    public Dimension getSize() {
        return new Dimension(this.windowSize);
    }

    private void centerWindow() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        GLFW.glfwSetWindowPos(this.windowId,
            (screenSize.width - this.windowSize.width) / 2,
            (screenSize.height - this.windowSize.height) / 2);
    }

    private static void setGlfwFlags() {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
    }
}
