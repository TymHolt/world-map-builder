package org.wmb.gui.input;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public enum ClickAction {

    PRESS(GLFW.GLFW_PRESS),
    RELEASE(GLFW.GLFW_RELEASE),
    REPEAT(GLFW.GLFW_REPEAT);

    private static final HashMap<Integer, ClickAction> glfwMapping;

    static {
        glfwMapping = new HashMap<>();

        for (ClickAction clickAction : values())
            glfwMapping.put(clickAction.glfwId, clickAction);
    }

    public static ClickAction getByGlfwId(int glfwId) {
        return glfwMapping.get(glfwId);
    }

    private final int glfwId;

    ClickAction(int glfwId) {
        this.glfwId = glfwId;
    }

    public boolean isPressOrRepeat() {
        return this == PRESS || this == REPEAT;
    }

    public int getGlfwId() {
        return glfwId;
    }
}
