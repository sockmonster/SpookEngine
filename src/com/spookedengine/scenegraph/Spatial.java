package com.spookedengine.scenegraph;

/**
 * The SpatialNode2 class extends Node by providing spatial information to
 * scenegraph objects.
 *
 * @author Oliver Winks
 */
public class Spatial<T extends Trfm> extends Bound {

    protected boolean is2D;
    protected boolean hasTransformed;
    protected T localTransform;
    protected T worldTransform;

    public static Spatial<Trfm2> new2D(String name) {
        return new Spatial<Trfm2>(true, name);
    }

    public static Spatial<Trfm3> new3D(String name) {
        return new Spatial<Trfm3>(false, name);
    }

    public Spatial(boolean is2D, String name) {
        super(name);
        this.is2D = is2D;

        if(is2D) {
            hasTransformed = true;
            localTransform = (T) new Trfm2();
            worldTransform = (T) new Trfm2();
        } else {
            hasTransformed = true;
            localTransform = (T) new Trfm3();
            worldTransform = (T) new Trfm3();
        }
    }
    
    public boolean hasTransformed() {
        return hasTransformed;
    }
    
    /**
     * ONLY THE RENDERER SHOULD CALL THIS!
     * 
     * @param hasTransformed
     */
    public void hasTransformed(boolean hasTransformed) {
        this.hasTransformed = hasTransformed;
    }

    public T getLocalTransform() {
        return localTransform;
    }

    /**
     * Returns the Transform that represents this SpatialNode2's rotation,
     * translation and scale relative to centre of the universe.
     *
     * @return This SpatialNode2's world transformation.
     */
    public T getWorldTransform() {
        return worldTransform; // TODO: RETURN A COPY
    }

    public void updateLocalTransform() {
        localTransform.update();
        hasTransformed = true;
    }
    
    /**
     * ONLY THE RENDERER SHOULD CALL THIS!
     * 
     * Set's this nodes world transform. Calling this method causes the 
     * hasTransformed flag to be set to true.
     * 
     * @param worldTransform 
     */
    public void applyTransform(T worldTransform) {
        this.worldTransform.setTo(worldTransform);

        // set flag
        hasTransformed = true;
    }

    @Override
    public Node clone() {
        Spatial clone = new Spatial(is2D, name);
        clone.localTransform.setTo(localTransform);

        if(bounds != null)
            clone.setBounds(bounds.clone());

        return clone;
    }

    @Override
    public Node cloneTree(Node parent) {
        Spatial clone = (Spatial) clone();

        // reparent clone
        if(parent != null)
            parent.attachChild(clone);

        // recursively clone children
        for(int i=0; i<children.size(); i++)
            children.get(i).cloneTree(clone);

        return clone;
    }

}
