package org.wmb;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public final class Main {

    private static final long MAX_UPS = 30L;

    public static void main(String[] args) {
        System.out.println("LWJGL Version: " + Version.getVersion());
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit())
            throw new IllegalStateException("GLFW could not be initialized");

        new WmbInstance();

        while (WmbInstance.hasInstances()) {
            WmbInstance.updateAllInstances();

            try {
                Thread.sleep(1000L / MAX_UPS);
            } catch (InterruptedException exception) {
                // More error handling not needed
                // Code will only run with higher updates per second
                exception.printStackTrace();
            }
        }

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
