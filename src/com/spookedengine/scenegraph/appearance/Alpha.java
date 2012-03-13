package com.spookedengine.scenegraph.appearance;

/**
 *
 * @author Oliver Winks
 */
public class Alpha {
    private float alpha;

    public Alpha() {
        toDefault();
    }

    public Alpha(float alpha) {
        setValue(alpha);
    }

    public void setTo(Alpha att) {
        this.alpha = att.alpha;
    }

    public void setValue(float alpha) {
        if(alpha >= 0)
            this.alpha = alpha;
    }

    public float getValue() {
        return alpha;
    }
    
    public void toDefault() {
        alpha = 1f;
    }

}
