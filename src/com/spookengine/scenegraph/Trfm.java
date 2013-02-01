package com.spookengine.scenegraph;

import com.spookengine.maths.Euler;
import com.spookengine.maths.FastMath;
import com.spookengine.maths.Mat3;
import com.spookengine.maths.Mat4;
import com.spookengine.maths.Vec3;

/**
 *
 * @author Oliver Winks
 */
public class Trfm {
    
    protected float sc;
    protected final Vec3 tr = new Vec3();
    protected final Mat3 ro = new Mat3();
    protected final Mat4 at = new Mat4();

    // convenience vars
    private final Vec3 tmpV = new Vec3();
    private final Mat3 tmpM3 = new Mat3();
    private final Mat4 tmpM4 = new Mat4();

    public Trfm() {
        toIdentity();
    }
    
    public Trfm setTo(Trfm t) {
        sc = t.sc;
        tr.setTo(t.tr);
        ro.setTo(t.ro);
        at.setTo(t.at);
        
        return this;
    }
    
    public Trfm toIdentity() {
        sc = 1f;
        tr.toZeros();
        ro.toIdentity();
        at.toIdentity();
        
        return this;
    }
    
    public Mat4 getAffineTransform() {
        return at;
    }
    
    public float getScale() {
        return sc;
    }
    
    public Trfm scaleTo(float s) {
        sc = s;
        return this;
    }
    
    public Trfm scaleBy(float dS) {
        sc *= dS;
        return this;
    }
    
    public Vec3 getPosition() {
        return tr;
    }
    
    public Trfm moveTo(float x, float y, float z) {
        tr.setTo(x, y, z);
        return this;
    }
    
    public Trfm moveTo(Vec3 pos) {
        tr.setTo(pos);
        return this;
    }
    
    public Trfm moveBy(float dx, float dy, float dz) {
        tr.add(dx, dy, dz);
        return this;
    }
    
    public Trfm moveBy(Vec3 dPos) {
        tr.add(dPos);
        return this;
    }
    
    public Mat3 getRotationMat() {
        return ro;
    }
    
    public float getRotationAA(Vec3 axis) {
        float fTrace = ro.m[0][0] + ro.m[1][1] + ro.m[2][2];
        float fCos = 0.5f*(fTrace - 1.0f);
        float angle = (float) Math.acos(fCos);  // in [0,PI]

        if(angle > 0.0f) {
            if(angle < Math.PI) {
                axis.v[0] = ro.m[2][1] - ro.m[1][2];
                axis.v[1] = ro.m[0][2] - ro.m[2][0];
                axis.v[2] = ro.m[1][0] - ro.m[0][1];
                axis.norm();
            } else {
                // angle is PI
                float halfInverse;
                if(ro.m[0][0] >= ro.m[1][1]) {
                    // r00 >= r11
                    if(ro.m[0][0] >= ro.m[2][2]) {
                        // r00 is maximum diagonal term
                        axis.v[0] = 0.5f*(float) Math.sqrt(ro.m[0][0] - ro.m[1][1] - ro.m[2][2] + 1.0f);
                        halfInverse = 0.5f/axis.v[0];
                        axis.v[1] = halfInverse*ro.m[1][0];
                        axis.v[2] = halfInverse*ro.m[2][0];
                    } else {
                        // r22 is maximum diagonal term
                        axis.v[2] = 0.5f*(float) Math.sqrt(ro.m[2][2] - ro.m[0][0] - ro.m[1][1] + 1.0f);
                        halfInverse = 0.5f/axis.v[2];
                        axis.v[0] = halfInverse*ro.m[2][0];
                        axis.v[1] = halfInverse*ro.m[2][1];
                    }
                } else {
                    // r11 > r00
                    if(ro.m[1][1] >= ro.m[2][2]) {
                        // r11 is maximum diagonal term
                        axis.v[1] = 0.5f*(float) Math.sqrt(ro.m[1][1] - ro.m[0][0] - ro.m[2][2] + 1.0f);
                        halfInverse  = 0.5f/axis.v[1];
                        axis.v[0] = halfInverse*ro.m[1][0];
                        axis.v[2] = halfInverse*ro.m[2][1];
                    } else {
                        // r22 is maximum diagonal term
                        axis.v[2] = 0.5f*(float) Math.sqrt(ro.m[2][2] - ro.m[0][0] - ro.m[1][1] + 1.0f);
                        halfInverse = 0.5f/axis.v[2];
                        axis.v[0] = halfInverse*ro.m[2][0];
                        axis.v[1] = halfInverse*ro.m[2][1];
                    }
                }
            }
        } else {
            /*
             * The angle is 0 and the matrix is the identity. Any axis will work
             * so just use the x-axis.
             */
            axis.v[0] = 1.0f;
            axis.v[1] = 0.0f;
            axis.v[2] = 0.0f;
        }

        return angle;
    }

