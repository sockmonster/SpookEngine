package com.spookengine.scenegraph.camera;

import com.spookengine.math.Vec2;

/**
 *
 * @author Oliver Winks
 */
public class Viewport {

    private boolean isFixed;
    private Vec2 relativePos;
    private Vec2 relativeDim;
    private Vec2 pos;
    private Vec2 dim;

    public Viewport() {
        isFixed = false;
        relativePos = new Vec2(0,0);
        relativeDim = new Vec2(1,1);
        pos = new Vec2();
        dim = new Vec2();
    }

    public Viewport(boolean isFixed, Vec2 pos, Vec2 dim) {
        this.isFixed = false;
        relativePos = new Vec2(0,0);
        relativeDim = new Vec2(1,1);
        pos = new Vec2(0,0);
        dim = new Vec2(1,1);

        setTo(isFixed, pos, dim);
    }

    /**
     * Sets this Viewport to the given parameters. If 'isFixed' is false then
     * 'pos' and 'dim' must be between 0 and 1. Also, onCanvasChanged must be
     * called straight afterwards in order to recalculate the viewports position
     * and dimensions.
     *
     * @param isFixed Whether this viewport will recalculate it's position and
     * dimensions when the canvas dimensions change.
     * @param pos The position (% of canvas dimensions if 'isFixed' is false) of
     * this Viewport.
     * @param dim The dimensions (% of canvas dimensions if 'isFixed' is false)
     * of this Viewport.
     */
    public void setTo(boolean isFixed, Vec2 pos, Vec2 dim) {
        this.isFixed = isFixed;
        if(isFixed) {
            this.pos.setTo(pos);
            this.dim.setTo(dim);
        } else {
            this.relativePos.setTo(pos);
            this.relativeDim.setTo(dim);
        }

    }
    
    public boolean isFixed() {
        return isFixed;
    }

    public Vec2 getPos() {
        return pos;
    }

    public Vec2 getDimensions() {
        return dim;
    }

    public void onCanvasChanged(int width, int height) {
        if(!isFixed) {
            this.pos.setTo(relativePos.v[0]*width, relativePos.v[1]*height);
            this.dim.setTo(relativeDim.v[0]*width, relativeDim.v[1]*height);
        }
    }

}
