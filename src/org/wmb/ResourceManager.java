package org.wmb;

import org.wmb.loading.ResourceLoader;
import org.wmb.rendering.AllocatedMeshData;
import org.wmb.rendering.AllocatedTexture;

import java.io.IOException;
import java.util.HashMap;

public final class ResourceManager {

    private static final String TAG = "ResourceManager";

    private final AllocatedMeshData debugModel;
    private final HashMap<String, AllocatedMeshData> loadedModels;

    private final AllocatedTexture debugTexture;
    private final HashMap<String, AllocatedTexture> loadedTextures;

    ResourceManager() throws IOException {
        try {
            final String path = "/org/wmb/editor/element/Object3dElement/model_default.obj";
            this.debugModel = AllocatedMeshData.fromResource(path);
        } catch (IOException exception) {
            Log.error(TAG, "Debug model failed to load");
            throw exception;
        }

        this.loadedModels = new HashMap<>();

        try {
            final String path = "/org/wmb/editor/element/Object3dElement/texture_default.png";
            this.debugTexture = new AllocatedTexture(ResourceLoader.loadResourceImage(path));
        } catch (IOException exception) {
            this.debugModel.delete();
            Log.error(TAG, "Debug texture failed to load");
            throw exception;
        }

        this.loadedTextures = new HashMap<>();
    }

    public void delete() {
        this.debugModel.delete();
        for (AllocatedMeshData model : this.loadedModels.values())
            model.delete();
        this.loadedModels.clear();

        this.debugTexture.delete();
        for (AllocatedTexture texture : this.loadedTextures.values())
            texture.delete();
        this.loadedTextures.clear();
    }

    public AllocatedMeshData getModel(String path) {
        final AllocatedMeshData model = this.loadedModels.get(path);

        if (model != null)
            return model;

        return this.debugModel;
    }

    public void loadModel(String path) throws IOException {
        if (loadedModels.containsKey(path)) {
            loadedModels.get(path).delete();
            loadedModels.remove(path);
        }

        loadedModels.put(path, AllocatedMeshData.fromFile(path));
    }

    public boolean hasModelLoaded(String path) {
        return this.loadedModels.containsKey(path);
    }

    public AllocatedTexture getTexture(String path) {
        final AllocatedTexture texture = this.loadedTextures.get(path);

        if (texture != null)
            return texture;

        return this.debugTexture;
    }

    public void loadTexture(String path) throws IOException {
        if (loadedTextures.containsKey(path)) {
            loadedTextures.get(path).delete();
            loadedTextures.remove(path);
        }

        loadedTextures.put(path, new AllocatedTexture(ResourceLoader.loadFileImage(path)));
    }

    public boolean hasTextureLoaded(String path) {
        return this.loadedTextures.containsKey(path);
    }
}
