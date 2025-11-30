package org.wmb.gui.component.sceneview3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.wmb.Log;
import org.wmb.WmbContext;
import org.wmb.gui.Window;
import org.wmb.gui.component.IconSwitchComponent;
import org.wmb.gui.component.container.ContainerComponent;
import org.wmb.gui.component.sceneview3d.gizmos.Gizmo;
import org.wmb.gui.component.sceneview3d.gizmos.GizmoAxis;
import org.wmb.gui.component.sceneview3d.gizmos.GizmoRenderer;
import org.wmb.gui.component.sceneview3d.gizmos.TranslationGizmo;
import org.wmb.gui.data.Bounds;
import org.wmb.gui.data.Position;
import org.wmb.gui.data.Size;
import org.wmb.gui.icon.Icon;
import org.wmb.gui.input.*;
import org.wmb.gui.GuiGraphics;
import org.wmb.editor.Scene3d;
import org.wmb.editor.element.Element;
import org.wmb.editor.element.Object3dElement.Object3dElement;
import org.wmb.editor.element.Object3dElement.Object3dElementRenderer;
import org.wmb.rendering.AllocatedFramebuffer;
import org.wmb.rendering.Camera;
import org.wmb.rendering.Color;
import org.wmb.rendering.Colors;
import org.wmb.rendering.DynamicColor;
import org.wmb.rendering.OpenGLStateException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public final class SceneView3dComponent extends ContainerComponent {

    private final WmbContext context;
    private AllocatedFramebuffer framebuffer;
    private AllocatedFramebuffer gizmoFramebuffer;
    private int lastWidth;
    private int lastHeight;
    private float aspect;
    private final GizmoRenderer gizmoRenderer;
    private final GridLineRenderer gridLineRenderer;
    private final Object3dElementRenderer object3dElementRenderer;
    private final Camera camera;
    private boolean rotatingCamera;
    private final float fov;

    private final IconSwitchComponent translationGizmoSwitch;
    private final IconSwitchComponent rotationGizmoSwitch;
    private final IconSwitchComponent scaleGizmoSwitch;
    private final TranslationGizmo translationGizmo;
    private Gizmo activeGizmo;
    private GizmoAxis hoveredAxis;

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

        try {
            this.translationGizmo = new TranslationGizmo();
        } catch (OpenGLStateException exception) {
            this.gizmoFramebuffer.delete();
            this.framebuffer.delete();
            this.object3dElementRenderer.delete();
            this.gizmoRenderer.delete();
            this.gridLineRenderer.delete();
            throw new OpenGLStateException("(TranslationGizmo) " + exception.getMessage());
        }

        this.fov = 70.0f;
        this.camera = new Camera(0.0f, 8.0f, 8.0f, 45.0f, 0.0f, this.fov, 0.1f, 128.0f);
        this.rotatingCamera = false;
        this.lastWidth = -1;
        this.lastHeight = -1;
        this.aspect = 1.0f;
        this.hoveredAxis = null;
        this.activeGizmo = null;

        this.translationGizmoSwitch = new IconSwitchComponent(Icon.GIZMO_TRANSLATE);
        this.rotationGizmoSwitch = new IconSwitchComponent(Icon.GIZMO_ROTATE);
        this.scaleGizmoSwitch = new IconSwitchComponent(Icon.GIZMO_SCALE);

        this.translationGizmoSwitch.setSwitchListener(switchComponent -> {
            if (switchComponent.isSelected()) {
                this.activeGizmo = translationGizmo;
                this.rotationGizmoSwitch.setSelected(false);
                this.scaleGizmoSwitch.setSelected(false);
            } else if(this.activeGizmo == translationGizmo)
                this.activeGizmo = null;
        });
        this.translationGizmoSwitch.setUnselectedGrayScaleMode(true);
        addComponent(this.translationGizmoSwitch);

        this.rotationGizmoSwitch.setSwitchListener(switchComponent -> {
            if (switchComponent.isSelected()) {
                this.translationGizmoSwitch.setSelected(false);
                this.scaleGizmoSwitch.setSelected(false);
            }
        });
        this.rotationGizmoSwitch.setUnselectedGrayScaleMode(true);
        addComponent(this.rotationGizmoSwitch);

        this.scaleGizmoSwitch.setSwitchListener(switchComponent -> {
            if (switchComponent.isSelected()) {
                this.translationGizmoSwitch.setSelected(false);
                this.rotationGizmoSwitch.setSelected(false);
            }
        });
        this.scaleGizmoSwitch.setUnselectedGrayScaleMode(true);
        addComponent(this.scaleGizmoSwitch);
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
            this.aspect = (float) this.lastWidth / (float) this.lastHeight;
        }

        handleUpdateInput();

        // ---------- Render Gizmo -----------------------------------------------------------------

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.gizmoFramebuffer.getFboId());
        GL30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.hoveredAxis = null;
        final Element selectedElement = this.context.getSelectedElement();
        if (selectedElement instanceof Object3dElement && this.activeGizmo != null) {
            final Object3dElement object3dElement = (Object3dElement) selectedElement;
            this.gizmoRenderer.preparePipeline(0, 0, innerBoundsWidth, innerBoundsHeight,
                this.camera, this.aspect);
            this.gizmoRenderer.renderGizmo(this.activeGizmo, object3dElement);
            this.gizmoRenderer.resetPipeline();

            final Position mousePosition = this.context.getWindow().getMousePosition();
            if (innerBounds.contains(mousePosition)) {
                final Color color = getPixelColor(mousePosition.getX(), mousePosition.getY());
                this.hoveredAxis = GizmoAxis.fromColor(color);
            }
        }

        // ---------- Render Scene -----------------------------------------------------------------

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.framebuffer.getFboId());
        GL30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.gridLineRenderer.render(0, 0, innerBoundsWidth, innerBoundsHeight, this.camera,
            this.aspect, Colors.GREY);
        this.object3dElementRenderer.preparePipeline(0, 0, innerBoundsWidth, innerBoundsHeight);
        this.object3dElementRenderer.uniformCamera(this.camera, this.aspect);
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
            this.object3dElementRenderer.render((Object3dElement) element, Colors.WHITE, factor);
        }

        for (Element child : element.getChildren())
            recursiveRender(child);
    }

    @Override
    public void draw(GuiGraphics graphics) {
        final Bounds bounds = getInnerBounds();
        graphics.fillQuadTexture(bounds, this.framebuffer);
        graphics.fillQuadTexture(bounds, this.gizmoFramebuffer);
        super.draw(graphics);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        final Size gizmoSize = this.translationGizmoSwitch.getRequestedSize();
        final int gizmoWidth = gizmoSize.getWidth();
        final int gizmoHeight = gizmoSize.getHeight();
        final int gizmoX = x + gizmoWidth;
        int currentY = y + gizmoHeight;

        this.translationGizmoSwitch.setBounds(gizmoX, currentY, gizmoWidth, gizmoHeight);
        currentY += gizmoHeight * 2;

        this.rotationGizmoSwitch.setBounds(gizmoX, currentY, gizmoWidth, gizmoHeight);
        currentY += gizmoHeight * 2;

        this.scaleGizmoSwitch.setBounds(gizmoX, currentY, gizmoWidth, gizmoHeight);
        currentY += gizmoHeight * 2;
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
    public void onMouseClick(MouseClickEvent event) {
        super.onMouseClick(event);

        if (event.button == MouseButton.MIDDLE)
            this.rotatingCamera = event.action == ClickAction.PRESS;

        if (this.activeGizmo != null)
            this.activeGizmo.handleMouseClick(event, this.camera.getLookVector(), this.hoveredAxis);
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

        if (this.activeGizmo != null)
            this.activeGizmo.handleMouseMove(getMouseRay(event.xTo, event.yTo), this.context,
                this.camera);
    }

    @Override
    public void onLooseFocus() {
        this.rotatingCamera = false;
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        return this.hoveredAxis != null ? Cursor.HAND : super.getCursor(mouseX, mouseY);
    }

    private void getPixelColor(int x, int y, DynamicColor color) {
        final Bounds innerBounds = getInnerBounds();
        x = x - innerBounds.getX();
        y = lastHeight - (y - innerBounds.getY()) - 1;

        final ByteBuffer pixelData = BufferUtils.createByteBuffer(4);
        GL30.glReadPixels(x, y, 1, 1, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, pixelData);

        color.set(
            (float) (pixelData.get(0) & 0xFF) / 255.0f,
            (float) (pixelData.get(1) & 0xFF) / 255.0f,
            (float) (pixelData.get(2) & 0xFF) / 255.0f,
            1.0f
        );
    }

    private Color getPixelColor(int x, int y) {
        final DynamicColor color = new DynamicColor(0.0f, 0.0f, 0.0f, 1.0f);
        getPixelColor(x, y, color);
        return color;
    }

    private void getMouseRay(int x, int y, Vector4f destination) {
        final Bounds innerBounds = getInnerBounds();
        final float innerBoundsWidth = (float) innerBounds.getWidth();
        final float innerBoundsHeight = (float) innerBounds.getHeight();
        final float localX = (float) (x - innerBounds.getX());
        final float localY = (float) (y - innerBounds.getY());

        destination.x = (2.0f * localX) / innerBoundsWidth - 1.0f;
        destination.y = 1.0f - (2.0f * localY) / innerBoundsHeight;
        destination.z = -1.0f;
        destination.w = 1.0f;

        final Matrix4f projectionInverse = this.camera.getProjectionMatrixInverse(this.aspect);
        projectionInverse.transform(destination);
        destination.z = -1.0f;
        destination.w = 0.0f;
        destination.normalize();

        this.camera.getLookRotationMatrix().transform(destination);
    }

    private Vector4f getMouseRay(int x, int y) {
        final Vector4f ray = new Vector4f();
        getMouseRay(x, y, ray);
        return ray;
    }

    public void delete() {
        this.framebuffer.delete();
        this.gizmoFramebuffer.delete();
        this.gizmoRenderer.delete();
        this.object3dElementRenderer.delete();
        this.gridLineRenderer.delete();
    }
}
