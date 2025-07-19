package org.wmb.rendering;

import org.lwjgl.opengl.GL30;

public final class Renderer {

    public static void render(AllocatedVertexData vertexData) {
        GL30.glBindVertexArray(vertexData.getId());
        GL30.glEnableVertexAttribArray(0);

        GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, vertexData.getVertexCount());

        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
