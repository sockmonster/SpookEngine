package com.spookengine.scenegraph;

import com.spookengine.scenegraph.collision.BoundingVolume;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The GeomLeaf2 class extends VisualN, it is a leaf node and therefore
 * cannot have other Node's attached to it. It holds a trimesh which can be
 * rendered by the Renderer.
 * 
 * @author Oliver Winks
 */
public class Geom<T extends Trfm, A extends App> extends Visual<T, A> {

    private static final Logger logger = Logger.getLogger(Geom.class.getName());
    protected Trimesh trimesh;

    public static Geom<Trfm2, App2> new2D(String name, Trimesh trimesh) {
        return new Geom<Trfm2, App2>(true, name, trimesh);
    }

    public static Geom<Trfm3, App3> new3D(String name, Trimesh trimesh) {
        return new Geom<Trfm3, App3>(false, name, trimesh);
    }

    public Geom(boolean is2D, String name, Trimesh trimesh) {
        super(is2D, name);

        this.trimesh = trimesh;
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

    /**
     * Sets this GeomLeaf2's bounding volume.
     *
     * <i>
     * Do not set the model bounds after the model has been scaled. The bounding
     * volume's extent is calculated upon calling this method and it assumes
     * that the model has a scale of 1!
     * </i>
     *
     * @param bounds This GeomLeaf2's bounding volume.
     */
    @Override
    public void setBounds(BoundingVolume bounds) {
        this.bounds = bounds;
        this.bounds.wrap(trimesh.getVertices());
    }

    public void setBounds(BoundingVolume bounds, boolean wrapGeom) {
        this.bounds = bounds;
        if(wrapGeom)
            this.bounds.wrap(trimesh.getVertices());
    }

    /**
     * Returns this GeomLeaf2's bounding volume.
     *
     * @return This GeomLeaf2's bounding volume.
     */
    @Override
    public BoundingVolume getBounds() {
        return bounds;
    }

    /**
     * Updates this GeomLeaf2's bounds (if it has any). GeomLeaf2's are leaf nodes,
     * therefore 'childBounds' will be empty!
     *
     * @param childBounds A list holding all of the bounds of this GeomLeaf2's
     * children (this list will always be empty).
     */
    @Override
    public void updateBounds(List<BoundingVolume> childBounds, int from, int to) {
        // update model bounds
        if(bounds != null && hasTransformed)
            bounds.applyTransform(worldTransform);
    }

    /**
     * Returns the Trimesh associated with this GeomLeaf2.
     *
     * @return The Trimesh associated with this GeomLeaf2.
     */
    public Trimesh getTrimesh() {
        return trimesh;
    }

    @Override
    public Node clone() {
        // create a clone with an instance of this trimesh
        Geom clone = new Geom(is2D, name, trimesh);
        clone.localTransform.setTo(localTransform);
        clone.localAppearance.setTo(localAppearance);

        if(bounds != null)
            clone.setBounds(bounds.clone());

        return clone;
    }

    @Override
    public Node cloneTree(Node parent) {
        logger.log(Level.WARNING, "Geom is a leaf node therefore there is no " +
                "sub-tree to clone, using clone() instead.");

        return clone();
    }

}
