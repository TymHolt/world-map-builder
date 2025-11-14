package org.wmb.common.gui.input;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public enum MouseButtonAction {

    PRESS(GLFW.GLFW_PRESS),
    RELEASE(GLFW.GLFW_RELEASE);

    private static final HashMap<Integer, MouseButtonAction> glfwMapping;

    static {
        glfwMapping = new HashMap<>();

        for (MouseButtonAction mouseButtonAction : values())
            glfwMapping.put(mouseButtonAction.glfwId, mouseButtonAction);
    }

    public static MouseButtonAction getByGlfwId(int glfwId) {
        return glfwMapping.get(glfwId);
    }

    private final int glfwId;

    MouseButtonAction(int glfwId) {
        this.glfwId = glfwId;
    }

    public int getGlfwId() {
        return glfwId;
    }
}
