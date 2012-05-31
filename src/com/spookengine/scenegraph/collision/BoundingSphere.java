package com.spookengine.scenegraph.collision;

import com.spookengine.maths.Vec3;
import com.spookengine.scenegraph.Trfm;
import java.nio.FloatBuffer;
import java.util.List;

/**
 *
 * @author Oliver Winks
 */
public class BoundingSphere extends BoundingVolume {

    private float normalRadius; // pre-scale radius
    private float radius;

    // convenience vars
    private Vec3 tmpV0;
    private Vec3 tmpV1;
    private Vec3 tmpV2;

    public BoundingSphere() {
        super();
        
        tmpV0 = new Vec3();
        tmpV1 = new Vec3();
        tmpV2 = new Vec3();
    }

    public BoundingSphere(Vec3 pos, float rad) {
        super(pos);

        normalRadius = rad;
        radius = rad;
        
        tmpV0 = new Vec3();
        tmpV1 = new Vec3();
        tmpV2 = new Vec3();
    }

    /**
     * {@inheritDoc}
     *
     * Note: dim must contains identical values!
     * @param dim
     */
    @Override
    public void setSize(Vec3 dim) {
        // find the circle's radius
        normalRadius = dim.v[0]/2;
        radius = normalRadius;
        localExtent.setTo(dim);
        worldExtent.setTo(dim);
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void wrap(FloatBuffer vertices) {
        /* 
         * initialise the min and max of the box that encompasses this sphere,
         * tmpV0 = min
         * tmpV1 = max
         */
        for(int i=0; i<tmpV0.v.length; i++) {
            tmpV0.v[i] = Float.MAX_VALUE;
            tmpV1.v[i] = -Float.MAX_VALUE;
        }

        /*
         * find the max and min extents of each dimension
         * i = dimension
         */
        float v;
        for(int vert=0; vert<vertices.capacity(); vert+=3) {
            for(int i=0; i<localExtent.v.length; i++) {
                v = vertices.get(vert);
                if(v < tmpV0.v[i])
                    tmpV0.v[i] = v;
                if(v > tmpV1.v[i])
                    tmpV1.v[i] = v;
            }
        }

        // calculate the centre (localPos) of this bounding sphere.
        for(int i=0; i<localPos.v.length; i++)
            localPos.v[i] = tmpV0.v[i] + (tmpV1.v[i] - tmpV0.v[i])/2;

        // find the sphere's radius
        float dist2 = 0;
        for(int i=0; i<tmpV0.v.length; i++)
            dist2 += (tmpV1.v[i] - tmpV0.v[i])*(tmpV1.v[i] - tmpV0.v[i]);

        normalRadius = (float) Math.sqrt(dist2)/2;
        radius = normalRadius;
        localExtent.setTo(radius, radius, radius);
        worldExtent.setTo(radius, radius, radius);
    }

    @Override
    public void wrap(List<BoundingVolume> bounds, int from, int to) {
        /*
         * initialise the min and max of the box that encompasses this sphere,
         * tmpV0 = min
         * tmpV1 = max
         */
        for(int i=0; i<tmpV0.v.length; i++) {
            tmpV0.v[i] = Float.MAX_VALUE;
            tmpV1.v[i] = -Float.MAX_VALUE;
        }
        
        for(int count=from; count<to; count++) {
            tmpV2.setTo(bounds.get(count).getWorldExtent());

            for(int i=0; i<tmpV2.v.length; i++) {
                float v = bounds.get(count).getWorldPos().v[i];
                if(v - tmpV2.v[i] < tmpV0.v[i])
                    tmpV0.v[i] = v - tmpV2.v[0];
                if(v + tmpV2.v[0] > tmpV1.v[i])
                    tmpV1.v[1] = v + tmpV2.v[0];
            }
        }

        // calculate the centre (localPos) of this bounding sphere.
        for(int i=0; i<localPos.v.length; i++)
            localPos.v[i] = tmpV0.v[i] + (tmpV1.v[i] - tmpV0.v[i])/2;

        // find the sphere's radius
        float max = tmpV1.v[0] - tmpV0.v[0];
        for(int i=1; i<tmpV0.v.length; i++)
            max = Math.max(max, tmpV1.v[i] - tmpV0.v[i]);
        normalRadius = max/2;
        radius = normalRadius;
        localExtent.setTo(radius, radius, radius);
        worldExtent.setTo(radius, radius, radius);
    }

    @Override
    public void applyTransform(Trfm worldTransform) {
        worldPos.setTo(localPos);
        worldTransform.apply(worldPos);

        radius = normalRadius*worldTransform.sc;
        localExtent.setTo(radius, radius, radius);
        worldExtent.setTo(radius, radius, radius);
    }

    @Override
    public boolean collides(BoundingVolume bounds) {
        if(bounds instanceof BoundingSphere) {
            if(worldPos.dist(bounds.worldPos) <= radius + ((BoundingSphere) bounds).radius)
                return true;
        }

        return false;
    }

    @Override
    public BoundingVolume clone() {
        BoundingSphere clone = new BoundingSphere();
        clone.localPos.setTo(localPos);
        clone.worldPos.setTo(worldPos);
        clone.localExtent.setTo(localExtent);
        clone.worldExtent.setTo(worldExtent);
        clone.normalRadius = normalRadius;
        clone.radius = radius;

        return clone;
    }

}
