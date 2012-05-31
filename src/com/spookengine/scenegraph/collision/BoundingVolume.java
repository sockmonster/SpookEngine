package com.spookengine.scenegraph.collision;

import com.spookengine.maths.Vec3;
import com.spookengine.scenegraph.Trfm;
import java.nio.FloatBuffer;
import java.util.List;

/**
 *
 * @author Oliver Winks
 */
public abstract class BoundingVolume {
    
    protected Vec3 localPos;
    protected Vec3 worldPos;
    protected Vec3 localExtent;
    protected Vec3 worldExtent;
    protected FloatBuffer vertices;

    protected BoundingVolume() {
        localPos = new Vec3();
        worldPos = new Vec3();
        localExtent = new Vec3();
        worldExtent = new Vec3();
    }

    protected BoundingVolume(Vec3 pos) {
        localPos = new Vec3(pos);
        worldPos = new Vec3(pos);
        localExtent = new Vec3();
        worldExtent = new Vec3();
    }

    public Vec3 getWorldPos() {
        return worldPos;
    }

    /**
     * Returns the distance from the centre of the edge of this
     * BoundingVolume along the 3 local axis.
     *
     * @return A Vector3 which represents the distance from the centre
     * of the BoundingVolume to the wall of the BoundingVolume.
     */
    public Vec3 getLocalExtent() {
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
    public Vec3 getWorldExtent() {
        return worldExtent;
    }

    /**
     * Sets the size of this bounding volume.
     *
     * @param width The width of the bounding volume.
     * @param height The height of the bounding volume.
     * @param depth The depth of the bounding volume.
     */
    public abstract void setSize(Vec3 dim);

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
