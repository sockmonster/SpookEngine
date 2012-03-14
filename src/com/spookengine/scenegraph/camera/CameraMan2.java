package com.spookengine.scenegraph.camera;

import com.spookengine.scenegraph.Node;
import com.spookengine.scenegraph.Spatial;
import com.spookengine.scenegraph.Trfm2;
import com.spookengine.scenegraph.collision.BoundingVolume;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public class CameraMan2 extends Spatial<Trfm2> {

    private Logger logger = Logger.getLogger(CameraMan2.class.getName());
    private Cam2 cam;

    public CameraMan2(String name, Cam2 cam) {
        super(true, name);

        this.cam = cam;
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

    public void setCamera(Cam2 cam) {
        this.cam = cam;
    }

    public Cam2 getCamera() {
        return cam;
    }

    @Override
    public void updateBounds(List<BoundingVolume> childBounds, int from, int to) {
        // update model bounds
        if(bounds != null)
            bounds.applyTransform(worldTransform);
    }

    @Override
    public void applyTransform(Trfm2 worldTransform) {
        super.applyTransform(worldTransform);

        // set position, and orientation
        cam.pos.toZeros();
        this.worldTransform.apply(cam.pos);

        // calculate orientation
        cam.rot = worldTransform.getRotation();

        // recalculate frustum
        cam.update();
    }

    @Override
    public Node clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Node cloneTree(Node parent) {
        logger.log(Level.WARNING, "CameraMan2 is a leaf node therefore there " +
                "is no sub-tree to clone, using clone() instead.");

        return clone();
    }

}
