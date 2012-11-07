package com.spookengine.core.camera;

import com.spookengine.maths.FastMath;
import com.spookengine.maths.Mat4;
import com.spookengine.maths.Plane;
import com.spookengine.maths.Vec3;
import com.spookengine.scenegraph.collision.BoundingSphere;
import com.spookengine.scenegraph.collision.BoundingVolume;

// TODO: REMEMBER WHEN SWITCHING SCENES THE NEW SCENE MUST HAVE IT'S CAMERA'S
// onCanvasChange() METHOD CALLED SO THAT THE ASPECT RATIO CAN BE RECALCULATED
// AS THE CANVAS MAY HAVE CHANGED WHILST THE NEW SCENE WAS INACTIVE!

/**
 *
 * @author Oliver Winks
 */
public class Cam {
    public enum Projection {ORTHOGRAPHIC, PERSPECTIVE}
    
    /** Used by renderer, DO NOT MODIFY! */
    public boolean viewportChanged = true;
    
    /** Used by renderer, DO NOT MODIFY! */
    public boolean frustumChanged = true;

    // viewport
    protected Viewport viewport;

    // camera
    public Projection projection;
    public Vec3 pos;
    protected Mat4 modelView;
    protected Mat4 modelViewInv;

    // frustum
    private float fov, halfFOV;
    private float ratio;
    private float nearClip;
    private float farClip;
    private Plane[] planes;

    // camera
    protected Vec3 x, y, z;
    protected Vec3 up;
    protected Vec3 lookAt;
    
    // coordinate system
    protected Vec3 globalUp;
    protected Vec3 globalForward;

    // convenience vars
    private float nh, nw, fh, fw;
    private float tanFOV;
    private Vec3 tmp1, tmp2, tmp3, tmp4;
    private Vec3 ntl, ntr, nbl, nbr;
    private Vec3 ftl, ftr, fbl, fbr;

    public Cam(Projection projection) {
        this.projection = projection;
        
        switch(projection) {
            case ORTHOGRAPHIC:
                viewport = new Viewport(true);
                viewportChanged = true;
                break;
                
            case PERSPECTIVE:
                viewport = new Viewport();
                viewportChanged = true;
                break;
        }

        modelView = new Mat4();
        modelViewInv = new Mat4();
        
        fov = 45.0f;
        halfFOV = 22.5f;
        nearClip = 1f;
        farClip = 100f;
        planes = new Plane[6];
        for(int i=0; i<6; i++)
            planes[i] = new Plane();
        frustumChanged = true;

        x = new Vec3();
        y = new Vec3();
        z = new Vec3();

        tmp1 = new Vec3();
        tmp2 = new Vec3();
        tmp3 = new Vec3();
        tmp4 = new Vec3();

        ntl = new Vec3();
        ntr = new Vec3();
        nbl = new Vec3();
        nbr = new Vec3();

        ftl = new Vec3();
        ftr = new Vec3();
        fbl = new Vec3();
        fbr = new Vec3();
        
        pos = new Vec3(0,1,0);
        up = new Vec3(0,0,1);
        lookAt = new Vec3(0,0,0);
    }

    public Cam(Projection projection, Viewport viewport, float fov, float nearClip, float farClip) {
        this.projection = projection;
        this.viewport = viewport;
        viewportChanged = true;

        modelView = new Mat4();
        modelViewInv = new Mat4();

        this.fov = fov;
        this.halfFOV = fov/2;
        this.nearClip = nearClip;
        this.farClip = farClip;
        planes = new Plane[6];
        for(int i=0; i<6; i++)
            planes[i] = new Plane();
        this.frustumChanged = true;

        x = new Vec3();
        y = new Vec3();
        z = new Vec3();

        tmp1 = new Vec3();
        tmp2 = new Vec3();
        tmp3 = new Vec3();
        tmp4 = new Vec3();

        ntl = new Vec3();
        ntr = new Vec3();
        nbl = new Vec3();
        nbr = new Vec3();

        ftl = new Vec3();
        ftr = new Vec3();
        fbl = new Vec3();
        fbr = new Vec3();

        pos = new Vec3(0,1,0);
        up = new Vec3(0,0,1);
        lookAt = new Vec3(0,0,0);
    }
    
    public void setViewport(Viewport viewport) {
        viewportChanged = true;
        this.viewport = viewport;
    }

    public Viewport getViewport() {
        return viewport;
    }
    
    public Mat4 getModelView() {
        return modelView;
    }

    public void setFOV(float fov) {
        frustumChanged = true;
        if(fov > 0 && fov < 180) {
            this.fov = fov;
            this.halfFOV = fov/2;
        }
    }

    public float getFOV() {
        return fov;
    }

    public float getAspectRatio() {
        return ratio;
    }

    public void setNearClip(float nearClip) {
        frustumChanged = true;
        if(nearClip < farClip) {
            this.nearClip = nearClip;
        }
    }

    public float getNearClip() {
        return nearClip;
    }

    public void setFarClip(float farClip) {
        frustumChanged = true;
        if(farClip > nearClip) {
            this.farClip = farClip;
        }
    }

    public float getFarClip() {
        return farClip;
    }

    public void setUp(Vec3 up) {
        this.up.setTo(up);
    }

    public void setUp(float x, float y, float z) {
        up.setTo(x, y, z);
    }

    public Vec3 getUp() {
        return up;
    }

    public void lookAt(Vec3 lookAt) {
        this.lookAt.setTo(lookAt);
    }

    public void lookAt(float x, float y, float z) {
        lookAt.setTo(x, y, z);
    }

    public Vec3 getLookAt() {
        return lookAt;
    }

