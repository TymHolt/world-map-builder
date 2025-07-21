package org.wmb.rendering;

import org.lwjgl.opengl.GL30;

public final class Renderer {

    private final AllocatedShaderProgram shaderProgram;

    public Renderer() {
        shaderProgram = new AllocatedShaderProgram(
                "#version 330 core\n" +
                "in vec3 i_pos;\n" +
                "void main() {\n" +
                "    gl_Position = vec4(vec3(i_pos.x + 0.25, i_pos.y, i_pos.z), 1.0);\n" +
                "}",
                "#version 330 core\n" +
                 "out vec4 o_color;\n" +
                 "void main() {\n" +
                 "    o_color = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                 "}"
        );
    }

    public void delete() {
        shaderProgram.delete();
    }

    public void render(AllocatedVertexData vertexData) {
        GL30.glUseProgram(shaderProgram.getId());
        GL30.glBindVertexArray(vertexData.getId());
        GL30.glEnableVertexAttribArray(0);

        GL30.glDrawElements(GL30.GL_TRIANGLES, vertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);
    }
}
