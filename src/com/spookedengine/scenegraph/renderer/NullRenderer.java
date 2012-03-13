package com.spookedengine.scenegraph.renderer;

import com.spookedengine.scenegraph.App3;
import com.spookedengine.scenegraph.Spatial;
import com.spookedengine.scenegraph.Trfm3;
import com.spookedengine.scenegraph.camera.Cam;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public class NullRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(NullRenderer.class.getName());
    private static NullRenderer instance;
    
    public static NullRenderer getInstance() {
        if(instance == null) {
            instance = new NullRenderer();
            
            instance.worldTransform = new Trfm3();
            instance.worldAppearance = new App3();
        }
        
        return instance;
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
