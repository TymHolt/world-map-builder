package org.wmb.rendering;

import org.lwjgl.opengl.GL30;

public final class Renderer {

    private final AllocatedShaderProgram shaderProgram;
    private final int textureUl;

    public Renderer() {
        shaderProgram = new AllocatedShaderProgram(
                "#version 330 core\n" +
                "layout(location=0) in vec3 i_pos;\n" +
                "layout(location=1) in vec2 i_texCoord;\n" +
                "out vec2 p_texCoord;\n" +
                "void main() {\n" +
                "    p_texCoord = i_texCoord;\n" +
                "    gl_Position = vec4(vec3(i_pos.x + 0.25, i_pos.y, i_pos.z), 1.0);\n" +
                "}",
                "#version 330 core\n" +
                 "in vec2 p_texCoord;\n" +
                 "uniform sampler2D u_texture;\n" +
                 "out vec4 o_color;\n" +
                 "void main() {\n" +
                 "    o_color =  texture(u_texture, p_texCoord);\n" +
                 "}"
        );
        textureUl = shaderProgram.getUniformLocation("u_texture");
    }

    public void delete() {
        shaderProgram.delete();
    }

    public void render(AllocatedVertexData vertexData, AllocatedTexture texture) {
        GL30.glUseProgram(shaderProgram.getId());
        GL30.glUniform1i(textureUl, 0);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());

        GL30.glBindVertexArray(vertexData.getId());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL30.glDrawElements(GL30.GL_TRIANGLES, vertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);
    }
}
