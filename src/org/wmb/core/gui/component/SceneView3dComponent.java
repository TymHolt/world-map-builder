package org.wmb.core.gui.component;

import org.lwjgl.opengl.GL30;
import org.wmb.core.gui.GuiGraphics;
import org.wmb.editor.Scene3d;
import org.wmb.editor.element.Element;
import org.wmb.editor.element.Object3dElement.Object3dElement;
import org.wmb.editor.element.Object3dElement.Object3dElementRenderer;
import org.wmb.common.gui.component.Component;
import org.wmb.rendering.AllocatedFramebuffer;
import org.wmb.rendering.Camera;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class SceneView3dComponent extends Component {

    private AllocatedFramebuffer framebuffer;
    private final Object3dElementRenderer object3dElementRenderer;
    private final Camera camera;

    public SceneView3dComponent() throws IOException {
        this.framebuffer = new AllocatedFramebuffer(2, 2);
        this.object3dElementRenderer = new Object3dElementRenderer();
        this.camera = new Camera(0.0f, 0.0f, 3.0f, 0.0f, 0.0f, 70.0f, 0.1f, 16.0f);
    }

    public void renderScene(Scene3d scene) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.framebuffer.getFboId());

        final Rectangle bounds = getBounds();
        GL30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glEnable(GL30.GL_DEPTH_TEST);

        this.object3dElementRenderer.preparePipeline(0, 0, bounds.width, bounds.height);
        this.object3dElementRenderer.uniformCamera(this.camera,
            (float) (bounds.getWidth() / bounds.getHeight()));
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
    public void draw(GuiGraphics graphics) {
        Objects.requireNonNull(graphics, "Graphics is null");

        graphics.fillQuadTexture(getBounds(), this.framebuffer);
    }

    @Override
    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        resizeFramebuffer(bounds.width, bounds.height);
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

    @Override
    public Dimension getRequestedSize() {
        return new Dimension(1, 1);
    }

    public void dispose() {
        this.object3dElementRenderer.delete();
        this.framebuffer.delete();
    }
}