    public void getRotationPRY(Euler euler) {
        /*
         *     |  cy*cr   -sy*cp + cy*sr*sp   -sy*-sp + cy*sr*cp   |
         * PRY=|  sy*cr    cy*cp + sy*sr*sp    cy*-sp + sy*sr*cp   |
         *     |  -sr      cr*sp               cr*cp               |
         */
        if(ro.m[2][0] > 0.9999) { // singularity at north pole
            euler.pitch = (float)  Math.atan2(-ro.m[0][1], -ro.m[0][2]);
            euler.roll  = (float) -Math.PI/2;
            euler.yaw   = 0;
            return;
	} else if(ro.m[2][0] < -0.9999) { // singularity at south pole
            euler.pitch = (float)  Math.atan2(-ro.m[0][1],  ro.m[0][2]);
            euler.roll  = (float)  Math.PI/2;
            euler.yaw   = 0;
            return;
	}
        
        euler.pitch = (float)  Math.atan2(ro.m[2][1], ro.m[2][2]);
        euler.roll  = (float) -Math.asin(ro.m[2][0]);
        euler.yaw   = (float)  Math.atan2(ro.m[1][0], ro.m[0][0]);
    }
    
    public void getRotationYPR(Euler euler) {
        /*
         *     |    cr*cy + sr*sp*sy    cr*-sy + sr*sp*cy    sr*cp    |
         * YPR=|    cp*sy               cp*cy               -sp       |
         *     |   -sr*cy + cr*sp*sy   -sr*-sy + cr*sp*cy    cr*cp    |
         */
        if(ro.m[1][2] > 0.9999) { // singularity at north pole
            euler.yaw   = (float)  Math.atan2(ro.m[0][1], ro.m[0][0]);
            euler.pitch = (float) -Math.PI/2;
            euler.roll  = 0;
            return;
	} else if(ro.m[1][2] < -0.9999) { // singularity at south pole
            euler.yaw   = (float)  Math.atan2(-ro.m[0][1],  ro.m[0][0]);
            euler.pitch = (float)  Math.PI/2;
            euler.roll  = 0;
            return;
	}
        
        euler.yaw   = (float)  Math.atan2(ro.m[1][0], ro.m[1][1]);
        euler.pitch = (float) -Math.asin(ro.m[1][2]);
        euler.roll  = (float)  Math.atan2(ro.m[0][2], ro.m[2][2]);
    }
    
    public void getRotationRPY(Euler euler) {
        /*
         *     |    cy*cr + -sy*-sp*-sr    -sy*cp    cy*sr + -sy*-sp*cr    |
         * RPY=|    sy*cr +  cy*-sp*-sr     cy*cp    sy*sr +  cy*-sp*cr    |
         *     |    cp*-sr                  sp       cp*cr                 |
         */
        if(ro.m[2][1] > 0.999) { // singularity at north pole
            euler.roll   = (float) Math.atan2(ro.m[0][2], ro.m[0][0]);
            euler.pitch = (float) Math.PI/2;
            euler.yaw  = 0;
            return;
	} else if(ro.m[2][1] < -0.999) { // singularity at south pole
            euler.roll   = (float) Math.atan2(ro.m[0][2],  ro.m[0][0]);
            euler.pitch = (float) -Math.PI/2;
            euler.yaw  = 0;
            return;
	}
        
        euler.roll  = (float)  Math.atan2(-ro.m[2][0],  ro.m[2][2] );
        euler.pitch = (float)  Math.asin( ro.m[2][1] );
        euler.yaw   = (float)  Math.atan2(-ro.m[0][1],  ro.m[1][1] );
    }
    
