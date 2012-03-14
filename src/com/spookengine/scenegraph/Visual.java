package com.spookengine.scenegraph;

/**
 *
 * @author Oliver Winks
 */
public class Visual<T extends Trfm, A extends App> extends Spatial<T> {

    protected boolean hasVisualStateChanged;
    protected A worldAppearance;
    protected A localAppearance;
    public int layer;

    public static Visual<Trfm2, App2> new2D(String name) {
        return new Visual<Trfm2, App2>(true, name);
    }

    public static Visual<Trfm3, App3> new3D(String name) {
        return new Visual<Trfm3, App3>(false, name);
    }

    public Visual(boolean is2D, String name) {
        super(is2D, name);

        if(is2D) {
            hasVisualStateChanged = true;
            localAppearance = (A) new App2();
            worldAppearance = (A) new App2();
        } else {
            hasVisualStateChanged = true;
            localAppearance = (A) new App3();
            worldAppearance = (A) new App3();
        }
    }
    
    public boolean hasVisualStateChanged() {
        return hasVisualStateChanged;
    }
    
    /**
     * ONLY THE RENDERER SHOULD CALL THIS!
     * 
     * @param hasTransformed
     */
    public void hasVisualStateChanged(boolean hasVisualStateChanged) {
        this.hasVisualStateChanged = hasVisualStateChanged;
    }

    /**
     * Returns this VisualNode's local Appearance. It is assumed that
     * this method is used to modify the local appearance, therefore calling
     * this method will cause the renderer to recalculate this visual node's
     * (and it's subtree's) appearance.
     *
     * @return This VisualNode's local Appearance.
     */
    public A getLocalAppearance() {
        hasVisualStateChanged = true;
        return localAppearance;
    }

    /**
     * Returns the VisualNode's world Appearance, this is the Appearance
     * relative to the root of the scenegraph.
     *
     * <i>NOTE: It is dangerous to modify the world appearance! The world
     * appearance contains pointers to the local appearance of ancestor nodes,
     * modifying the world appearance will also modify the local appearance
     * of ancestor nodes!</i>
     *
     * @return This VisualNode's world Appearance.
     */
    public A getWorldAppearance() {
        return worldAppearance;
    }

    /**
     * Sets this VisualNode's world Appearance to the given world Appearance.
     * This method is called by the Renderer, the passed world Appearance is
     * the composition of all Appearance's from the root Node.
     *
     * <i>NOTE: This method is automatically called by the Renderer, there is
     * no need to call it yourself</i>
     *
     * @param worldAppearance The Appearance to set this VisualNode's
     * Appearance to.
     */
    public void applyAppearance(A worldAppearance) {
        this.worldAppearance.setTo(worldAppearance);
        this.hasVisualStateChanged = true;
    }

    @Override
    public Node clone() {
        Visual clone = new Visual(is2D, name);
        clone.localTransform.setTo(localTransform);
        clone.localAppearance.setTo(localAppearance);

        if(bounds != null)
            clone.setBounds(bounds.clone());

        return clone;
    }

    @Override
    public Node cloneTree(Node parent) {
        Visual clone = (Visual) clone();

        // reparent clone
        if(parent != null)
            parent.attachChild(clone);

        // recursively clone children
        for(int i=0; i<children.size(); i++)
            children.get(i).cloneTree(clone);

        return clone;
    }

}
