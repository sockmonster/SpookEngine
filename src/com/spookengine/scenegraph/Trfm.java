package com.spookengine.scenegraph;

import com.spookengine.math.Mat;
import com.spookengine.math.Vec;

/**
 *
 * @author Oliver Winks
 */
public abstract class Trfm {

    protected float sc;
    protected Vec tr;
    protected Mat ro;
    protected Mat at;

    // convenience vars
    protected Vec pos;
    protected Vec tmpV;
    protected Mat tmpM;

    public Trfm() {
        sc = 1f;
    }

    public void setTo(Trfm t) {
        sc = t.sc;
        tr.setTo(t.tr);
        ro.setTo(t.ro);
        at.setTo(t.at);
    }

    public void toIdentity() {
        sc = 1f;
        tr.toZeros();
        ro.toIdentity();
        at.toIdentity();
    }

    /**
     * Decomposes the 3X3 affine transform matrix into a uniform scale, a 2X2
     * rotation matrix and a translation vector, which are all stored in this
     * Trfm.
     */
    public abstract void decompose();

    public void getPos(Vec store) {
        store.toZeros();
        this.apply(store);
    }

    public float getScale() {
        return sc;
    }

    public void getTranslation(Vec store) {
        store.setTo(tr);
    }
    
    public Mat getAffineTransform() {
        return at;
    }

    public void scaleTo(float scale) {
        sc = scale;
    }

    public void translateTo(Vec pos) {
        tr.setTo(pos);
    }

    public void translateBy(Vec dPos) {
        tr.add(dPos);
    }

    public abstract void update();

    public void add(Trfm local) {
        at.mult(local.at);
    }

    public void apply(Vec vec) {
        at.mult(vec);
    }

    public abstract void applyInverse(Vec vec);

}