    public Trfm rotateTo(Mat3 rot) {
        ro.setTo(rot);
        return this;
    }
    
    public Trfm rotateTo(float angle, float x, float y, float z) {
        float cos = FastMath.cos(angle);
        float sin = FastMath.sin(angle);

        float oneMinusCos = 1.0f - cos;
        float x2 = x*x;
        float y2 = y*y;
        float z2 = z*z;
        float xym = x*y*oneMinusCos;
        float xzm = x*z*oneMinusCos;
        float yzm = y*z*oneMinusCos;
        float xSin = x*sin;
        float ySin = y*sin;
        float zSin = z*sin;

        ro.m[0][0] = x2*oneMinusCos + cos;
        ro.m[0][1] = xym - zSin;
        ro.m[0][2] = xzm + ySin;
        ro.m[1][0] = xym + zSin;
        ro.m[1][1] = y2*oneMinusCos + cos;
        ro.m[1][2] = yzm - xSin;
        ro.m[2][0] = xzm - ySin;
        ro.m[2][1] = yzm + xSin;
        ro.m[2][2] = z2*oneMinusCos + cos;
        
        return this;
    }

    public Trfm rotateTo(float angle, Vec3 axis) {
        rotateTo(angle, axis.x(), axis.y(), axis.z());
        return this;
    }
    
    public Trfm rotateToPRY(float pitch, float roll, float yaw) {
        /* 
         * The conversion from Euler angles to rotation matrix is calculated as 
         * follows: 
         * 
         * The rotation matrices for each axis
         *   i.e.
         *          |   1       0       0   |
         *        P=|   0     cos(p) -sin(p)|
         *          |   0     sin(p)  cos(p)|
         * 
         *          | cos(r)    0     sin(r)|
         *        R=|   0       1       0   |
         *          |-sin(r)    0     cos(r)|
         * 
         *          | cos(y) -sin(y)    0   |
         *        Y=| sin(y)  cos(y)    0   |
         *          |   0       0       1   |
         * 
         * are multiplied in the order of rotation for the Euler angles. In the 
         * spooked engine rotation matrices are multiplied in the order of 
         * rotation (not back to front like other 3D engine). For example, 
         * converting from XYZ Euler angles; rotation matrices for each axis 
         * are created and multiplied in this order X then Y then Z.
         * 
         * The result of this multiplication is given below.
         */
        
        float sy = FastMath.sin(yaw);
        float cy = FastMath.cos(yaw);
        float sp = FastMath.sin(pitch);
        float cp = FastMath.cos(pitch);
        float sr = FastMath.sin(roll);
        float cr = FastMath.cos(roll);

        // rotate around X then Y then Z
        ro.m[0][0] = cy*cr;
        ro.m[0][1] = -sy*cp + cy*sr*sp;
        ro.m[0][2] = -sy*-sp + cy*sr*cp;
        ro.m[1][0] = sy*cr;
        ro.m[1][1] = cy*cp + sy*sr*sp;
        ro.m[1][2] = cy*-sp + sy*sr*cp;
        ro.m[2][0] = -sr;
        ro.m[2][1] = cr*sp;
        ro.m[2][2] = cr*cp;
        
        return this;
    }

    public Trfm rotateToPRY(Euler euler) {
        rotateToPRY(euler.pitch, euler.roll, euler.yaw);
        return this;
    }
    
