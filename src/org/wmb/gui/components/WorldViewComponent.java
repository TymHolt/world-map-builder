package org.wmb.gui.components;

import org.wmb.rendering.Camera;
import org.wmb.rendering.floor.FloorRenderer;
import org.wmb.rendering.object.ObjectRenderer;
import org.wmb.world.WorldObject;

import java.io.IOException;
import java.util.ArrayList;

public class WorldViewComponent {

    private final ArrayList<WorldObject> objectList;
    private final ObjectRenderer objectRenderer = new ObjectRenderer();
    private final FloorRenderer floorRenderer = new FloorRenderer(1024.0f);
    private final Camera camera = new Camera(0.0f, 4.0f, 4.0f, 45.0f, 0.0f, 90.0f, 0.1f, 1024.0f);
    private int x, y, width, height;

    public WorldViewComponent(int x, int y, int width, int height, ArrayList<WorldObject> objectList)
        throws IOException {
        this.objectList = objectList;
        setBounds(x, y, width, height);
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render() {
        float aspect = (float) width / (float) height;

        this.floorRenderer.begin(x, y, width, height);
        this.floorRenderer.renderGuideLines(camera, aspect, 0.025f, 0.5f, 0.5f, 0.5f);
        this.floorRenderer.end();

        this.objectRenderer.begin(x, y, width, height);
        this.objectRenderer.uniformCamera(this.camera, aspect);
        for (WorldObject object : this.objectList) {
            this.objectRenderer.render(object);
        }
        this.objectRenderer.end();
    }

    public Camera getCamera() {
        return this.camera;
    }
}
