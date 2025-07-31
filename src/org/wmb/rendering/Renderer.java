package org.wmb.rendering;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

public final class Renderer {

    private final AllocatedShaderProgram shaderProgram;
    private final int textureUl, transformUl, viewUl, projectionUl;

    public Renderer() {
        shaderProgram = new AllocatedShaderProgram(
                "#version 330 core\n" +
                "layout(location=0) in vec3 i_pos;\n" +
                "layout(location=1) in vec2 i_texCoord;\n" +
                "uniform mat4 u_transform;\n" +
                "uniform mat4 u_view;\n" +
                "uniform mat4 u_projection;\n" +
                "out vec2 p_texCoord;\n" +
                "void main() {\n" +
                "    p_texCoord = i_texCoord;\n" +
                "    gl_Position = u_projection * u_view * u_transform *" +
                "        vec4(vec3(i_pos.x, i_pos.y, i_pos.z), 1.0);\n" +
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
        transformUl = shaderProgram.getUniformLocation("u_transform");
        viewUl = shaderProgram.getUniformLocation("u_view");
        projectionUl = shaderProgram.getUniformLocation("u_projection");
    }

    public void delete() {
        shaderProgram.delete();
    }

    public void begin() {
        GL30.glUseProgram(shaderProgram.getId());
        GL30.glUniform1i(textureUl, 0);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
    }

    public void uniformCamera(Camera camera, float aspect) {
        AllocatedShaderProgram.uniformMat4(viewUl, camera.getViewMatrix());
        AllocatedShaderProgram.uniformMat4(projectionUl, camera.getProjectionMatrix(aspect));
    }

    public void render(IRenderObject renderObject) {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, renderObject.getTexture().getId());
        GL30.glBindVertexArray(renderObject.getVertexData().getId());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        AllocatedShaderProgram.uniformMat4(transformUl, renderObject.getTransformMatrix());
        GL30.glDrawElements(GL30.GL_TRIANGLES, renderObject.getVertexData().getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public void end() {
        GL30.glUseProgram(0);
    }
}
