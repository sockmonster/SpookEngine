package com.spookengine.scenegraph;

import com.spookengine.maths.Vec2;
import com.spookengine.maths.Mat3;
import com.spookengine.maths.FastMath;
import com.spookengine.maths.Mat2;
import com.spookengine.maths.Vec;

/**
 *
 * @author Oliver Winks
 */
public class Trfm2 extends Trfm {

    public Trfm2() {
        super();

        tr = new Vec2();
        ro = new Mat2();
        at = new Mat3();

        tmpV = new Vec2();
        tmpM = new Mat3();
    }

    @Override
    public void decompose() {
        // scale
        ((Vec2) tmpV).setTo(at.m[0][0], at.m[0][1]);
        sc = tmpV.length();

        // translate
        ((Vec2) tr).setTo(at.m[0][2], at.m[1][2]);

        // rotate
        ro.setTo(at);
        ro.mult(1.0f/sc);
    }

    public float getRotation() {
        return (float) Math.acos(ro.m[0][0]);
    }
    
    @Override
    public Mat3 getAffineTransform() {
        return (Mat3) at;
    }

    public void translateTo(float x, float y) {
        ((Vec2) tr).setTo(x, y);
    }

    public void translateBy(float dx, float dy) {
        ((Vec2) tr).add(dx, dy);
    }

    public void rotateTo(float angle) {
        float cos = FastMath.cos(angle);
        float sin = FastMath.sin(angle);

        ro.m[0][0]= cos; ro.m[0][1]=-sin;
        ro.m[1][0]= sin; ro.m[1][1]= cos;
    }

    @Override
    public void update() {
        ((Mat3) at).setTo(
                ro.m[0][0]*sc, ro.m[0][1]*sc, tr.v[0],
                ro.m[1][0]*sc, ro.m[1][1]*sc, tr.v[1],
                0.0f, 0.0f, 1.0f);
    }

    @Override
    public void apply(Vec vec) {
        at.mult(vec);
    }

    @Override
    public void applyInverse(Vec vec) {
        tmpM.setTo(at);
        ((Mat2) tmpM).invert();

        tmpM.mult(vec);
    }

}
