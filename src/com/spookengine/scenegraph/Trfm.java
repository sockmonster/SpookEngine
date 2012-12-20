package com.spookengine.scenegraph;

import com.spookengine.maths.Mat3;
import com.spookengine.maths.Mat4;
import com.spookengine.maths.Vec3;

/**
 *
 * @author Oliver Winks
 */
public class Trfm {
    
    public float sc;
    public final Vec3 tr = new Vec3();
    public final Mat3 ro = new Mat3();
    public final Mat4 at = new Mat4();

    // convenience vars
    private final Vec3 tmpV = new Vec3();
    private final Mat3 tmpM3 = new Mat3();
    private final Mat4 tmpM4 = new Mat4();

    public Trfm() {
        toIdentity();
    }
    
    public void setTo(Trfm t) {
        sc = t.sc;
        tr.setTo(t.tr);
        ro.setTo(t.ro);
        at.setTo(t.at);
    }
    
    public final void toIdentity() {
        sc = 1f;
        tr.toZeros();
        ro.toIdentity();
        at.toIdentity();
    }
    
    public void decompose() {
        // scale
        tmpV.setTo(at.m[0][0], at.m[0][1], at.m[0][2]);
        sc = tmpV.length();

        // translate
        tr.setTo(at.m[0][3], at.m[1][3], at.m[2][3]);

        // rotate
        ro.setTo(
                at.m[0][0], at.m[0][1], at.m[0][2],
                at.m[1][0], at.m[1][1], at.m[1][2],
                at.m[2][0], at.m[2][1], at.m[2][2]);
        ro.mult(1.0f/sc);
    }
    
    public void update() {
        at.setTo(
                ro.m[0][0]*sc, ro.m[0][1]*sc, ro.m[0][2]*sc, tr.v[0],
                ro.m[1][0]*sc, ro.m[1][1]*sc, ro.m[1][2]*sc, tr.v[1],
                ro.m[2][0]*sc, ro.m[2][1]*sc, ro.m[2][2]*sc, tr.v[2],
                0.0f, 0.0f, 0.0f, 1.0f);
    }
    
    public void add(Trfm local) {
        sc *= local.sc;
        ro.mult(local.ro);
        
        at.mult(local.at);
        
        tr.setTo(at.m[0][3], at.m[1][3], at.m[2][3]);
    }
    
    public void sub(Trfm transform) {
        sc /= transform.sc;
        ro.mult(tmpM3.setTo(transform.ro).invert());
        
        at.mult(tmpM4.setTo(transform.at).invert());
        
        tr.setTo(at.m[0][3], at.m[1][3], at.m[2][3]);
    }

    public void apply(Vec3 vec) {
        at.mult(vec);
    }
    
    public void applyInverse(Vec3 vec) {
        tmpM4.setTo(at);
        tmpM4.invert();

        tmpM4.mult(vec);
    }

}
