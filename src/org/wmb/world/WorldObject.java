package org.wmb.world;

import org.wmb.debug.Assert;
import org.wmb.rendering.AllocatedTexture;
import org.wmb.rendering.AllocatedVertexData;
import org.wmb.rendering.Renderer;

public final class WorldObject {

    private final AllocatedVertexData model;
    private final AllocatedTexture material;
    private final WorldPosition position;

    public WorldObject(AllocatedVertexData model, AllocatedTexture material,
        WorldPosition position) {

        Assert.argNotNull(model, "model");
        Assert.argNotNull(material, "material");
        Assert.argNotNull(position, "position");

        this.model = model;
        this.material = material;
        this.position = position;
    }

    public void render(Renderer renderer) {
        renderer.render(model, material, position.getAsMatrix());
    }
}
