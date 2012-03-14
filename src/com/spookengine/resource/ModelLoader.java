package com.spookengine.resource;

import com.spookengine.resource.ResourceManager.ResourceScope;
import com.spookengine.scenegraph.Visual;

/**
 *
 * @author Oliver Winks
 */
public abstract class ModelLoader {

    public abstract Visual loadModel(ResourceScope scope, String filename) throws Exception;

}
