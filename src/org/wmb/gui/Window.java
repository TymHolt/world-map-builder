package org.wmb.gui;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.wmb.gui.data.DynamicPosition;
import org.wmb.gui.data.DynamicSize;
import org.wmb.gui.data.Position;
import org.wmb.gui.data.Size;
import org.wmb.gui.input.*;
import org.wmb.Main;
import org.wmb.gui.input.Cursor;

import java.awt.Dimension;
import java.awt.Toolkit;

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
    private final DynamicPosition mousePosition;

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

        this.mousePosition = new DynamicPosition(0, 0);

        GLFW.glfwSetMouseButtonCallback(this.windowId, (window, button, action, mods) -> {
            final MouseButton mouseButton = MouseButton.getByGlfwId(button);
            final ClickAction clickAction = ClickAction.getByGlfwId(action);

            if (mouseButton == null || clickAction == null || inputListener == null)
                return;

            final MouseClickEvent event = new MouseClickEvent(mouseButton, clickAction,
                this.mousePosition.x, this.mousePosition.y);
            this.inputListener.mouseClick(event);
        });

        GLFW.glfwSetCursorPosCallback(this.windowId, (window, xToDouble, yToDouble) -> {
            if (this.inputListener == null)
                return;

            final int xTo = (int) xToDouble;
            final int yTo = (int) yToDouble;
            final MouseMoveEvent event = new MouseMoveEvent(mousePosition.x, mousePosition.y,
                xTo, yTo);
            this.mousePosition.x = xTo;
            this.mousePosition.y = yTo;
            this.inputListener.mouseMove(event);
        });

        GLFW.glfwSetScrollCallback(this.windowId, (window, xoffset, yoffset) -> {
            if (this.inputListener == null)
                return;

            final MouseScrollEvent event = new MouseScrollEvent(ScrollDirection.fromValue(yoffset),
                this.mousePosition.x, this.mousePosition.y);
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

    public Position getMousePosition() {
        return this.mousePosition;
    }

    public boolean isKeyPressed(KeyButton button) {
        return GLFW.glfwGetKey(this.windowId, button.getGlfwId()) == GLFW.GLFW_PRESS;
    }

    public Size getSize() {
        final int[] widthPointer = new int[1];
        final int[] heightPointer = new int[1];
        GLFW.glfwGetFramebufferSize(windowId, widthPointer, heightPointer);
        return new DynamicSize(widthPointer[0], heightPointer[0]);
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
        final Size windowSize = getSize();
        GLFW.glfwSetWindowPos(this.windowId,
            (screenSize.width - windowSize.getWidth()) / 2,
            (screenSize.height - windowSize.getHeight()) / 2);
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
