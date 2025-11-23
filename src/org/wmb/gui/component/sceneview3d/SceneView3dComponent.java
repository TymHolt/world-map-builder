package org.wmb.gui.component.sceneview3d;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import org.wmb.WmbContext;
import org.wmb.gui.Window;
import org.wmb.gui.component.Component;
import org.wmb.gui.input.*;
import org.wmb.gui.GuiGraphics;
import org.wmb.editor.Scene3d;
import org.wmb.editor.element.Element;
import org.wmb.editor.element.Object3dElement.Object3dElement;
import org.wmb.editor.element.Object3dElement.Object3dElementRenderer;
import org.wmb.rendering.AllocatedFramebuffer;
import org.wmb.rendering.Camera;
import org.wmb.rendering.Color;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class SceneView3dComponent extends Component {

    private WmbContext context;
    private AllocatedFramebuffer framebuffer;
    private final GridLineRenderer gridLineRenderer;
    private final Object3dElementRenderer object3dElementRenderer;
    private final Camera camera;
    private boolean rotatingCamera;
    private final float fov;

    public SceneView3dComponent(WmbContext context) throws IOException {
        Objects.requireNonNull(context, "Context is null");

        this.context = context;
        this.framebuffer = new AllocatedFramebuffer(2, 2);
        this.object3dElementRenderer = new Object3dElementRenderer();
        this.gridLineRenderer = new GridLineRenderer(8);
        this.fov = 70.0f;
        this.camera = new Camera(0.0f, 8.0f, 8.0f, 45.0f, 0.0f, this.fov, 0.1f, 128.0f);
        this.rotatingCamera = false;
    }

    public void renderScene(Scene3d scene) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.framebuffer.getFboId());

        final Rectangle bounds = getBounds();
        GL30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glEnable(GL30.GL_DEPTH_TEST);

        handleUpdateInput();

        final float aspect = (float) (bounds.getWidth() / bounds.getHeight());
        this.gridLineRenderer.render(0, 0, bounds.width, bounds.height, this.camera, aspect,
            Color.GREY);
        this.object3dElementRenderer.preparePipeline(0, 0, bounds.width, bounds.height);
        this.object3dElementRenderer.uniformCamera(this.camera, aspect);
        recursiveRender(scene);
        this.object3dElementRenderer.resetPipeline();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    private void recursiveRender(Element element) {
        if (element == null)
            return;

        if (element instanceof Object3dElement)
            this.object3dElementRenderer.render((Object3dElement) element);

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
        Objects.requireNonNull(graphics, "Graphics is null");

        graphics.fillQuadTexture(getBounds(), this.framebuffer);
    }

    @Override
    public void setBounds(Rectangle bounds) {
        setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        resizeFramebuffer(width, height);
    }

    private void resizeFramebuffer(int width, int height) {
        this.framebuffer.delete();
        this.framebuffer = new AllocatedFramebuffer(width, height);
    }

    public void dispose() {
        this.object3dElementRenderer.delete();
        this.gridLineRenderer.delete();
        this.framebuffer.delete();
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
        final Rectangle bounds = getBounds();
        final float dxf = (float) dx / (float) bounds.width;
        final float dyf = (float) dy / (float) bounds.height;

        this.camera.setPitch(this.camera.getPitch() + (dyf * this.fov * 2.0f));
        this.camera.setYaw(this.camera.getYaw() + (dxf * this.fov * 2.0f));
    }

    @Override
    public void onLooseFocus() {
        this.rotatingCamera = false;
    }
}
