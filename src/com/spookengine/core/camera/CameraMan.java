package com.spookengine.core.camera;

import com.spookengine.maths.Vec3;
import com.spookengine.scenegraph.Node;
import com.spookengine.scenegraph.Spatial;
import com.spookengine.scenegraph.Trfm;
import com.spookengine.scenegraph.collision.BoundingVolume;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public class CameraMan extends Spatial {
    private static final Logger logger = Logger.getLogger(CameraMan.class.getName());
    
    public boolean fixLookAt;
    protected Vec3 worldLookAt;
    protected Vec3 worldDir;
    protected Vec3 worldUp;
    private Cam cam;

    public CameraMan(String name, Cam camera) {
        super(name);
        
        this.cam = camera;
        
        fixLookAt = false;
        worldLookAt = new Vec3();
        
        worldDir = new Vec3(0,1,0);
        worldUp = new Vec3(0,0,1);
    }

    @Override
    public void attachChild(Node child) {
        logger.log(Level.WARNING, "CameraNode is a leaf, you cannot attach " +
                "children to a leaf node!");
    }

    @Override
    public void detachChild(Node child) {
        logger.log(Level.WARNING, "CameraNode is a leaf, you cannot detach " +
                "children from a leaf node!");
    }

    @Override
    public Node detachChild(int i) {
        logger.log(Level.WARNING, "CameraNode is a leaf, you cannot detach " +
                "children from a leaf node!");

        return null;
    }
    
    @Override
    public void setWorldTransform(Trfm worldTransform) {
        super.setWorldTransform(worldTransform);

        // calculate camera pos
        cam.pos.toZeros();
        this.worldTransform.apply(cam.pos);
        
        if(!fixLookAt) {
            worldUp.setTo(0,0,1);
            worldLookAt.setTo(0,1,0);
            
            // calculate up vector
            this.worldTransform.apply(worldUp);
            worldUp.sub(cam.pos).norm(); // testing

            // calculate lookat vector
            this.worldTransform.apply(worldLookAt);
        } else {
            // TODO: CALCULATE UP VECTOR!
        }

        // set look at direction and up direction
        cam.up.setTo(worldUp);
        cam.lookAt.setTo(worldLookAt);

        // calculate direction
        worldDir.setTo(worldLookAt).sub(cam.pos);

        // recalculate frustum
        cam.update();
    }

    public Vec3 getWorldLookAt() {
        return worldLookAt;
    }
    
    public void setWorldLookAt(Vec3 pos) {
        this.worldLookAt = pos;
    }

    /**
     * Returns the direction that this SpatialNode is facing.
     *
     * @return The direction that this SpatialNode is facing.
     */
    public Vec3 getWorldDir() {
        return worldDir;
    }

    /**
     * Returns the direction that this SpatialNode is facing.
     *
     * @return The direction that this SpatialNode is facing.
     */
    public Vec3 getWorldUp() {
        return worldUp;
    }

    public void setCamera(Cam camera) {
        this.cam = camera;
    }

    public Cam getCamera() {
        return cam;
    }

    @Override
    public void updateBounds(List<BoundingVolume> childBounds, int from, int to) {
        // update model bounds
        if(bounds != null)
            bounds.applyTransform(worldTransform);
    }

    @Override
    public Node clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Node cloneTree(Node parent) {
        logger.log(Level.WARNING, "CameraMan3 is a leaf node therefore there " +
                "is no sub-tree to clone, using clone() instead.");

        return clone();
    }

}
