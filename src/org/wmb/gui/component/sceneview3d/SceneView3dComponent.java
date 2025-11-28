package org.wmb.gui.component.sceneview3d;

import org.joml.Matrix4f;
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
    private boolean focused;
    private boolean hoversGizmo;

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
        this.focused = false;
        this.hoversGizmo = false;
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

        this.hoversGizmo = false;
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

                final int r = pixelData.get(0) & 0xFF;
                final int g = pixelData.get(1) & 0xFF;
                final int b = pixelData.get(2) & 0xFF;
                this.hoversGizmo = (r + g + b) > 0;
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
        final Matrix4f rotationMatrix = camera.getLookRotationMatrix();
        final Vector4f forward = rotationMatrix.transform(
            new Vector4f(0.0f, 0.0f, -direction, 1.0f));
        camera.move(forward);
    }

    private static final float CAMERA_MOVE_SPEED = 8.0f;
    private long lastUpdate = System.currentTimeMillis();

    private void handleUpdateInput() {
        final long currentTime = System.currentTimeMillis();
        final long deltaTimeLong = currentTime - this.lastUpdate;
        this.lastUpdate = currentTime;
        final float deltaTime = (float) deltaTimeLong / 1000.0f;
        final float moveDelta = CAMERA_MOVE_SPEED * deltaTime;

        if (!this.focused)
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
        if (event.button != MouseButton.MIDDLE)
            return;

        this.rotatingCamera = event.action == ClickAction.PRESS;
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        if (!this.rotatingCamera)
            return;

        final int dx = event.xTo - event.xFrom;
        final int dy = event.yTo - event.yFrom;
        final Bounds bounds = getBounds();
        final float dxf = (float) dx / (float) bounds.getWidth();
        final float dyf = (float) dy / (float) bounds.getHeight();

        this.camera.setPitch(this.camera.getPitch() + (dyf * this.fov * 2.0f));
        this.camera.setYaw(this.camera.getYaw() + (dxf * this.fov * 2.0f));
    }

    @Override
    public void onGainFocus() {
        this.focused = true;
    }

    @Override
    public void onLooseFocus() {
        this.rotatingCamera = false;
        this.focused = false;
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        return this.hoversGizmo ? Cursor.HAND : Cursor.DEFAULT;
    }
}
