package com.spookengine.scenegraph;

import com.spookengine.scenegraph.collision.BoundingVolume;
import java.util.List;

/**
 * A group node that simply holds a bounding volume which wraps around all
 * child nodes.
 *
 * BoundNodes are ordered such that all children that are leaf
 * nodes are on the 'left' of non-leaf nodes. This makes collision detection
 * easier!
 *
 * @author Oliver Winks
 */
public class Bound extends Node {

    protected BoundingVolume bounds;
    public int boundGroup;

    public Bound() {
        super();
    }

    public Bound(String name) {
        super(name);
    }

    private void sort() {
        int lastLeaf = -1;
        for(int i=0; i<children.size(); i++) {
            if(children.get(i).isLeaf() && i > lastLeaf + 1) {
                children.add(++lastLeaf, children.remove(i));
                i--;
            }
        }
    }

    @Override
    public void attachChild(Node child) {
        super.attachChild(child);

        // sort this node and it's parent node (if it's a Bound)
        sort();
        if(parent != null && parent instanceof Bound)
            ((Bound) parent).sort();
    }

    /**
     * Sets this Bound's bounding volume.
     *
     * @param bounds This Bound's bounding volume.
     */
    public void setBounds(BoundingVolume bounds) {
        this.bounds = bounds;
    }

    /**
     * Returns this Bound's bounding volume.
     *
     * @return This Bound's bounding volume.
     */
    public BoundingVolume getBounds() {
        return bounds;
    }

    /**
     * Updates this Bound's bounds (if it has any) by wraping it's child
     * Nodes in it's BoundingVolume.
     *
     * @param childBounds A list holding all of the bounds of this ModelLeaf's
     * children (this list will always be empty).
     */
    public void updateBounds(List<BoundingVolume> childBounds, int from, int to) {
        if(bounds != null) {
            /*
             resize bounding volume according to the bounds of this
             Bound's children
             */
            bounds.wrap(childBounds, from, to);
        }
    }

    @Override
    public Node clone() {
        Bound clone = new Bound();
        
        if(bounds != null)
            clone.setBounds(bounds.clone());

        return clone;
    }

    @Override
    public Node cloneTree(Node parent) {
        Bound clone = (Bound) clone();

        // reparent clone
        if(parent != null)
            parent.attachChild(clone);

        // recursively clone children
        for(int i=0; i<children.size(); i++)
            children.get(i).cloneTree(clone);

        return clone;
    }

}
