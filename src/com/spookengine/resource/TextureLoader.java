package com.spookengine.resource;

import com.spookengine.scenegraph.appearance.Texture;
import java.io.IOException;

/**
 *
 * @author Oliver Winks
 */
public abstract class TextureLoader {

    public abstract Texture loadTexture(String... filename) throws IOException;

}
