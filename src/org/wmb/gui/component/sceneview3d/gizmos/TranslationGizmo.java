package org.wmb.gui.component.sceneview3d.gizmos;

import org.bfg.Context;
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

public final class TranslationGizmo implements Gizmo {

    private final AllocatedMeshData meshData;
    private GizmoAxis draggingAxis;
    private AAPlane draggingPlane;

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
                this.draggingPlane = getBestDraggingPlane(this.draggingAxis, lookVector);
            } else {
                this.draggingAxis = null;
                this.draggingPlane = null;
            }
        }
    }

    private AAPlane getBestDraggingPlane(GizmoAxis axis, Vector3f lookVector) {
        final float xzFactor = Math.max(lookVector.dot(0.0f, 1.0f, 0.0f),
            lookVector.dot(0.0f, -1.0f, 0.0f));
        final float xyFactor = Math.max(lookVector.dot(0.0f, 0.0f, 1.0f),
            lookVector.dot(0.0f, 0.0f, -1.0f));
        final float yzFactor = Math.max(lookVector.dot(1.0f, 0.0f, 0.0f),
            lookVector.dot(-1.0f, 0.0f, 0.0f));

        return switch (axis) {
            case X -> xzFactor > xyFactor ? AAPlane.XZ : AAPlane.XY;
            case Y -> xyFactor > yzFactor ? AAPlane.XY : AAPlane.YZ;
            case Z -> xzFactor > yzFactor ? AAPlane.XZ : AAPlane.YZ;
        };
    }

    @Override
    public void handleMouseMove(Vector4f mouseRay, WmbContext context, Camera camera) {
        if (this.draggingAxis != null && this.draggingPlane != null) {
            final Element selectedElement = context.getSelectedElement();
            if (selectedElement instanceof Object3dElement) {
                final Object3dElement object3dElement = (Object3dElement) selectedElement;
                float planePosition = 0.0f;

                switch (this.draggingPlane) {
                    case XY -> planePosition = object3dElement.getTransform().getPosition().getZ();
                    case XZ -> planePosition = object3dElement.getTransform().getPosition().getY();
                    case YZ -> planePosition = object3dElement.getTransform().getPosition().getX();
                }

                final Vector3f intersection = intersect(mouseRay, this.draggingPlane,
                    planePosition, camera);
                switch (this.draggingAxis) {
                    case X -> object3dElement.getTransform().getPosition().setX(intersection.x);
                    case Y -> object3dElement.getTransform().getPosition().setY(intersection.y);
                    case Z -> object3dElement.getTransform().getPosition().setZ(intersection.z);
                }

                context.getGui().notifyReadScene();
            }
        }
    }

    private Vector3f intersect(Vector4f ray, AAPlane plane, float planePosition, Camera camera) {
        final Vector3f intersection = new Vector3f();
        switch (plane) {
            case XZ:
                final float yDiff = planePosition - camera.getY();
                final float ySteps = yDiff / ray.y;
                intersection.x = camera.getX() + ray.x * ySteps;
                intersection.y = planePosition;
                intersection.z = camera.getZ() + ray.z * ySteps;
                break;
            case XY:
                final float zDiff = planePosition - camera.getZ();
                final float zSteps = zDiff / ray.z;
                intersection.x = camera.getX() + ray.x * zSteps;
                intersection.y = camera.getY() + ray.y * zSteps;
                intersection.z = planePosition;
                break;
            case YZ:
                final float xDiff = planePosition - camera.getX();
                final float xSteps = xDiff / ray.x;
                intersection.x = planePosition;
                intersection.y = camera.getY() + ray.y * xSteps;
                intersection.z = camera.getZ() + ray.z * xSteps;
                break;
        }

        return intersection;
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

    private enum AAPlane {
        XZ,
        XY,
        YZ
    }
}
