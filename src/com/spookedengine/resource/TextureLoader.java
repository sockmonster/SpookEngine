package com.spookedengine.resource;

import com.spookedengine.scenegraph.appearance.Texture;
import java.io.IOException;

/**
 *
 * @author Oliver Winks
 */
public abstract class TextureLoader {

    public abstract Texture loadTexture(String... filename) throws IOException;

}
