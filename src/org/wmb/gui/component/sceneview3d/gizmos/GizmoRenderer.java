package org.wmb.gui.component.sceneview3d.gizmos;

import org.lwjgl.opengl.GL30;
import org.wmb.rendering.AllocatedMeshData;
import org.wmb.rendering.AllocatedShaderProgram;
import org.wmb.rendering.Camera;
import org.wmb.rendering.MeshDataDescription;
import org.wmb.rendering.OpenGLStateException;
import org.wmb.rendering.math.ObjectPosition;
import org.wmb.rendering.math.ObjectTransform;

import java.io.IOException;

public final class GizmoRenderer {

    private final AllocatedShaderProgram gizmoShaderProgram;
    private final AllocatedMeshData translationGizmoMeshData;
    private final int transformUl;
    private final int viewUl;
    private final int projectionUl;

    public GizmoRenderer() throws IOException {
        try {
            this.gizmoShaderProgram = AllocatedShaderProgram.fromResources(
                "/org/wmb/gui/component/sceneview3d/gizmos/gizmo_vs.glsl",
                "/org/wmb/gui/component/sceneview3d/gizmos/gizmo_fs.glsl"
            );
        } catch (IOException exception) {
            throw new IOException("(GizmoShaderProgram) " + exception.getMessage());
        }

        try {
            this.transformUl = gizmoShaderProgram.getUniformLocation("u_transform");
            this.viewUl = gizmoShaderProgram.getUniformLocation("u_view");
            this.projectionUl = gizmoShaderProgram.getUniformLocation("u_projection");
        } catch (OpenGLStateException exception) {
            this.gizmoShaderProgram.delete();
            throw new OpenGLStateException("(GizmoShaderProgram) " + exception.getMessage());
        }

        try {
            final float gShort = 0.02f;
            final float gLong = 0.6f;

            final MeshDataDescription meshDataDescription = new MeshDataDescription();
            meshDataDescription.addDataArray(3, new float[] {
                // x front
                0.0f, gShort, gShort,
                0.0f, -gShort, gShort,
                gLong, -gShort, gShort,
                gLong, gShort, gShort,

                // x back
                0.0f, gShort, -gShort,
                0.0f, -gShort, -gShort,
                gLong, -gShort, -gShort,
                gLong, gShort, -gShort,

                // x top
                0.0f, gShort, -gShort,
                0.0f, gShort, gShort,
                gLong, gShort, gShort,
                gLong, gShort, -gShort,

                // x bottom
                0.0f, -gShort, -gShort,
                0.0f, -gShort, gShort,
                gLong, -gShort, gShort,
                gLong, -gShort, -gShort,

                // y front
                gShort, 0.0f, gShort,
                -gShort, 0.0f, gShort,
                -gShort, gLong, gShort,
                gShort, gLong, gShort,

                // y back
                gShort, 0.0f, -gShort,
                -gShort, 0.0f, -gShort,
                -gShort, gLong, -gShort,
                gShort, gLong, -gShort,

                // y right
                gShort, 0.0f, -gShort,
                gShort, 0.0f, gShort,
                gShort, gLong, gShort,
                gShort, gLong, -gShort,

                // y left
                -gShort, 0.0f, -gShort,
                -gShort, 0.0f, gShort,
                -gShort, gLong, gShort,
                -gShort, gLong, -gShort,

                // z top
                gShort, gShort, 0.0f,
                -gShort, gShort, 0.0f,
                -gShort, gShort, gLong,
                gShort, gShort, gLong,

                // z bottom
                gShort, -gShort, 0.0f,
                -gShort, -gShort, 0.0f,
                -gShort, -gShort, gLong,
                gShort, -gShort, gLong,

                // z right
                gShort, -gShort, 0.0f,
                gShort, gShort, 0.0f,
                gShort, gShort, gLong,
                gShort, -gShort, gLong,

                // z left
                -gShort, -gShort, 0.0f,
                -gShort, gShort, 0.0f,
                -gShort, gShort, gLong,
                -gShort, -gShort, gLong
            });
            meshDataDescription.addDataArray(3, new float[] {
                // x front
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                // x back
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                // x top
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                // x bottom
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                // y front
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                // y back
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                // y right
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                // y left
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                // z top
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                // z bottom
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                // z right
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                // z left
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f
            });
            meshDataDescription.setIndexArray(new short[] {
                // x front
                0, 1, 2,
                2, 3, 0,

                // x back
                4, 5, 6,
                6, 7, 4,

                // x top
                8, 9, 10,
                10, 11, 8,

                // x bottom
                12, 13, 14,
                14, 15, 12,

                // y front
                16, 17, 18,
                18, 19, 16,

                // y back
                20, 21, 22,
                22, 23, 20,

                // y right
                24, 25, 26,
                26, 27, 24,

                // y left
                28, 29, 30,
                30, 31, 28,

                // z top
                32, 33, 34,
                34, 35, 32,

                // z bottom
                36, 37, 38,
                38, 39, 36,

                // z right
                40, 41, 42,
                42, 43, 40,

                // z left
                44, 45, 46,
                46, 47, 44
            });
            this.translationGizmoMeshData = new AllocatedMeshData(meshDataDescription);
        } catch(OpenGLStateException exception) {
            this.gizmoShaderProgram.delete();
            throw new OpenGLStateException("(TranslationGizmoMeshData) " + exception.getMessage());
        }
    }

    public void delete() {
        this.gizmoShaderProgram.delete();
        this.translationGizmoMeshData.delete();
    }

    public void preparePipeline(int x, int y, int width, int height) {
        GL30.glViewport(x, y, width, height);
        GL30.glUseProgram(this.gizmoShaderProgram.getId());
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_BLEND);
    }

    public void uniformCamera(Camera camera, float aspect) {
        AllocatedShaderProgram.uniformMat4(this.viewUl, camera.getViewMatrix());
        AllocatedShaderProgram.uniformMat4(this.projectionUl, camera.getProjectionMatrix(aspect));
    }

    public void renderTranslationGizmo(ObjectPosition position) {
        GL30.glBindVertexArray(this.translationGizmoMeshData.getId());
        AllocatedShaderProgram.uniformMat4(this.transformUl, position.getAsMatrix());

        GL30.glDrawElements(GL30.GL_TRIANGLES, this.translationGizmoMeshData.vertexCount,
            GL30.GL_UNSIGNED_SHORT, 0);

        GL30.glBindVertexArray(0);
    }

    public void resetPipeline() {
        GL30.glUseProgram(0);
    }
}
