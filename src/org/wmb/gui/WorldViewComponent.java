package org.wmb.gui;

import org.wmb.rendering.Camera;
import org.wmb.rendering.floor.FloorRenderer;
import org.wmb.rendering.object.ObjectRenderer;
import org.wmb.world.WorldObject;

import java.util.ArrayList;

public class WorldViewComponent implements IGuiComponent {

    private final ArrayList<WorldObject> objectList;
    private final ObjectRenderer objectRenderer;
    private final FloorRenderer floorRenderer;
    private final Camera camera;
    private int x, y, width, height;

    public WorldViewComponent(int x, int y, int width, int height, ArrayList<WorldObject> objectList) {
        this.objectList = objectList;
        this.objectRenderer = new ObjectRenderer();
        this.floorRenderer = new FloorRenderer(1024.0f);
        this.camera = new Camera(0.0f, 4.0f, 4.0f, 45.0f, 0.0f, 90.0f, 0.1f, 1024.0f);
        setBounds(x, y, width, height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
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

    public void delete() {
        this.objectRenderer.delete();
        this.floorRenderer.delete();
    }

    public Camera getCamera() {
        return this.camera;
    }
}
