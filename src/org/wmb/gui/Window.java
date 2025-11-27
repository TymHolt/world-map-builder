package org.wmb.gui;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.wmb.gui.input.*;
import org.wmb.Main;
import org.wmb.gui.input.Cursor;

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
    private WindowListener inputListener;
    private int xFrom;
    private int yFrom;

    public Window(int width, int height, String title) {
        if (width < 1 || height < 1)
            throw new IllegalArgumentException("Illegal dimension " + width + "x" + height);

        if (title == null)
            title = "";

        setGlfwFlags();
        this.windowId = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        centerWindow();

        GLFW.glfwMakeContextCurrent(this.windowId);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(this.windowId);
        GL.createCapabilities();

        GLFW.glfwSetMouseButtonCallback(this.windowId, (window, button, action, mods) -> {
            final MouseButton mouseButton = MouseButton.getByGlfwId(button);
            final ClickAction clickAction = ClickAction.getByGlfwId(action);
            final Point mouse = this.getMousePosition();

            if (mouseButton == null || clickAction == null || inputListener == null)
                return;

            final MouseClickEvent event = new MouseClickEvent(mouseButton, clickAction,
                mouse.x, mouse.y);
            this.inputListener.mouseClick(event);
        });

        final Point mousePosition = getMousePosition();
        this.xFrom = mousePosition.x;
        this.yFrom = mousePosition.y;

        GLFW.glfwSetCursorPosCallback(this.windowId, (window, xToDouble, yToDouble) -> {
            if (this.inputListener == null)
                return;

            final int xTo = (int) xToDouble;
            final int yTo = (int) yToDouble;
            final MouseMoveEvent event = new MouseMoveEvent(this.xFrom, this.yFrom, xTo, yTo);
            this.xFrom = xTo;
            this.yFrom = yTo;
            this.inputListener.mouseMove(event);
        });

        GLFW.glfwSetScrollCallback(this.windowId, (window, xoffset, yoffset) -> {
            if (this.inputListener == null)
                return;

            final Point mouse = this.getMousePosition();
            final MouseScrollEvent event = new MouseScrollEvent(ScrollDirection.fromValue(yoffset),
                mouse.x, mouse.y);
            this.inputListener.mouseScroll(event);
        });

        GLFW.glfwSetCharCallback(this.windowId, (window, codepoint) -> {
            if (this.inputListener == null)
                return;

            this.inputListener.textInput((char) codepoint);
        });

        GLFW.glfwSetKeyCallback(this.windowId, (window, key, scancode, action, mods) -> {
            if (this.inputListener == null)
                return;

            final KeyButton button = KeyButton.getByGlfwId(key);
            final ClickAction clickAction = ClickAction.getByGlfwId(action);

            if (button == null || clickAction == null)
                return;

            this.inputListener.keyClick(new KeyClickEvent(button, clickAction));
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
        if (this.currentCursorId != 0L)
            GLFW.glfwDestroyCursor(this.currentCursorId);

        Callbacks.glfwFreeCallbacks(windowId);
        GLFW.glfwDestroyWindow(this.windowId);
    }

    public Point getMousePosition() {
        final double[] xPositionPointer = new double[1];
        final double[] yPositionPointer = new double[1];
        GLFW.glfwGetCursorPos(windowId, xPositionPointer, yPositionPointer);
        return new Point((int) xPositionPointer[0], (int) yPositionPointer[0]);
    }

    public boolean isKeyPressed(KeyButton button) {
        return GLFW.glfwGetKey(this.windowId, button.getGlfwId()) == GLFW.GLFW_PRESS;
    }

    public Dimension getSize() {
        final int[] widthPointer = new int[1];
        final int[] heightPointer = new int[1];
        GLFW.glfwGetFramebufferSize(windowId, widthPointer, heightPointer);
        return new Dimension(widthPointer[0], heightPointer[0]);
    }

    public void setMinimumSize(int width, int height) {
        GLFW.glfwSetWindowSizeLimits(this.windowId, width, height, GLFW.GLFW_DONT_CARE,
            GLFW.GLFW_DONT_CARE);
    }

    public void removeMinimumSize() {
        GLFW.glfwSetWindowSizeLimits(this.windowId, GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE,
            GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE);
    }

    public void setInputListener(WindowListener inputListener) {
        this.inputListener = inputListener;
    }

    private Cursor currentCursor = Cursor.DEFAULT;
    private long currentCursorId = 0L;

    public void setCursor(Cursor cursor) {
        if (cursor == null)
            cursor = Cursor.DEFAULT;

        if (this.currentCursor == cursor)
            return;

        this.currentCursor = cursor;

        if (cursor != Cursor.DEFAULT) {
            if (this.currentCursorId != 0L)
                GLFW.glfwDestroyCursor(this.currentCursorId);

            this.currentCursorId = GLFW.glfwCreateStandardCursor(cursor.getGlfwId());
        } else
            this.currentCursorId = 0L;

        GLFW.glfwSetCursor(this.windowId, this.currentCursorId);
    }

    private void centerWindow() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension windowSize = getSize();
        GLFW.glfwSetWindowPos(this.windowId,
            (screenSize.width - windowSize.width) / 2,
            (screenSize.height - windowSize.height) / 2);
    }

    private static void setGlfwFlags() {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 8);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
    }
}
