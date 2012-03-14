package com.spookengine.scenegraph.lights;

import com.spookengine.math.Vec3;
import com.spookengine.scenegraph.Node;
import com.spookengine.scenegraph.Spatial;
import com.spookengine.scenegraph.Trfm3;
import com.spookengine.scenegraph.collision.BoundingVolume;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A leaf node that allows a positional light to be placed into a scenegraph.
 * 
 * @author Oliver Winks
 */
public class LightMan extends Spatial<Trfm3> {
    
    private Logger logger = Logger.getLogger(LightMan.class.getName());

    private Vec3 worldDir;
    private LightBulb light;

    public LightMan(String name) {
        super(false, name);

        worldDir = new Vec3(0,0,1);
    }

    public LightMan(String name, LightBulb light) {
        super(false, name);
        
        worldDir = new Vec3(0,0,1);
        this.light = light;
    }

    @Override
    public void attachChild(Node child) {
        logger.log(Level.WARNING, "LightNode is a leaf, you cannot attach " +
                "children to a leaf node!");
    }

    @Override
    public void detachChild(Node child) {
        logger.log(Level.WARNING, "LightNode is a leaf, you cannot detach " +
                "children from a leaf node!");
    }

    @Override
    public Node detachChild(int i) {
        logger.log(Level.WARNING, "LightNode is a leaf, you cannot detach " +
                "children from a leaf node!");

        return null;
    }

    /**
     * Returns the direction that this SpatialNode is facing.
     *
     * @return The direction that this SpatialNode is facing.
     */
    public Vec3 getWorldDir() {
        return worldDir;
    }

    public void setLight(LightBulb light) {
        this.light = light;
    }

    public LightBulb getLight() {
        return light;
    }

    @Override
    public void updateBounds(List<BoundingVolume> childBounds, int from, int to) {
        // update model bounds
        if(bounds != null)
            bounds.applyTransform(worldTransform);
    }

    @Override
    public void applyTransform(Trfm3 worldTransform) {
        super.applyTransform(worldTransform);

        // set light position
        light.pos.toZeros();
        worldTransform.getPos(light.pos);

        if(light instanceof SpotLight) {
            // update direction
            worldDir.setTo(0,0,1);
            this.worldTransform.apply(worldDir);

            // set light direction
            ((SpotLight) light).dir.setTo(light.pos);
            ((SpotLight) light).dir.sub(worldDir);
            ((SpotLight) light).dir.norm();
        }

    }

    @Override
    public Node clone() {
        // create a clone with an instance of this geometry
        LightMan clone = new LightMan(name, (LightBulb) light.clone());

        // clone local transform
        clone.localTransform.setTo(localTransform);

        return clone;
    }

    @Override
    public Node cloneTree(Node parent) {
        logger.log(Level.WARNING, "LightLeaf is a leaf node therefore there is no " +
                "sub-tree to clone, using clone() instead.");

        return clone();
    }

}
