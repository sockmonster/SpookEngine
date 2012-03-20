package com.spookengine.scenegraph.camera;

import com.spookengine.maths.FastMath;
import com.spookengine.maths.Vec2;
import com.spookengine.scenegraph.collision.BoundingSphere;
import com.spookengine.scenegraph.collision.BoundingVolume;
import com.spookengine.scenegraph.renderer.Renderer;

/**
 * TODO: REMOVE REFERENCE TO ANDROID RENDERER!!!!!!!!!!
 * 
 * @author Oliver Winks
 */
public class Cam2 extends Cam<Vec2> {

    protected float rot;

    // convenience vars
    private BoundingSphere bs;

    public Cam2() {
        super();

        pos = new Vec2(0,0);
    }

    public Cam2(Viewport viewport) {
        super(viewport);

        pos = new Vec2(0,0);
    }

    public void setPos(float x, float y) {
        pos.setTo(x, y);
    }

    /**
     * Called when the canvas that this camera's viewport is attached to
     * changes size.
     *
     * @param width The new width of the canvas.
     * @param height Te new height of the canvas.
     */
    @Override
    public void onCanvasChanged(int width, int height) {
        super.onCanvasChanged(width, height);

        modelView.setTo(
                 FastMath.cos(-rot), -FastMath.sin(-rot),  0.0f, -pos.v[0],
                 FastMath.sin(-rot),  FastMath.cos(-rot),  0.0f, -pos.v[1],
                 0.0f,  0.0f,  1.0f,  0.0f,
                 0.0f,  0.0f,  0.0f,  1.0f);
    }

    @Override
    public boolean isVisible(Vec2 pos) {
        if(pos.v[0] <= this.pos.v[0] || pos.v[0] >= this.pos.v[0] + Renderer.canvasWidth ||
           pos.v[1] <= this.pos.v[1] || pos.v[1] >= this.pos.v[1] + Renderer.canvasHeight)
                return false;
        
        return true;
    }

    @Override
    public boolean isVisible(BoundingVolume<Vec2> bounds) {
        if(bounds instanceof BoundingSphere) {
            bs = (BoundingSphere) bounds;
            float bs_x = bs.getWorldPos().v[0];
            float bs_y = bs.getWorldPos().v[1];
            float bs_rad = bs.getRadius();
            if(bs_x + bs_rad <= pos.v[0] || bs_x - bs_rad >= pos.v[0] + Renderer.canvasWidth ||
               bs_y + bs_rad <= pos.v[1] || bs_y - bs_rad >= pos.v[1] + Renderer.canvasHeight)
                return false;
        }

        return true;
    }

    @Override
    public void update() {
        modelView.setTo(
                 FastMath.cos(-rot), -FastMath.sin(-rot),  0.0f, -pos.v[0],
                 FastMath.sin(-rot),  FastMath.cos(-rot),  0.0f, -pos.v[1],
                 0.0f,  0.0f,  1.0f,  0.0f,
                 0.0f,  0.0f,  0.0f,  1.0f);
//        System.out.println("ModelView" + "\n" + modelView);
    }

}
