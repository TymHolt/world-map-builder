package org.wmb;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public final class Main {

    public static void main(String[] args) {
        System.out.println("LWJGL Version: " + Version.getVersion());
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit())
            throw new IllegalStateException("GLFW could not be initialized");

        new WmbInstance();

        while (WmbInstance.hasInstances()) {
            WmbInstance.updateAllInstances();
        }

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
