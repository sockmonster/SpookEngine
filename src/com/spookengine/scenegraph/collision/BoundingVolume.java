package com.spookengine.scenegraph.collision;

import com.spookengine.math.Vec;
import com.spookengine.math.Vec2;
import com.spookengine.math.Vec3;
import com.spookengine.scenegraph.Trfm;
import java.nio.FloatBuffer;
import java.util.List;

/**
 *
 * @author Oliver Winks
 */
public abstract class BoundingVolume<V extends Vec> {

    protected boolean is2D;
    protected V localPos;
    protected V worldPos;
    protected V localExtent;
    protected V worldExtent;
    protected FloatBuffer vertices;

    protected BoundingVolume(boolean is2D) {
        this.is2D = is2D;

        if(is2D) {
            localPos = (V) new Vec2();
            worldPos = (V) new Vec2();
            localExtent = (V) new Vec2();
            worldExtent = (V) new Vec2();
        } else {
            localPos = (V) new Vec3();
            worldPos = (V) new Vec3();
            localExtent = (V) new Vec3();
            worldExtent = (V) new Vec3();
        }
    }

    protected BoundingVolume(boolean is2D, Vec pos) {
        this.is2D = is2D;

        if(is2D) {
            localPos = (V) new Vec2(pos);
            worldPos = (V) new Vec2(pos);
            localExtent = (V) new Vec2();
            worldExtent = (V) new Vec2();
        } else {
            localPos = (V) new Vec3(pos);
            worldPos = (V) new Vec3(pos);
            localExtent = (V) new Vec3();
            worldExtent = (V) new Vec3();
        }
    }

    public V getWorldPos() {
        return worldPos;
    }

    /**
     * Returns the distance from the centre of the edge of this
     * BoundingVolume along the 3 local axis.
     *
     * @return A Vector3 which represents the distance from the centre
     * of the BoundingVolume to the wall of the BoundingVolume.
     */
    public V getLocalExtent() {
        return localExtent;
    }

    /**
     * Returns the distance from the centre to the edge of this
     * BoundingVolume along the 3 world axis. For example,
     * an oriented bounding box will return the extent of the
     * axis aligned bounding box that encases it. However, for
     * a bounding sphere's and for axis aligned bounding box's
     * the local extent is the same as the world extent.
     *
     * @return The distance from the centre to the edge of
     * this BoundingVolume along the world axis.
     */
    public V getWorldExtent() {
        return worldExtent;
    }

    /**
     * Sets the size of this bounding volume.
     *
     * @param width The width of the bounding volume.
     * @param height The height of the bounding volume.
     * @param depth The depth of the bounding volume.
     */
    public abstract void setSize(V dim);

    /**
     * Wraps the model (specified by the vertices FloatBuffer) in this bounding
     * volume.
     *
     * <i>
     * The extent of the bounding volume is calculated in this method,
     * it assumes that the model has a scale of 1. Do not call this method or
     * set the models bounds after the model has been scaled!
     * </i>
     *
     * <i> Be warned! This is an expensive method, so call it spareingly.</i>
     *
     * @param vertices
     */
    public abstract void wrap(FloatBuffer vertices);

    /**
     * Wraps all BoundingVolumes in the list, between the two given index's
     * ('from' and 'to'), in this BoundingVolume.
     *
     * @param bounds A list of bounding volumes.
     * @param from the start index in the bounds list.
     * @param to the end index in the bounds list.
     */
    public abstract void wrap(List<BoundingVolume> bounds, int from, int to);

    /**
     * Re-calculates this BoundingVolume's world position. This is called
     * automatically by the renderer everytime the transform of the Node this
     * BoundingVolume wraps has been modified.
     *
     * @param worldTransform The new world transform.
     */
    public abstract void applyTransform(Trfm worldTransform);

    public abstract boolean collides(BoundingVolume bounds);

    @Override
    public abstract BoundingVolume clone();

}
