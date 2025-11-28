package org.wmb.gui.component.sceneview3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.wmb.Log;
import org.wmb.WmbContext;
import org.wmb.gui.Window;
import org.wmb.gui.component.Component;
import org.wmb.gui.component.sceneview3d.gizmos.GizmoRenderer;
import org.wmb.gui.data.Bounds;
import org.wmb.gui.data.Position;
import org.wmb.gui.input.*;
import org.wmb.gui.GuiGraphics;
import org.wmb.editor.Scene3d;
import org.wmb.editor.element.Element;
import org.wmb.editor.element.Object3dElement.Object3dElement;
import org.wmb.editor.element.Object3dElement.Object3dElementRenderer;
import org.wmb.rendering.AllocatedFramebuffer;
import org.wmb.rendering.Camera;
import org.wmb.rendering.Color;
import org.wmb.rendering.OpenGLStateException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public final class SceneView3dComponent extends Component {

    private enum GizmoAxis {
        X, Y, Z
    }

    private enum AAPlane {
        XZ,
        XY,
        YZ
    }

    private final WmbContext context;
    private AllocatedFramebuffer framebuffer;
    private AllocatedFramebuffer gizmoFramebuffer;
    private int lastWidth;
    private int lastHeight;
    private final GizmoRenderer gizmoRenderer;
    private final GridLineRenderer gridLineRenderer;
    private final Object3dElementRenderer object3dElementRenderer;
    private final Camera camera;
    private boolean rotatingCamera;
    private final float fov;
    private GizmoAxis hoveredAxis;
    private GizmoAxis draggingAxis;
    private AAPlane draggingPlane;

    public SceneView3dComponent(WmbContext context) throws IOException {
        super();
        Objects.requireNonNull(context, "Context is null");
        this.context = context;

        try {
            this.framebuffer = new AllocatedFramebuffer(2, 2);
        } catch (OpenGLStateException exception) {
            throw new OpenGLStateException("(Framebuffer) " + exception.getMessage());
        }

        try {
            this.gizmoFramebuffer = new AllocatedFramebuffer(2, 2);
        } catch (OpenGLStateException exception) {
            this.framebuffer.delete();
            throw new OpenGLStateException("(GizmoFramebuffer) " + exception.getMessage());
        }

        try {
            this.gizmoRenderer = new GizmoRenderer();
        } catch (IOException exception) {
            this.gizmoFramebuffer.delete();
            this.framebuffer.delete();
            throw new OpenGLStateException("(GizmoRenderer) " + exception.getMessage());
        }

        try {
            this.object3dElementRenderer = new Object3dElementRenderer();
        } catch (IOException exception) {
            this.gizmoFramebuffer.delete();
            this.framebuffer.delete();
            this.gizmoRenderer.delete();
            throw new OpenGLStateException("(Object3dElementRenderer) " + exception.getMessage());
        }

        try {
            this.gridLineRenderer = new GridLineRenderer(8);
        } catch (IOException exception) {
            this.gizmoFramebuffer.delete();
            this.framebuffer.delete();
            this.object3dElementRenderer.delete();
            this.gizmoRenderer.delete();
            throw new IOException("(GridLineRenderer) " + exception.getMessage());
        }

        this.fov = 70.0f;
        this.camera = new Camera(0.0f, 8.0f, 8.0f, 45.0f, 0.0f, this.fov, 0.1f, 128.0f);
        this.rotatingCamera = false;
        this.lastWidth = -1;
        this.lastHeight = -1;
        this.hoveredAxis = null;
        this.draggingAxis = null;
        this.draggingPlane = null;
    }

    public void renderScene(Scene3d scene) throws OpenGLStateException {
        final Bounds innerBounds = getInnerBounds();
        final int innerBoundsWidth = innerBounds.getWidth();
        final int innerBoundsHeight = innerBounds.getHeight();
        if (innerBoundsWidth != this.lastWidth || innerBoundsHeight != this.lastHeight) {
            try {
                resizeFramebuffer(innerBoundsWidth, innerBoundsHeight);
            } catch (OpenGLStateException exception) {
                Log.debug("New framebuffer size: " + innerBoundsWidth + "x" + innerBoundsHeight);
                throw new OpenGLStateException("(Prepare GuiGraphics)" + exception);
            }

            this.lastWidth = innerBoundsWidth;
            this.lastHeight = innerBoundsHeight;
        }

        handleUpdateInput();
        final float aspect = (float) innerBoundsWidth / (float) innerBoundsHeight;

        // ---------- Render Gizmo -----------------------------------------------------------------

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.gizmoFramebuffer.getFboId());

        GL30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.hoveredAxis = null;
        final Element selectedElement = this.context.getSelectedElement();
        if (selectedElement instanceof Object3dElement) {
            final Object3dElement object3dElement = (Object3dElement) selectedElement;
            this.gizmoRenderer.preparePipeline(0, 0, innerBoundsWidth, innerBoundsHeight);
            this.gizmoRenderer.uniformCamera(this.camera, aspect);
            this.gizmoRenderer.renderTranslationGizmo(object3dElement.getTransform().getPosition());
            this.gizmoRenderer.resetPipeline();

            final Position mousePosition = this.context.getWindow().getMousePosition();
            if (innerBounds.contains(mousePosition)) {
                final int x = (mousePosition.getX() - innerBounds.getX());
                final int y = lastHeight - (mousePosition.getY() - innerBounds.getY()) - 1;

                final ByteBuffer pixelData = BufferUtils.createByteBuffer(4);
                GL30.glReadPixels(x, y, 1, 1, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, pixelData);

                final int red = pixelData.get(0) & 0xFF;
                final int green = pixelData.get(1) & 0xFF;
                final int blue = pixelData.get(2) & 0xFF;

                if (red > 0)
                    this.hoveredAxis = GizmoAxis.X;
                else if (green > 0)
                    this.hoveredAxis = GizmoAxis.Y;
                else if(blue > 0)
                    this.hoveredAxis = GizmoAxis.Z;
            }
        }

        // ---------- Render Scene -----------------------------------------------------------------

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.framebuffer.getFboId());

        GL30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.gridLineRenderer.render(0, 0, innerBoundsWidth, innerBoundsHeight, this.camera, aspect,
            Color.GREY);
        this.object3dElementRenderer.preparePipeline(0, 0, innerBoundsWidth, innerBoundsHeight);
        this.object3dElementRenderer.uniformCamera(this.camera, aspect);
        recursiveRender(scene);
        this.object3dElementRenderer.resetPipeline();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        // -----------------------------------------------------------------------------------------
    }

    private void recursiveRender(Element element) {
        if (element == null)
            return;

        if (element instanceof Object3dElement) {
            final float factor = this.context.getSelectedElement() == element ? 0.2f : 0.0f;
            this.object3dElementRenderer.render((Object3dElement) element, Color.WHITE, factor);
        }

        for (Element child : element.getChildren())
            recursiveRender(child);
    }

    @Override
    public void onMouseScroll(MouseScrollEvent event) {
        final float direction = event.direction == ScrollDirection.UP ? 1.0f : -1.0f;
        this.camera.move(this.camera.getLookVector().mul(direction));
    }

    private static final float CAMERA_MOVE_SPEED = 8.0f;
    private long lastUpdate = System.currentTimeMillis();

    private void handleUpdateInput() {
        final long currentTime = System.currentTimeMillis();
        final long deltaTimeLong = currentTime - this.lastUpdate;
        this.lastUpdate = currentTime;
        final float deltaTime = (float) deltaTimeLong / 1000.0f;
        final float moveDelta = CAMERA_MOVE_SPEED * deltaTime;

        if (this.context.getGui().isListeningForKeyboard())
            return;

        final Matrix4f rotationMatrix = camera.getLookYawRotationMatrix();
        final Vector4f forward = rotationMatrix.transform(
            new Vector4f(0.0f, 0.0f, -moveDelta, 1.0f));
        final Vector4f right = rotationMatrix.transform(
            new Vector4f(moveDelta, 0.0f, 0.0f, 1.0f));

        final Window window = context.getWindow();
        if (window.isKeyPressed(KeyButton.W))
            camera.move(forward);
        if (window.isKeyPressed(KeyButton.S))
            camera.move(forward.mul(-1.0f));
        if (window.isKeyPressed(KeyButton.D))
            camera.move(right);
        if (window.isKeyPressed(KeyButton.A))
            camera.move(right.mul(-1.0f));
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);
        final Bounds bounds = getInnerBounds();
        graphics.fillQuadTexture(bounds, this.framebuffer);
        graphics.fillQuadTexture(bounds, this.gizmoFramebuffer);
    }

    private void resizeFramebuffer(int width, int height) throws OpenGLStateException {
        final AllocatedFramebuffer newFramebuffer;
        try {
            newFramebuffer = new AllocatedFramebuffer(width, height);
        } catch (OpenGLStateException exception) {
            throw new OpenGLStateException("(Framebuffer) " + exception.getMessage());
        }

        final AllocatedFramebuffer newGizmoFramebuffer;
        try {
            newGizmoFramebuffer = new AllocatedFramebuffer(width, height);
        } catch (OpenGLStateException exception) {
            newFramebuffer.delete();
            throw new OpenGLStateException("(GizmoFramebuffer) " + exception.getMessage());
        }

        this.framebuffer.delete();
        this.framebuffer = newFramebuffer;
        this.gizmoFramebuffer.delete();
        this.gizmoFramebuffer = newGizmoFramebuffer;
    }

    public void delete() {
        this.framebuffer.delete();
        this.gizmoFramebuffer.delete();
        this.gizmoRenderer.delete();
        this.object3dElementRenderer.delete();
        this.gridLineRenderer.delete();
    }

    @Override
    public void onMouseClick(MouseClickEvent event) {
        if (event.button == MouseButton.MIDDLE) {
            this.rotatingCamera = event.action == ClickAction.PRESS;
            return;
        }

        if (event.button == MouseButton.LEFT) {
            if (event.action == ClickAction.PRESS && this.hoveredAxis != null) {
                this.draggingAxis = this.hoveredAxis;
                this.draggingPlane = getBestDraggingPlane(this.draggingAxis);
            } else {
                this.draggingAxis = null;
                this.draggingPlane = null;
            }
        }
    }

   private AAPlane getBestDraggingPlane(GizmoAxis axis) {
        final Vector3f lookVector = this.camera.getLookVector();
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
    public void onMouseMove(MouseMoveEvent event) {
        if (this.rotatingCamera) {
            final int dx = event.xTo - event.xFrom;
            final int dy = event.yTo - event.yFrom;
            final Bounds bounds = getBounds();
            final float dxf = (float) dx / (float) bounds.getWidth();
            final float dyf = (float) dy / (float) bounds.getHeight();

            this.camera.setPitch(this.camera.getPitch() + (dyf * this.fov * 2.0f));
            this.camera.setYaw(this.camera.getYaw() + (dxf * this.fov * 2.0f));
        }

        if (this.draggingAxis != null && this.draggingPlane != null) {
            final Bounds innerBounds = getInnerBounds();
            final float innerBoundsWidth = (float) innerBounds.getWidth();
            final float innerBoundsHeight = (float) innerBounds.getHeight();
            final float localX = (float) (event.xTo - innerBounds.getX());
            final float localY = (float) (event.yTo - innerBounds.getY());

            final Vector4f ray = new Vector4f(
                (2.0f * localX) / innerBoundsWidth - 1.0f,
                1.0f - (2.0f * localY) / innerBoundsHeight,
                -1.0f,
                1.0f
            );
            final float aspect = innerBoundsWidth / innerBoundsHeight;
            final Matrix4f projectionInverse = this.camera.getProjectionMatrixInverse(aspect);
            projectionInverse.transform(ray);
            ray.z = -1.0f;
            ray.w = 0.0f;
            ray.normalize();

            this.camera.getLookRotationMatrix().transform(ray);

            final Element selectedElement = this.context.getSelectedElement();
            if (selectedElement instanceof Object3dElement) {
                final Object3dElement object3dElement = (Object3dElement) selectedElement;

                float planePosition = 0.0f;
                switch (this.draggingPlane) {
                    case XY -> planePosition = object3dElement.getTransform().getPosition().getZ();
                    case XZ -> planePosition = object3dElement.getTransform().getPosition().getY();
                    case YZ -> planePosition = object3dElement.getTransform().getPosition().getX();
                }

                final Vector3f intersection = intersect(ray, this.draggingPlane, planePosition);
                switch (this.draggingAxis) {
                    case X -> object3dElement.getTransform().getPosition().setX(intersection.x);
                    case Y -> object3dElement.getTransform().getPosition().setY(intersection.y);
                    case Z -> object3dElement.getTransform().getPosition().setZ(intersection.z);
                }

                this.context.getGui().notifyReadScene();
            }
        }
    }

    private Vector3f intersect(Vector4f ray, AAPlane plane, float planePosition) {
        final Vector3f intersection = new Vector3f();
        switch (plane) {
            case XZ:
                final float yDiff = planePosition - this.camera.getY();
                final float ySteps = yDiff / ray.y;
                intersection.x = this.camera.getX() + ray.x * ySteps;
                intersection.y = planePosition;
                intersection.z = this.camera.getZ() + ray.z * ySteps;
                break;
            case XY:
                final float zDiff = planePosition - this.camera.getZ();
                final float zSteps = zDiff / ray.z;
                intersection.x = this.camera.getX() + ray.x * zSteps;
                intersection.y = this.camera.getY() + ray.y * zSteps;
                intersection.z = planePosition;
                break;
            case YZ:
                final float xDiff = planePosition - this.camera.getX();
                final float xSteps = xDiff / ray.x;
                intersection.x = planePosition;
                intersection.y = this.camera.getY() + ray.y * xSteps;
                intersection.z = this.camera.getZ() + ray.z * xSteps;
                break;
        }

        return intersection;
    }

    @Override
    public void onLooseFocus() {
        this.rotatingCamera = false;
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        return this.hoveredAxis != null ? Cursor.HAND : Cursor.DEFAULT;
    }
}
