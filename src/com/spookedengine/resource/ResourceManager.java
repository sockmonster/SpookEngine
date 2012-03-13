package com.spookedengine.resource;

import com.spookedengine.scenegraph.Visual;
import com.spookedengine.scenegraph.appearance.Texture;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Oliver Winks
 */
public class ResourceManager {
    private static ResourceManager instance;
    public static enum ResourceScope {GAME, SCENE}

    // resource loaders
    private TextureLoader textureLoader;
    private ModelLoader modelLoader;

    // resource stores
    private HashMap<String, Texture> gameTextures;
    private HashMap<String, Texture> sceneTextures;
    private HashMap<String, Visual> gameModels;
    private HashMap<String, Visual> sceneModels;

    private ResourceManager() {
        gameTextures = new HashMap<String, Texture>();
        sceneTextures = new HashMap<String, Texture>();
        gameModels = new HashMap<String, Visual>();
        sceneModels = new HashMap<String, Visual>();
    }

    public static ResourceManager getInstance() {
        if(instance == null)
            instance = new ResourceManager();

        return instance;
    }

    public void setTextureLoader(TextureLoader loader) {
        textureLoader = loader;
    }

    public void setModelLoader(ModelLoader loader) {
        modelLoader = loader;
    }

    /**
     * 
     * @param scope
     * @param filename
     * @return
     * @throws IOException
     */
    public Texture loadTexture(ResourceScope scope, String... filename) throws IOException {
        Texture tex = null;
        switch(scope) {
            case GAME:
                if(gameTextures.containsKey(filename[0])) {
                    tex = gameTextures.get(filename[0]);
                } else {
                    tex = textureLoader.loadTexture(filename);
                    gameTextures.put(filename[0], tex);
                }
                break;

            case SCENE:
                if(sceneTextures.containsKey(filename[0])) {
                    tex = sceneTextures.get(filename[0]);
                } else {
                    tex = textureLoader.loadTexture(filename);
                    sceneTextures.put(filename[0], tex);
                }
                break;
        }

        return tex;
    }

    /**
     * Returns all textures from every scope.
     *
     * @return All textures from every scope.
     */
    public Collection<Texture> getTextures() {
        Collection<Texture> textures = new ArrayList<Texture>();

        textures.addAll(gameTextures.values());
        textures.addAll(sceneTextures.values());

        return textures;
    }

    /**
     * Return all textures from the given scope.
     *
     * @param scope The scope of the textures to return.
     * @return all textures from the given scope.
     */
    public Collection<Texture> getTextures(ResourceScope scope) {
        switch(scope) {
            case GAME:
                return gameTextures.values();

            case SCENE:
                return sceneTextures.values();
        }

        return null;
    }

    /**
     * Return the texture with the given name from the given scope.
     *
     * @param scope The scope of the texture.
     * @param filename The textures filename.
     * @return The texture with the given name from the given scope.
     */
    public Texture getTexture(ResourceScope scope, String filename) {
        switch(scope) {
            case GAME:
                return gameTextures.get(filename);

            case SCENE:
                return sceneTextures.get(filename);
        }

        return null;
    }

    /**
     * Loads the obj model from the given .obj and .mtl files.
     *
     * @param scope
     * @param objFile
     * @param mtlFile
     * @param asLeaf If false the model is parented to a VisualNode, if true
     * the raw ModelLeaf is returned.
     * @return
     * @throws Exception
     */
    public Visual loadModel(ResourceScope scope, String file) throws Exception {
        Visual model = null;
        switch(scope) {
            case GAME:
                if(gameModels.containsKey(file)) {
                    model = (Visual) gameModels.get(file).cloneTree(null);
                } else {
                    model = (Visual) modelLoader.loadModel(scope, file);
                    gameModels.put(file, (Visual) model.cloneTree(null));
                }
                break;

            case SCENE:
                if(sceneModels.containsKey(file)) {
                    model = (Visual) sceneModels.get(file).cloneTree(null);
                } else {
                    model = (Visual) modelLoader.loadModel(scope, file);
                    sceneModels.put(file, (Visual) model.cloneTree(null));
                }
                break;
        }

        return model;
    }

    /**
     * Returns all models from every scope.
     *
     * @return all models from every scope.
     */
    public Collection<Visual> getModels() {
        Collection<Visual> list = gameModels.values();
        list.addAll(sceneModels.values());

        return list;
    }

    /**
     * Return all models from the given scope.
     *
     * @param scope The scope of the models to return.
     * @return All models from the given scope.
     */
    public Collection<Visual> getModels(ResourceScope scope) {
        switch(scope) {
            case GAME:
                return gameModels.values();

            case SCENE:
                return sceneModels.values();
        }

        return null;
    }

    /**
     * Return the model with the given name from the given scope.
     *
     * @param scope The scope of the model to return.
     * @param filename The name of the model.
     * @return The model with the given name from the given scope.
     */
    public Visual getModel(ResourceScope scope, String filename) {
        switch(scope) {
            case GAME:
                return gameModels.get(filename);

            case SCENE:
                return sceneModels.get(filename);
        }

        return null;
    }

}
