package com.spookengine.scenegraph;

import com.spookengine.maths.Mat4;
import com.spookengine.maths.Mat3;
import com.spookengine.maths.FastMath;
import com.spookengine.maths.Vec3;
import com.spookengine.maths.Vec;

/**
 *
 * @author Oliver Winks
 */
public class Trfm3 extends Trfm {
    
    // convenience var
    private Mat3 tmpM3;

    public Trfm3() {
        super();

        tr = new Vec3();
        ro = new Mat3();
        at = new Mat4();

        tmpV = new Vec3();
        tmpM = new Mat4();
        tmpM3 = new Mat3();
    }

    @Override
    public void decompose() {
        // scale
        ((Vec3) tmpV).setTo(at.m[0][0], at.m[0][1], at.m[0][2]);
        sc = tmpV.length();

        // translate
        ((Vec3) tr).setTo(at.m[0][3], at.m[1][3], at.m[2][3]);

        // rotate
        ro.setTo(at);
        ro.mult(1.0f/sc);
    }

    public float getRotation(Vec3 axis) {
        float fTrace = ro.m[0][0] + ro.m[1][1] + ro.m[2][2];
        float fCos = 0.5f*(fTrace - 1.0f);
        float angle = (float) Math.acos(fCos);  // in [0,PI]

        if(angle > 0.0f) {
            if(angle < Math.PI) {
                axis.v[0] = ro.m[1][2] - ro.m[2][1];
                axis.v[1] = ro.m[2][0] - ro.m[0][2];
                axis.v[2] = ro.m[0][1] - ro.m[1][0];
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

        return FastMath.toDegrees(angle);
    }

    public void getRotationXYZ(Vec3 store) {
        if(ro.m[0][2] < +1) {
            if(ro.m[0][2] > -1) {
                store.v[1] = (float) Math.asin(ro.m[0][2]);
                store.v[0] = (float) Math.atan2(-ro.m[1][2], ro.m[2][2]);
                store.v[2] = (float) Math.atan2(-ro.m[0][1], ro.m[0][0]);
            } else {
                store.v[1] = (float) (-Math.PI/2);
                store.v[0] = (float) (-Math.atan2(ro.m[1][0], ro.m[1][1]));
                store.v[2] = 0f;
            }
        } else {
            store.v[1] = (float) (Math.PI/2);
            store.v[0] = (float) (Math.atan2(ro.m[1][0], ro.m[1][1]));
            store.v[2] = 0f;
        }
    }

    public void getRotationYXZ(Vec3 store) {
        if(ro.m[1][2] < +1) {
            if(ro.m[1][2] > -1) {
                store.v[0] = (float) Math.asin(-ro.m[1][2]);
                store.v[1] = (float) Math.atan2(ro.m[0][2], ro.m[2][2]);
                store.v[2] = (float) Math.atan2(ro.m[1][0], ro.m[1][1]);
            } else {
                // Not a unique solution!
                store.v[0] = (float) (Math.PI/2);
                store.v[1] = (float) -Math.atan2(-ro.m[0][1], ro.m[0][0]);
                store.v[2] = 0;
            }
        } else {
            // Not a unique solution!
            store.v[0] = (float) (-Math.PI/2);
            store.v[1] = (float) Math.atan2(-ro.m[0][1], ro.m[0][0]);
            store.v[2] = 0;
        }
    }
    
    public void getRotationZXY(Vec3 store) {
        // TODO: Not implemented yet!
    }
    
    @Override
    public Mat4 getAffineTransform() {
        return (Mat4) at;
    }

    public void translateTo(float x, float y, float z) {
        ((Vec3) tr).setTo(x, y, z);
    }

    public void translateBy(float dx, float dy, float dz) {
        ((Vec3) tr).add(dx, dy, dz);
    }

    private void rotate(float angle, float x, float y, float z, Mat3 store) {
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

        store.m[0][0] = x2*oneMinusCos + cos;
        store.m[0][1] = xym - zSin;
        store.m[0][2] = xzm + ySin;
        store.m[1][0] = xym + zSin;
        store.m[1][1] = y2*oneMinusCos + cos;
        store.m[1][2] = yzm - xSin;
        store.m[2][0] = xzm - ySin;
        store.m[2][1] = yzm + xSin;
        store.m[2][2] = z2*oneMinusCos + cos;
    }
    
    public void rotateTo(float angle, float x, float y, float z) {
        rotate(angle, x, y, z, (Mat3) ro);
    }

    public void rotateTo(float angle, Vec3 axis) {
        rotate(angle, axis.x(), axis.y(), axis.z(), (Mat3) ro);
    }
    
    public void rotateBy(float angle, float x, float y, float z) {
        // store the rotation delta in a temporary matrix
        rotate(angle, x, y, z, tmpM3);
        
        // multiply rotation delta with current rotation matix
        ro.mult(tmpM3);
    }
    
    public void rotateBy(float angle, Vec3 axis) {
        // store the rotation delta in a temporary matrix
        rotate(angle, axis.x(), axis.y(), axis.z(), tmpM3);
        
        // multiply rotation delta with current rotation matix
        ro.mult(tmpM3);
    }

    private void rotateXYZ(float pitch, float yaw, float roll, Mat3 store) {
        /* 
         * The conversion from Euler angles to rotation matrix is calculated as 
         * follows: 
         * 
         * The rotation matrices for each axis
         *   i.e.
         *          |   1       0       0   |
         *        X=|   0     cos(x) -sin(x)|
         *          |   0     sin(x)  cos(x)|
         * 
         *          | cos(y)    0     sin(y)|
         *        Y=|   0       1       0   |
         *          |-sin(y)    0     cos(y)|
         * 
         *          | cos(z) -sin(z)    0   |
         *        Z=| sin(z)  cos(z)    0   |
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
        store.m[0][0] = cy*cr;
        store.m[0][1] = cy*-sr;
        store.m[0][2] = sy;
        store.m[1][0] = -sp*-sy*cr + cp*sr;
        store.m[1][1] = -sp*-sy*-sr + cp*cr;
        store.m[1][2] = -sp*cy;
        store.m[2][0] = cp*-sy*cr + sp*sr;
        store.m[2][1] = cp*-sy*-sr + sp*cr;
        store.m[2][2] = cp*cy;
    }
    
    public void rotateToXYZ(float pitch, float yaw, float roll) {
        rotateXYZ(pitch, yaw, roll, (Mat3) ro);
    }

    public void rotateToXYZ(Vec3 rot) {
        rotateXYZ(rot.x(), rot.y(), rot.z(), (Mat3) ro);
    }

    public void rotateByXYZ(float pitch, float yaw, float roll) {
        // store the rotation delta in a temporary matrix
        rotateXYZ(pitch, yaw, roll, tmpM3);
        
        // multiply rotation delta with current rotation matix
        ro.mult(tmpM3);
    }
    
    public void rotateByXYZ(Vec3 rot) {
        // store the rotation delta in a temporary matrix
        rotateXYZ(rot.x(), rot.y(), rot.z(), tmpM3);
        
        // multiply rotation delta with current rotation matix
        ro.mult(tmpM3);
    }
    
    private void rotateYXZ(float pitch, float yaw, float roll, Mat3 store) {
        /* 
         * The conversion from Euler angles to rotation matrix is calculated as 
         * follows: 
         * 
         * The rotation matrices for each axis
         *   i.e.
         *          |   1       0       0   |
         *        X=|   0     cos(x) -sin(x)|
         *          |   0     sin(x)  cos(x)|
         * 
         *          | cos(y)    0     sin(y)|
         *        Y=|   0       1       0   |
         *          |-sin(y)    0     cos(y)|
         * 
         *          | cos(z) -sin(z)    0   |
         *        Z=| sin(z)  cos(z)    0   |
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

        // rotate around Y then X then Z
        store.m[0][0] = cy*cr + sy*sp*sr;
        store.m[0][1] = cy*-sr + sy*sp*cr;
        store.m[0][2] = sy*cp;
        store.m[1][0] = cp*sr;
        store.m[1][1] = cp*cr;
        store.m[1][2] = -sp;
        store.m[2][0] = -sy*cr + cy*sp*sr;
        store.m[2][1] = -sy*-sr + cy*sp*cr;
        store.m[2][2] = cy*cp;
    }
    
    public void rotateToYXZ(float pitch, float yaw, float roll) {
        rotateYXZ(pitch, yaw, roll, (Mat3) ro);
    }

    public void rotateToYXZ(Vec3 rot) {
        rotateYXZ(rot.x(), rot.y(), rot.z(), (Mat3) ro);
    }

    public void rotateByYXZ(float pitch, float yaw, float roll) {
        // store the rotation delta in a temporary matrix
        rotateYXZ(pitch, yaw, roll, tmpM3);
        
        // multiply rotation delta with current rotation matix
        ro.mult(tmpM3);
    }
    
    public void rotateByYXZ(Vec3 rot) {
        // store the rotation delta in a temporary matrix
        rotateYXZ(rot.x(), rot.y(), rot.z(), tmpM3);
        
        // multiply rotation delta with current rotation matix
        ro.mult(tmpM3);
    }
    
    private void rotateZXY(float pitch, float yaw, float roll, Mat3 store) {
        /* 
         * The conversion from Euler angles to rotation matrix is calculated as 
         * follows: 
         * 
         * The rotation matrices for each axis
         *   i.e.
         *          |   1       0       0   |
         *        X=|   0     cos(x) -sin(x)|
         *          |   0     sin(x)  cos(x)|
         * 
         *          | cos(y)    0     sin(y)|
         *        Y=|   0       1       0   |
         *          |-sin(y)    0     cos(y)|
         * 
         *          | cos(z) -sin(z)    0   |
         *        Z=| sin(z)  cos(z)    0   |
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
        store.m[0][0] = cr*cy + -sr*-sp*-sy;
        store.m[0][1] = -sr*cp;
        store.m[0][2] = cr*sy + -sr*-sp*cy;
        store.m[1][0] = sr*cy + cr*-sp*-sy;
        store.m[1][1] = cr*cp;
        store.m[1][2] = sr*sy + cr*-sp*cy;
        store.m[2][0] = cp*-sy;
        store.m[2][1] = sp;
        store.m[2][2] = cp*cy;
    }
    
    public void rotateToZXY(float pitch, float yaw, float roll) {
        rotateZXY(pitch, yaw, roll, (Mat3) ro);
    }
    
    public void rotateToZXY(Vec3 rot) {
        rotateZXY(rot.x(), rot.y(), rot.z(), (Mat3) ro);
    }
    
    public void rotateByZXY(float pitch, float yaw, float roll) {
        // store the rotation delta in a temporary matrix
        rotateZXY(pitch, yaw, roll, tmpM3);
        
        // multiply rotation delta with current rotation matix
        ro.mult(tmpM3);
    }
    
    public void rotateByZXY(Vec3 rot) {
        // store the rotation delta in a temporary matrix
        rotateZXY(rot.x(), rot.y(), rot.z(), tmpM3);
        
        // multiply rotation delta with current rotation matix
        ro.mult(tmpM3);
    }
    
    @Override
    public void update() {
        ((Mat4) at).setTo(
                ro.m[0][0]*sc, ro.m[0][1]*sc, ro.m[0][2]*sc, tr.v[0],
                ro.m[1][0]*sc, ro.m[1][1]*sc, ro.m[1][2]*sc, tr.v[1],
                ro.m[2][0]*sc, ro.m[2][1]*sc, ro.m[2][2]*sc, tr.v[2],
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void applyInverse(Vec vec) {
        tmpM.setTo(at);
        ((Mat4) tmpM).invert();

        tmpM.mult(vec);
    }

}