    public Trfm rotateToYPR(float yaw, float pitch, float roll) {
        /* 
         * The conversion from Euler angles to rotation matrix is calculated as 
         * follows: 
         * 
         * The rotation matrices for each axis
         *   i.e.
         *          |   1       0       0   |
         *        P=|   0     cos(p) -sin(p)|
         *          |   0     sin(p)  cos(p)|
         * 
         *          | cos(r)    0     sin(r)|
         *        R=|   0       1       0   |
         *          |-sin(r)    0     cos(r)|
         * 
         *          | cos(y) -sin(y)    0   |
         *        Y=| sin(y)  cos(y)    0   |
         *          |   0       0       1   |
         * 
         * are multiplied in the order of rotation for the Euler angles. In the 
         * spooked engine rotation matrices are multiplied in the order of 
         * rotation (not back to front like other 3D engine). For example, 
         * converting from YXZ Euler angles; rotation matrices for each axis 
         * are created and multiplied in this order Y then X then Z.
         * 
         * The result of this multiplication is given below.
         */
        
        float sy = FastMath.sin(yaw);
        float cy = FastMath.cos(yaw);
        float sp = FastMath.sin(pitch);
        float cp = FastMath.cos(pitch);
        float sr = FastMath.sin(roll);
        float cr = FastMath.cos(roll);

        // rotate around Z then X then Y
        ro.m[0][0] = cr*cy + sr*sp*sy;
        ro.m[0][1] = cr*-sy + sr*sp*cy;
        ro.m[0][2] = sr*cp;
        ro.m[1][0] = cp*sy;
        ro.m[1][1] = cp*cy;
        ro.m[1][2] = -sp;
        ro.m[2][0] = -sr*cy + cr*sp*sy;
        ro.m[2][1] = -sr*-sy + cr*sp*cy;
        ro.m[2][2] = cr*cp;
        
        return this;
    }

    public Trfm rotateToYPR(Euler euler) {
        rotateToYPR(euler.yaw, euler.pitch, euler.roll);
        return this;
    }
    
    public Trfm rotateToRPY(float roll, float pitch, float yaw) {
        /* 
         * The conversion from Euler angles to rotation matrix is calculated as 
         * follows: 
         * 
         * The rotation matrices for each axis
         *   i.e.
         *          |   1       0       0   |
         *        P=|   0     cos(p) -sin(p)|
         *          |   0     sin(p)  cos(p)|
         * 
         *          | cos(r)    0     sin(r)|
         *        R=|   0       1       0   |
         *          |-sin(r)    0     cos(r)|
         * 
         *          | cos(y) -sin(y)    0   |
         *        Y=| sin(y)  cos(y)    0   |
         *          |   0       0       1   |
         * 
         * are multiplied in the order of rotation for the Euler angles. In the 
         * spooked engine rotation matrices are multiplied in the order of 
         * rotation (not back to front like other 3D engine). For example, 
         * converting from ZXY Euler angles; rotation matrices for each axis 
         * are created and multiplied in this order Z then X then Y.
         * 
         * The result of this multiplication is given below.
         */
        
        float sy = FastMath.sin(yaw);
        float cy = FastMath.cos(yaw);
        float sp = FastMath.sin(pitch);
        float cp = FastMath.cos(pitch);
        float sr = FastMath.sin(roll);
        float cr = FastMath.cos(roll);
        
        // rotate around Z then X then Y
        ro.m[0][0] = cy*cr + -sy*-sp*-sr;
        ro.m[0][1] = -sy*cp;
        ro.m[0][2] = cy*sr + -sy*-sp*cr;
        ro.m[1][0] = sy*cr + cy*-sp*-sr;
        ro.m[1][1] = cy*cp;
        ro.m[1][2] = sy*sr + cy*-sp*cr;
        ro.m[2][0] = cp*-sr;
        ro.m[2][1] = sp;
        ro.m[2][2] = cp*cr;
        
        return this;
    }
    
    public Trfm rotateToRPY(Euler euler) {
        rotateToRPY(euler.roll, euler.pitch, euler.yaw);
        return this;
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
    
    public void add(Trfm trfm) {
        at.mult(trfm.at);
        
        sc *= trfm.sc;
        ro.mult(trfm.ro);
        tr.setTo(at.m[0][3], at.m[1][3], at.m[2][3]);
    }
    
    public void sub(Trfm transform) {
        at.mult(tmpM4.setTo(transform.at).invert());
        
        sc /= transform.sc;
        ro.mult(tmpM3.setTo(transform.ro).invert());
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
