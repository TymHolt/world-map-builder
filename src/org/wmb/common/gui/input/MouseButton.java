package org.wmb.common.gui.input;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public enum MouseButton {

    LEFT(GLFW.GLFW_MOUSE_BUTTON_LEFT),
    RIGHT(GLFW.GLFW_MOUSE_BUTTON_RIGHT),
    MIDDLE(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);

    private static final HashMap<Integer, MouseButton> glfwMapping;

    static {
        glfwMapping = new HashMap<>();

        for (MouseButton mouseButton : values())
            glfwMapping.put(mouseButton.glfwId, mouseButton);
    }

    public static MouseButton getByGlfwId(int glfwId) {
        return glfwMapping.get(glfwId);
    }

    private final int glfwId;

    MouseButton(int glfwId) {
        this.glfwId = glfwId;
    }

    public int getGlfwId() {
        return glfwId;
    }
}
