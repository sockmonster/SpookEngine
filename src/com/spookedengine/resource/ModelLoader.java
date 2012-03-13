package com.spookedengine.resource;

import com.spookedengine.resource.ResourceManager.ResourceScope;
import com.spookedengine.scenegraph.Visual;

/**
 *
 * @author Oliver Winks
 */
public abstract class ModelLoader {

    public abstract Visual loadModel(ResourceScope scope, String filename) throws Exception;

}
