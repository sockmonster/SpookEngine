package com.spookedengine.scenegraph.camera;

import com.spookedengine.math.Mat4;
import com.spookedengine.math.Vec;
import com.spookedengine.scenegraph.collision.BoundingVolume;

/**
 *
 * @author Oliver Winks
 */
public abstract class Cam<V extends Vec> {
    /** Used by renderer, DO NOT MODIFY! */
    public boolean viewportChanged = true;

    // viewport
    protected Viewport viewport;

    // camera
    protected V pos;
    protected Mat4 modelView;
    protected Mat4 modelViewInv;

    public Cam() {
        viewport = new Viewport();
        viewportChanged = true;

        modelView = new Mat4();
        modelViewInv = new Mat4();
    }

    public Cam(Viewport viewport) {
        this.viewport = viewport;
        viewportChanged = true;

        modelView = new Mat4();
        modelViewInv = new Mat4();
    }

    public void setViewport(Viewport viewport) {
        viewportChanged = true;
        this.viewport = viewport;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setPos(V pos) {
        this.pos.setTo(pos);
    }

    public void setPos(float[] pos) {
        this.pos.setTo(pos);
    }

    public V getPos() {
        return pos;
    }

    public Mat4 getModelView() {
        return modelView;
    }

    public abstract boolean isVisible(V pos);

    public abstract boolean isVisible(BoundingVolume<V> bounds);

    /**
     * This must be called every time the camera is transformed, it's frustum
     * is changed, or the viewport is modified.
     */
    public abstract void update();

    /**
     * Called when the canvas that this camera's viewport is attached to
     * changes size.
     *
     * @param width The new width of the canvas.
     * @param height Te new height of the canvas.
     */
    public void onCanvasChanged(int width, int height) {
        viewport.onCanvasChanged(width, height);
        viewportChanged = true;
    }

}
