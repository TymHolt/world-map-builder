package org.wmb.gui.input;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public enum Cursor {

    DEFAULT(0),
    ARROW(GLFW.GLFW_ARROW_CURSOR),
    I_BEAM(GLFW.GLFW_IBEAM_CURSOR),
    CROSSHAIR(GLFW.GLFW_CROSSHAIR_CURSOR),
    HAND(GLFW.GLFW_HAND_CURSOR),
    H_RESIZE(GLFW.GLFW_HRESIZE_CURSOR),
    V_RESIZE(GLFW.GLFW_VRESIZE_CURSOR);

    private static final HashMap<Integer, Cursor> glfwMapping;

    static {
        glfwMapping = new HashMap<>();

        for (Cursor cursor : values())
            glfwMapping.put(cursor.glfwId, cursor);
    }

    public static Cursor getByGlfwId(int glfwId) {
        return glfwMapping.get(glfwId);
    }

    private final int glfwId;

    Cursor(int glfwId) {
        this.glfwId = glfwId;
    }

    public int getGlfwId() {
        return glfwId;
    }
}
