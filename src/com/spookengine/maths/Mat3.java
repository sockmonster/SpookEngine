package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Mat3 {
    
    // convenience vars
    private final int dim = 3;
    private Mat3 tmpM;
    
    public final float[][] m = new float[dim][dim];
    
    public Mat3() {
    }

    public Mat3(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        setTo(
                m00, m01, m02, 
                m10, m11, m12, 
                m20, m21, m22);
    }
    
    public Mat3(float[][] m) {
        setTo(
                m[0][0], m[0][1], m[0][2],
                m[1][0], m[1][1], m[1][2],
                m[2][0], m[2][1], m[2][2]);
    }
    
    public Mat3(Mat3 mat) {
        setTo(
                mat.m[0][0], mat.m[0][1], mat.m[0][2],
                mat.m[1][0], mat.m[1][1], mat.m[1][2],
                mat.m[2][0], mat.m[2][1], mat.m[2][2]);
    }
    
    /* ******** ******** ******** */
    /*          SETTERS           */
    /* ******** ******** ******** */
    public final Mat3 setTo(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        m[0][0]=m00; m[0][1]=m01; m[0][2]=m02;
        m[1][0]=m10; m[1][1]=m11; m[1][2]=m12;
        m[2][0]=m20; m[2][1]=m21; m[2][2]=m22;

        return this;
    }
    
    public Mat3 setTo(Mat3 mat) {
        return setTo(
                mat.m[0][0], mat.m[0][1], mat.m[0][2],
                mat.m[1][0], mat.m[1][1], mat.m[1][2],
                mat.m[2][0], mat.m[2][1], mat.m[2][2]);
    }
    
    public Mat3 setTo(float[][] m) {
        return setTo(
                m[0][0], m[0][1], m[0][2],
                m[1][0], m[1][1], m[1][2],
                m[2][0], m[2][1], m[2][2]);
    }
    
    public Mat3 toZeros() {
        return setTo(
                0, 0, 0, 
                0, 0, 0, 
                0, 0, 0);
    }
    
    public Mat3 toOnes() {
        return setTo(
                1, 1, 1, 
                1, 1, 1, 
                1, 1, 1);
    }
    
    public Mat3 toIdentity() {
        return setTo(
                1, 0, 0, 
                0, 1, 0, 
                0, 0, 1);
    }

    /**
     * Assuming this is an affine transform matrix (i.e. a matrix that defines
     * rotation, scale and translation), this method will returns a matrix
     * which is compatible with OpenGL (i.e. a 4x4, transposed view of this
     * matrix). The original matrix is untouched.
     *
     * This method is used to convert a 2D affine transform matrix into a form
     * that is compatible with the OpenGL methods.
     *
     * @param M A 16 item array which is used to store a 4x4 view of this
     * matrix.
     */
    public void toOpenGL(float[] M) {
        M[ 0]=m[0][0]; M[ 1]=m[1][0]; M[ 2]=0.0f; M[ 3]=0.0f;
        M[ 4]=m[0][1]; M[ 5]=m[1][1]; M[ 6]=0.0f; M[ 7]=0.0f;
        M[ 8]=0.0f;    M[ 9]=0.0f;    M[10]=1.0f; M[11]=0.0f;
        M[12]=m[0][2]; M[13]=m[1][2]; M[14]=0.0f; M[15]=1.0f;
    }
    
    /**
     * Reconstructs this Mat3 using the given axis.
     *
     * @param uAxis Vector3
     * @param vAxis Vector3
     * @param wAxis Vector3
     */
    public Mat3 fromAxes(Vec3 uAxis, Vec3 vAxis, Vec3 wAxis) {
        m[0][0] = uAxis.v[0];
        m[1][0] = uAxis.v[1];
        m[2][0] = uAxis.v[2];

        m[0][1] = vAxis.v[0];
        m[1][1] = vAxis.v[1];
        m[2][1] = vAxis.v[2];

        m[0][2] = wAxis.v[0];
        m[1][2] = wAxis.v[1];
        m[2][2] = wAxis.v[2];

        return this;
    }
    
    /* ******** ******** ******** */
    /*         OPERATORS          */
    /* ******** ******** ******** */
    public Mat3 add(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        m[0][0]+=m00; m[0][1]+=m01; m[0][2]+=m02;
        m[1][0]+=m10; m[1][1]+=m11; m[1][2]+=m12;
        m[2][0]+=m20; m[2][1]+=m21; m[2][2]+=m22;

        return this;
    }
    
    public Mat3 add(Mat3 mat) {
        return add(
                mat.m[0][0], mat.m[0][1], mat.m[0][2],
                mat.m[1][0], mat.m[1][1], mat.m[1][2],
                mat.m[2][0], mat.m[2][1], mat.m[2][2]);
    }
    
    public Mat3 add(float[][] m) {
        return add(
                m[0][0], m[0][1], m[0][2],
                m[1][0], m[1][1], m[1][2],
                m[2][0], m[2][1], m[2][2]);
    }
    
    public Mat3 sub(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        m[0][0]-=m00; m[0][1]-=m01; m[0][2]-=m02;
        m[1][0]-=m10; m[1][1]-=m11; m[1][2]-=m12;
        m[2][0]-=m20; m[2][1]-=m21; m[2][2]-=m22;

        return this;
    }
    
    public Mat3 sub(float[][] m) {
        return sub(
                m[0][0], m[0][1], m[0][2],
                m[1][0], m[1][1], m[1][2],
                m[2][0], m[2][1], m[2][2]);
    }
    
    public Mat3 sub(Mat3 mat) {
        return sub(
                mat.m[0][0], mat.m[0][1], mat.m[0][2],
                mat.m[1][0], mat.m[1][1], mat.m[1][2],
                mat.m[2][0], mat.m[2][1], mat.m[2][2]);
    }
    
    public Mat3 mult(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        if(tmpM == null)
            tmpM = new Mat3();
        
        tmpM.m[0][0] = m[0][0]*m00 + m[0][1]*m10 + m[0][2]*m20;
        tmpM.m[0][1] = m[0][0]*m01 + m[0][1]*m11 + m[0][2]*m21;
        tmpM.m[0][2] = m[0][0]*m02 + m[0][1]*m12 + m[0][2]*m22;

        tmpM.m[1][0] = m[1][0]*m00 + m[1][1]*m10 + m[1][2]*m20;
        tmpM.m[1][1] = m[1][0]*m01 + m[1][1]*m11 + m[1][2]*m21;
        tmpM.m[1][2] = m[1][0]*m02 + m[1][1]*m12 + m[1][2]*m22;

        tmpM.m[2][0] = m[2][0]*m00 + m[2][1]*m10 + m[2][2]*m20;
        tmpM.m[2][1] = m[2][0]*m01 + m[2][1]*m11 + m[2][2]*m21;
        tmpM.m[2][2] = m[2][0]*m02 + m[2][1]*m12 + m[2][2]*m22;

        // set matrix
        setTo(tmpM);

        return this;
    }
    
    public Mat3 mult(float[][] m) {
        return mult(
                m[0][0], m[0][1], m[0][2],
                m[1][0], m[1][1], m[1][2],
                m[2][0], m[2][1], m[2][2]);
    }
    
    public Mat3 mult(Mat3 mat) {
        return mult(
                mat.m[0][0], mat.m[0][1], mat.m[0][2],
                mat.m[1][0], mat.m[1][1], mat.m[1][2],
                mat.m[2][0], mat.m[2][1], mat.m[2][2]);
    }
    
    public Mat3 mult(float s) {
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                m[r][c] *= s;
        }

        return this;
    }
    
    public void mult(Vec2 vec) {
        float x = m[0][0]*vec.v[0] + m[0][1]*vec.v[1] + m[0][2];
        float y = m[1][0]*vec.v[0] + m[1][1]*vec.v[1] + m[1][2];

        vec.v[0] = x;
        vec.v[1] = y;
    }
    
    public void mult(Vec3 vec) {
        float x = m[0][0]*vec.v[0] + m[0][1]*vec.v[1] + m[0][2]*vec.v[2];
        float y = m[1][0]*vec.v[0] + m[1][1]*vec.v[1] + m[1][2]*vec.v[2];
        float z = m[2][0]*vec.v[0] + m[2][1]*vec.v[1] + m[2][2]*vec.v[2];

        vec.v[0] = x;
        vec.v[1] = y;
        vec.v[2] = z;
    }
    
    public Mat3 div(float s) {
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                m[r][c] /= s;
        }

        return this;
    }
    
    /**
     * Transposes this matrix.
     *
     * @return this matrix.
     */
    public Mat3 transpose() {
        if(tmpM == null)
            tmpM = new Mat3();
        
        // transpose
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                tmpM.m[r][c] = m[c][r];
        }

        // set the transposed view
        setTo(tmpM);

        return this;
    }

    public float determinant() {
        float t00 = m[1][1]*m[2][2] - m[1][2]*m[2][1];
        float t10 = m[2][1]*m[2][0] - m[1][0]*m[2][2];
        float t20 = m[1][0]*m[2][1] - m[1][1]*m[2][0];
        
        return m[0][0]*t00 + m[0][1]*t10 + m[0][2]*t20;
    }

    public Mat3 invert() {
        float t00 = m[1][1]*m[2][2] - m[1][2]*m[2][1];
        float t10 = m[2][1]*m[2][0] - m[1][0]*m[2][2];
        float t20 = m[1][0]*m[2][1] - m[1][1]*m[2][0];
        float det = m[0][0]*t00 + m[0][1]*t10 + m[0][2]*t20;

        if(FastMath.abs(det) <= 0) {
            toZeros();
            
            return this;
        }
        
        if(tmpM == null)
            tmpM = new Mat3();
        
        tmpM.m[0][0] = m[1][1]*m[2][2] - m[1][2]*m[2][1];
        tmpM.m[0][1] = m[0][2]*m[2][1] - m[0][1]*m[2][2];
        tmpM.m[0][2] = m[0][1]*m[1][2] - m[0][2]*m[1][1];
        tmpM.m[1][0] = m[1][2]*m[2][0] - m[1][0]*m[2][2];
        tmpM.m[1][1] = m[0][0]*m[2][2] - m[0][2]*m[2][0];
        tmpM.m[1][2] = m[0][2]*m[1][0] - m[0][0]*m[1][2];
        tmpM.m[2][0] = m[1][0]*m[2][1] - m[1][1]*m[2][0];
        tmpM.m[2][1] = m[0][1]*m[2][0] - m[0][0]*m[2][1];
        tmpM.m[2][2] = m[0][0]*m[1][1] - m[0][1]*m[1][0];

        float one_over_det = 1.0f/det;
        setTo(tmpM);
        mult(one_over_det);

        return this;
    }
    
    /* ******** ******** ******** */
    /*      ROTATION METHODS      */
    /* ******** ******** ******** */
    public float getRotation(Vec3 axis) {
        float fTrace = m[0][0] + m[1][1] + m[2][2];
        float fCos = 0.5f*(fTrace - 1.0f);
        float angle = (float) Math.acos(fCos);  // in [0,PI]

        if(angle > 0.0f) {
            if(angle < Math.PI) {
                axis.v[0] = m[1][2] - m[2][1];
                axis.v[1] = m[2][0] - m[0][2];
                axis.v[2] = m[0][1] - m[1][0];
                axis.norm();
            } else {
                // angle is PI
                float halfInverse;
                if(m[0][0] >= m[1][1]) {
                    // r00 >= r11
                    if(m[0][0] >= m[2][2]) {
                        // r00 is maximum diagonal term
                        axis.v[0] = 0.5f*(float) Math.sqrt(m[0][0] - m[1][1] - m[2][2] + 1.0f);
                        halfInverse = 0.5f/axis.v[0];
                        axis.v[1] = halfInverse*m[1][0];
                        axis.v[2] = halfInverse*m[2][0];
                    } else {
                        // r22 is maximum diagonal term
                        axis.v[2] = 0.5f*(float) Math.sqrt(m[2][2] - m[0][0] - m[1][1] + 1.0f);
                        halfInverse = 0.5f/axis.v[2];
                        axis.v[0] = halfInverse*m[2][0];
                        axis.v[1] = halfInverse*m[2][1];
                    }
                } else {
                    // r11 > r00
                    if(m[1][1] >= m[2][2]) {
                        // r11 is maximum diagonal term
                        axis.v[1] = 0.5f*(float) Math.sqrt(m[1][1] - m[0][0] - m[2][2] + 1.0f);
                        halfInverse  = 0.5f/axis.v[1];
                        axis.v[0] = halfInverse*m[1][0];
                        axis.v[2] = halfInverse*m[2][1];
                    } else {
                        // r22 is maximum diagonal term
                        axis.v[2] = 0.5f*(float) Math.sqrt(m[2][2] - m[0][0] - m[1][1] + 1.0f);
                        halfInverse = 0.5f/axis.v[2];
                        axis.v[0] = halfInverse*m[2][0];
                        axis.v[1] = halfInverse*m[2][1];
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
        if(m[0][2] < +1) {
            if(m[0][2] > -1) {
                store.v[1] = (float) Math.asin(m[0][2]);
                store.v[0] = (float) Math.atan2(-m[1][2], m[2][2]);
                store.v[2] = (float) Math.atan2(-m[0][1], m[0][0]);
            } else {
                store.v[1] = (float) (-Math.PI/2);
                store.v[0] = (float) (-Math.atan2(m[1][0], m[1][1]));
                store.v[2] = 0f;
            }
        } else {
            store.v[1] = (float) (Math.PI/2);
            store.v[0] = (float) (Math.atan2(m[1][0], m[1][1]));
            store.v[2] = 0f;
        }
    }

    public void getRotationYXZ(Vec3 store) {
        if(m[1][2] < +1) {
            if(m[1][2] > -1) {
                store.v[0] = (float) Math.asin(-m[1][2]);
                store.v[1] = (float) Math.atan2(m[0][2], m[2][2]);
                store.v[2] = (float) Math.atan2(m[1][0], m[1][1]);
            } else {
                // Not a unique solution!
                store.v[0] = (float) (Math.PI/2);
                store.v[1] = (float) -Math.atan2(-m[0][1], m[0][0]);
                store.v[2] = 0;
            }
        } else {
            // Not a unique solution!
            store.v[0] = (float) (-Math.PI/2);
            store.v[1] = (float) Math.atan2(-m[0][1], m[0][0]);
            store.v[2] = 0;
        }
    }
    
    public void getRotationZXY(Vec3 store) {
        // TODO: Not implemented yet!
    }

    private void rotate(float angle, float x, float y, float z) {
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

        m[0][0] = x2*oneMinusCos + cos;
        m[0][1] = xym - zSin;
        m[0][2] = xzm + ySin;
        m[1][0] = xym + zSin;
        m[1][1] = y2*oneMinusCos + cos;
        m[1][2] = yzm - xSin;
        m[2][0] = xzm - ySin;
        m[2][1] = yzm + xSin;
        m[2][2] = z2*oneMinusCos + cos;
    }
    
    public void rotateTo(float angle, float x, float y, float z) {
        rotate(angle, x, y, z);
    }

    public void rotateTo(float angle, Vec3 axis) {
        rotate(angle, axis.x(), axis.y(), axis.z());
    }
    
    public void rotateBy(float angle, float x, float y, float z) {
        if(tmpM == null)
            tmpM = new Mat3();
        
        // store the rotation delta in a temporary matrix
        tmpM.rotate(angle, x, y, z);
        
        // multiply rotation delta with current rotation matix
        mult(tmpM);
    }
    
    public void rotateBy(float angle, Vec3 axis) {
        if(tmpM == null)
            tmpM = new Mat3();
        
        // store the rotation delta in a temporary matrix
        tmpM.rotate(angle, axis.x(), axis.y(), axis.z());
        
        // multiply rotation delta with current rotation matix
        mult(tmpM);
    }

    private void rotateXYZ(float pitch, float yaw, float roll) {
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
        m[0][0] = cy*cr;
        m[0][1] = cy*-sr;
        m[0][2] = sy;
        m[1][0] = -sp*-sy*cr + cp*sr;
        m[1][1] = -sp*-sy*-sr + cp*cr;
        m[1][2] = -sp*cy;
        m[2][0] = cp*-sy*cr + sp*sr;
        m[2][1] = cp*-sy*-sr + sp*cr;
        m[2][2] = cp*cy;
    }
    
    public void rotateToXYZ(float pitch, float yaw, float roll) {
        rotateXYZ(pitch, yaw, roll);
    }

    public void rotateToXYZ(Vec3 rot) {
        rotateXYZ(rot.x(), rot.y(), rot.z());
    }

    public void rotateByXYZ(float pitch, float yaw, float roll) {
        if(tmpM == null)
            tmpM = new Mat3();
        
        // store the rotation delta in a temporary matrix
        tmpM.rotateXYZ(pitch, yaw, roll);
        
        // multiply rotation delta with current rotation matix
        mult(tmpM);
    }
    
    public void rotateByXYZ(Vec3 rot) {
        if(tmpM == null)
            tmpM = new Mat3();
        
        // store the rotation delta in a temporary matrix
        tmpM.rotateByXYZ(rot.x(), rot.y(), rot.z());
        
        // multiply rotation delta with current rotation matix
        mult(tmpM);
    }
    
    private void rotateYXZ(float pitch, float yaw, float roll) {
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
        m[0][0] = cy*cr + sy*sp*sr;
        m[0][1] = cy*-sr + sy*sp*cr;
        m[0][2] = sy*cp;
        m[1][0] = cp*sr;
        m[1][1] = cp*cr;
        m[1][2] = -sp;
        m[2][0] = -sy*cr + cy*sp*sr;
        m[2][1] = -sy*-sr + cy*sp*cr;
        m[2][2] = cy*cp;
    }
    
    public void rotateToYXZ(float pitch, float yaw, float roll) {
        rotateYXZ(pitch, yaw, roll);
    }

    public void rotateToYXZ(Vec3 rot) {
        rotateYXZ(rot.x(), rot.y(), rot.z());
    }

    public void rotateByYXZ(float pitch, float yaw, float roll) {
        if(tmpM == null)
            tmpM = new Mat3();
        
        // store the rotation delta in a temporary matrix
        tmpM.rotateYXZ(pitch, yaw, roll);
        
        // multiply rotation delta with current rotation matix
        mult(tmpM);
    }
    
    public void rotateByYXZ(Vec3 rot) {
        if(tmpM == null)
            tmpM = new Mat3();
        
        // store the rotation delta in a temporary matrix
        tmpM.rotateYXZ(rot.x(), rot.y(), rot.z());
        
        // multiply rotation delta with current rotation matix
        mult(tmpM);
    }
    
    private void rotateZXY(float pitch, float yaw, float roll) {
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
        m[0][0] = cr*cy + -sr*-sp*-sy;
        m[0][1] = -sr*cp;
        m[0][2] = cr*sy + -sr*-sp*cy;
        m[1][0] = sr*cy + cr*-sp*-sy;
        m[1][1] = cr*cp;
        m[1][2] = sr*sy + cr*-sp*cy;
        m[2][0] = cp*-sy;
        m[2][1] = sp;
        m[2][2] = cp*cy;
    }
    
    public void rotateToZXY(float pitch, float yaw, float roll) {
        rotateZXY(pitch, yaw, roll);
    }
    
    public void rotateToZXY(Vec3 rot) {
        rotateZXY(rot.x(), rot.y(), rot.z());
    }
    
    public void rotateByZXY(float pitch, float yaw, float roll) {
        if(tmpM == null)
            tmpM = new Mat3();
        
        // store the rotation delta in a temporary matrix
        tmpM.rotateZXY(pitch, yaw, roll);
        
        // multiply rotation delta with current rotation matix
        mult(tmpM);
    }
    
    public void rotateByZXY(Vec3 rot) {
        if(tmpM == null)
            tmpM = new Mat3();
        
        // store the rotation delta in a temporary matrix
        tmpM.rotateZXY(rot.x(), rot.y(), rot.z());
        
        // multiply rotation delta with current rotation matix
        mult(tmpM);
    }
    
    @Override
    public boolean equals(Object mat) {
        if(mat instanceof Mat3) {
            for(int r=0; r<dim; r++) {
                for(int c=0; c<dim; c++) {
                    if(m[r][c] != ((Mat3) mat).m[r][c])
                        return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        String str = "";
        for(int r=0; r<dim; r++) {
            str += "[ ";
            for(int c=0; c<dim; c++) {
                str += m[r][c] + " ";
            }
            str += "]" + '\n';
        }

        return str;
    }

}
