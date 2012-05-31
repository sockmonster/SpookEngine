package com.spookengine.scenegraph;

/**
 * The Spatial class extends Bound by providing spatial information to
 * scenegraph objects.
 *
 * @author Oliver Winks
 */
public class Spatial extends Bound {
    
    protected boolean hasTransformed;
    protected Trfm localTransform;
    protected Trfm worldTransform;

    public Spatial(String name) {
        super(name);
        
        hasTransformed = true;
        localTransform = new Trfm();
        worldTransform = new Trfm();
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

    public Trfm getLocalTransform() {
        return localTransform;
    }

    /**
     * Returns the Transform that represents this SpatialNode2's rotation,
     * translation and scale relative to centre of the universe.
     *
     * @return This SpatialNode2's world transformation.
     */
    public Trfm getWorldTransform() {
        return worldTransform; // TODO: RETURN A COPY
    }

    public void updateLocalTransform() {
        localTransform.update();
        hasTransformed = true;
    }
    
    public void updateWorldTransform() {
        Node ancestor = parent;
        while(!(ancestor instanceof Spatial)) {
            if(ancestor == null) {
                worldTransform.add(localTransform);
                return;
            } else {
                ancestor = parent.parent;
            }
        }
        
        ((Spatial) ancestor).updateWorldTransform();
        worldTransform.setTo(((Spatial) ancestor).worldTransform);
        worldTransform.add(localTransform);
    }
    
    /**
     * ONLY THE RENDERER SHOULD CALL THIS!
     * 
     * Set's this nodes world transform. Calling this method causes the 
     * hasTransformed flag to be set to true.
     * 
     * @param worldTransform 
     */
    public void applyTransform(Trfm worldTransform) {
        this.worldTransform.setTo(worldTransform);

        // set flag
        hasTransformed = true;
    }

    @Override
    public Node clone() {
        Spatial clone = new Spatial(name);
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