    public Vec3 getXAxis() {
        return x;
    }

    public Vec3 getYAxis() {
        return y;
    }

    public Vec3 getZAxis() {
        return z;
    }
    
    public boolean isVisible(Vec3 pos) {
        for(int i=0; i<planes.length; i++) {
            if(planes[i].dist(pos) < 0) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean isVisible(BoundingVolume bounds) {
        if(bounds instanceof BoundingSphere)
            return isVisible((BoundingSphere) bounds);
//        else if(bounds instanceof BoundingBox)
//            return isVisible((BoundingBox) bounds);
//        else if(bounds instanceof AABoundingBox)
//            return isVisible((AABoundingBox) bounds);

        return true;
    }

    private boolean isVisible(BoundingSphere bounds) {
        for(int i=0; i<planes.length; i++) {
            if(planes[i].dist(bounds.getWorldPos()) < -bounds.getWorldExtent().v[0]) {
                return false;
            }
        }
        return true;
    }
    
    private void calculatePlanes() {
        // compute width and height of the near and far plane sections
        if(frustumChanged) {
            float tang = (float) Math.tan(Math.PI/180 * halfFOV) ;
            nh = nearClip*tang;
            nw = nh*ratio;
            fh = farClip*tang;
            fw = fh*ratio;
        }

        // compute the Z axis of camera
        // this axis points in the opposite direction from the looking direction
        y.setTo(pos).sub(lookAt).norm();

        // X axis of camera with given "up" vector and Z axis
        x.setTo(up).cross(y).norm();

        // the real "up" vector is the cross product of Z and X
        z.setTo(y).cross(x);

        // near plane
        tmp4.setTo(y).mult(nearClip);
        tmp1.setTo(pos).sub(tmp4);
        tmp2.setTo(z).mult(nh);
        tmp3.setTo(x).mult(nw);
        ntl.setTo(tmp1).add(tmp2).sub(tmp3);
        ntr.setTo(tmp1).add(tmp2).add(tmp3);
        nbl.setTo(tmp1).sub(tmp2).sub(tmp3);
        nbr.setTo(tmp1).sub(tmp2).add(tmp3);

        // far plane
        tmp4.setTo(y).mult(farClip);
        tmp1.setTo(pos).sub(tmp4);
        tmp2.setTo(z).mult(fh);
        tmp3.setTo(x).mult(fw);
        ftl.setTo(tmp1).add(tmp2).sub(tmp3);
        ftr.setTo(tmp1).add(tmp2).add(tmp3);
        fbl.setTo(tmp1).sub(tmp2).sub(tmp3);
        fbr.setTo(tmp1).sub(tmp2).add(tmp3);

        // compute the six planes
        planes[0].setTo(ntl,ntr,nbr); // near
        planes[1].setTo(ftr,ftl,fbl); // far
        planes[2].setTo(ntr,ntl,ftl); // top
        planes[3].setTo(nbl,nbr,fbr); // bottom
        planes[4].setTo(ntl,nbl,fbl); // left
        planes[5].setTo(nbr,ntr,fbr); // right
    }

    /**
     * THIS NEEDS TO BE REDONE, IT'S NOT WORKING!
     * 
     * @param screenX
     * @param screenY
     * @param pickPos
     * @param pickDir
     */
    public void generatePickRay(float screenX, float screenY, Vec3 pickPos, Vec3 pickDir) {
        System.out.println(screenX + " " + screenY);
        // normalize the screen coords
        float sx = (screenX/(viewport.getDimensions().v[0]/2) - 1.0f)/ratio;
	float sy = 1.0f - screenY/(viewport.getDimensions().v[1]/2);

//        if(sx < sy)
//            sy /= ratio;
//        else if(sy < sx)
//            sx /= ratio;

        // convert normalized screen coords to frustum coords
        sx *= tanFOV;
        sy *= tanFOV;

        // create a line in the frustum between the near and far clipping plane
        pickPos.setTo(sx*nearClip, sy*nearClip, nearClip);
        pickDir.setTo(sx*farClip , sy*farClip , -farClip);
        
        // invert the view matrix
        modelViewInv.setTo(modelView);
        modelViewInv.invert();
        
        // convert frustum line to world line by transform the frustum line 
        // by the inverse view matrix
        modelViewInv.mult(pickPos);
        modelViewInv.mult(pickDir);

        // convert line to ray
        pickDir.sub(pickPos).norm();
    }
    
    private void calculateModelView() {
        // calculate modelview matrix
        tmp1.setTo(lookAt).sub(pos).norm(); // (forward)
        tmp2.setTo(tmp1).cross(up).norm();  // (side)
        tmp3.setTo(up);                     // (up)
        
        modelView.setTo(
            tmp2.v[0],  tmp2.v[1],  tmp2.v[2], -pos.dot(tmp2),
            tmp3.v[0],  tmp3.v[1],  tmp3.v[2], -pos.dot(tmp3),
           -tmp1.v[0], -tmp1.v[1], -tmp1.v[2],  pos.dot(tmp1),
                 0.0f,       0.0f,       0.0f,          1.0f);
    }
    
    public void update() {
        // recalculate frustum
        calculatePlanes();
        tanFOV = (float) Math.tan(FastMath.toRadians(halfFOV));

        // recalculate model view matrix
        calculateModelView();
    }
    
    public void onCanvasChanged(int width, int height) {
        viewport.onCanvasChanged(width, height);
        viewportChanged = true;

        // recalculate frustum
        ratio = (float) width/height;
        frustumChanged = true;
        calculatePlanes();

        // calculate modelview matrix
        calculateModelView();
    }

}
