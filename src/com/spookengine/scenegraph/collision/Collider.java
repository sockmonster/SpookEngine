package com.spookengine.scenegraph.collision;

import com.spookengine.scenegraph.Node;
import com.spookengine.scenegraph.Spatial;
import com.spookengine.scenegraph.collision.BoundingVolume;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public class Collider extends Spatial {
    private static final Logger logger = Logger.getLogger(Collider.class.getName());

    protected Collider(String name) {
        super(name);
    }

    /**
     * Updates this Collider's bounds (if it has any). Collider's are leaf
     * nodes, therefore 'childBounds' will be empty!
     *
     * @param childBounds A list holding all of the bounds of this ModelLeaf's
     * children (this list will always be empty).
     */
    @Override
    public void updateBounds(List<BoundingVolume> childBounds, int from, int to) {
        // update model bounds
        if(bounds != null)
            bounds.applyTransform(worldTransform);
    }

    @Override
    public void attachChild(Node child) {
        logger.log(Level.WARNING, "Node's cannot be attach to a leaf!");
    }

    @Override
    public void detachChild(Node child) {
        logger.log(Level.WARNING, "Leaf Node's do not have children!");
    }

    @Override
    public Node detachChild(int i) {
        logger.log(Level.WARNING, "Leaf Node's do not have children!");
        
        return null;
    }

    @Override
    public Node clone() {
        // TODO: need to implement!
        Collider clone = new Collider(name);
        clone.localTransform.setTo(localTransform);

        if(bounds != null)
            clone.setBounds(bounds.clone());

        return clone;
    }

    @Override
    public Node cloneTree(Node parent) {
        logger.log(Level.WARNING, "BoundLeaf is a leaf node therefore there " +
                "is no sub-tree to clone, using clone() instead.");

        return clone();
    }

}
