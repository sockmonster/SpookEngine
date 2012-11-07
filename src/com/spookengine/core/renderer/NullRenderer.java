package com.spookengine.core.renderer;

import com.spookengine.core.camera.Cam;
import com.spookengine.scenegraph.Spatial;
import com.spookengine.scenegraph.Trfm;
import com.spookengine.scenegraph.appearance.App;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public class NullRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(NullRenderer.class.getName());
    private static Map<String, NullRenderer> instances = new HashMap<String, NullRenderer>();
    
    public static NullRenderer getInstance(String name) {
        NullRenderer renderer = instances.get(name);
        if(renderer == null) {
            instances.put(name, renderer = new NullRenderer());
            
            renderer.worldTransform = new Trfm();
            renderer.worldAppearance = new App();
        }
        
        return renderer;
    }
    
    private NullRenderer() {
        super();
    }
    
    @Override
    public void onSurfaceCreated() {
        // do nothing
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        // do nothing
    }

    @Override
    public void onDrawFrame(Spatial root, Cam cam) {
        camera = cam;

        // reset state
        worldTransform.toIdentity();
        boundsStack.clear();

        // update, bound and build render stacks
        spatialStackEmpty = true;
        visualStackEmpty = true;
        update(root);
        boundAndStack(root);
        
        canvasChanged = false;
    }
    
}
