package com.spookengine.scenegraph;

import java.util.List;

/**
 * The Spatial class extends Bound by providing spatial information to
 * scenegraph objects.
 *
 * @author Oliver Winks
 */
public class Spatial extends Bound {
    
    protected Trfm localTransform;
    protected Trfm worldTransform;

    public Spatial(String name) {
        super(name);
        
        localTransform = new Trfm();
        worldTransform = new Trfm();
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
        return worldTransform;
    }
    
    public void setWorldTransform(Trfm worldTransform) {
        this.worldTransform.setTo(worldTransform);
    }
    
    /**
     * Calculates the world transform using this spatial's local transform and  
     * it's closest Spatial ancestor's world transform.
     */
    public void localToWorld() {
        // find closest Spatial ancestor
        Spatial ancestor = findAncestor(Spatial.class);
        
        /*
         * This is the root Spatial node so set it's world transform to it's 
         * local transform.
         */
        if(ancestor == null)
            worldTransform.setTo(localTransform);
        
        /*
         * This is a normal Spatial node so set it's world transform to it's 
         * parent's world transform and add the local transform.
         */
        else {
            // set world transform to ancestor world transform
            worldTransform.setTo(ancestor.getWorldTransform());

            // add local transform
            worldTransform.add(localTransform);
        }
    }
    
    /**
     * Calculates the world transform using this spatial's local transform and  
     * it's closest Spatial ancestor's world transform. This method recurses 
     * down the scenegraph from this spatial.
     */
    public void localToWorldTree() {
        localToWorld();
        
        // apply localToWorld for all Spatial children
        List<Spatial> spatialChildren = findChildren(Spatial.class);
        for(Spatial child : spatialChildren) {
            child.localToWorld();
        }
    }
    
    /**
     * Calculates the local transform using this spatial's world transform and  
     * it's closest Spatial ancestor's world transform.
     */
    public void worldToLocal() {
        Spatial ancestor = findAncestor(Spatial.class);
        
        /*
         * This is the root Spatial node so set it's local transform to it's 
         * world transform.
         */
        if(ancestor == null)
            localTransform.setTo(worldTransform);
        
        /*
         * This is a normal Spatial node so set it's local transform to the 
         * difference between it's parent's world transform and it's own world 
         * transform.
         */
        else {
            // set local transform to world transform
            localTransform.setTo(worldTransform);
            
            // subtract the parent world transform from the local transform
            localTransform.sub(ancestor.getWorldTransform());
        }
    }
    
    /**
     * Calculates the local transform using this spatial's world transform and  
     * it's closest Spatial ancestor's world transform. This method recurses 
     * down the scenegraph from this spatial.
     */
    public void worldToLocalTree() {
        worldToLocal();
        
        // apply localToWorld for all Spatial children
        List<Spatial> spatialChildren = findChildren(Spatial.class);
        for(Spatial child : spatialChildren) {
            child.worldToLocalTree();
        }
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
