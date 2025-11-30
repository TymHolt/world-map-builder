package org.wmb.gui.component.sceneview3d.gizmos;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.wmb.WmbContext;
import org.wmb.editor.element.Element;
import org.wmb.editor.element.Object3dElement.Object3dElement;
import org.wmb.gui.icon.Icon;
import org.wmb.gui.input.ClickAction;
import org.wmb.gui.input.MouseButton;
import org.wmb.gui.input.MouseClickEvent;
import org.wmb.rendering.AllocatedMeshData;
import org.wmb.rendering.Camera;
import org.wmb.rendering.MeshDataDescription;
import org.wmb.rendering.OpenGLStateException;
import org.wmb.rendering.math.ObjectPosition;

public final class TranslationGizmo implements Gizmo {

    private final AllocatedMeshData meshData;
    private GizmoAxis draggingAxis;
    private GizmoAAPlane draggingPlane;

    public TranslationGizmo() throws OpenGLStateException {
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
            this.meshData = new AllocatedMeshData(meshDataDescription);
        } catch(OpenGLStateException exception) {
            throw new OpenGLStateException("(TranslationGizmoMeshData) " + exception.getMessage());
        }

        this.draggingAxis = null;
        this.draggingPlane = null;
    }

    @Override
    public void handleMouseClick(MouseClickEvent event, Vector3f lookVector,
        GizmoAxis hoveredAxis) {
        if (event.button == MouseButton.LEFT) {
            if (event.action == ClickAction.PRESS && hoveredAxis != null) {
                this.draggingAxis = hoveredAxis;
                this.draggingPlane = GizmoAAPlane.getBestPlane(this.draggingAxis, lookVector);
            } else {
                this.draggingAxis = null;
                this.draggingPlane = null;
            }
        }
    }

    @Override
    public void handleMouseMove(Vector4f mouseRay, WmbContext context, Camera camera) {
        if (this.draggingAxis == null || this.draggingPlane == null)
            return;

        final Element selectedElement = context.getSelectedElement();
        if (!(selectedElement instanceof Object3dElement))
            return;

        final Object3dElement object3dElement = (Object3dElement) selectedElement;
        final ObjectPosition objectPosition = object3dElement.getTransform().getPosition();
        final Vector3f planePosition = new Vector3f(
            objectPosition.getX(),
            objectPosition.getY(),
            objectPosition.getZ()
        );

        final Vector3f intersection = this.draggingPlane.intersect(mouseRay, planePosition, camera);
        switch (this.draggingAxis) {
            case X -> objectPosition.setX(intersection.x);
            case Y -> objectPosition.setY(intersection.y);
            case Z -> objectPosition.setZ(intersection.z);
        }

        context.getGui().notifyReadScene();
    }

    @Override
    public void getTransform(Object3dElement object3dElement, Matrix4f destination) {
        destination.set(object3dElement.getTransform().getPosition().getAsMatrix());
    }

    @Override
    public AllocatedMeshData getControlMesh() {
        return this.meshData;
    }

    @Override
    public Icon getIcon() {
        return Icon.GIZMO_TRANSLATE;
    }

    public void delete() {
        this.meshData.delete();
    }
}
