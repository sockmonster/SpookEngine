package com.spookedengine.math;

/**
 *
 * @author Oliver Winks
 */
public class Mat3 extends Mat {

    public Mat3() {
        super(3);
    }

    public Mat3(Mat3 mat) {
        super(mat);
    }

    public Mat3(float[][] m) {
        super(3);

        super.setTo(m);
    }

    @Override
    public Mat3 setTo(Mat mat) {
        super.setTo(mat);

        return this;
    }

    @Override
    public Mat3 setTo(float[][] m) {
        super.setTo(m);

        return this;
    }

    public Mat3 setTo(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        m[0][0]=m00; m[0][1]=m01; m[0][2]=m02;
        m[1][0]=m10; m[1][1]=m11; m[1][2]=m12;
        m[2][0]=m20; m[2][1]=m21; m[2][2]=m22;

        return this;
    }

    @Override
    public Mat3 toZeros() {
        super.toZeros();

        return this;
    }

    @Override
    public Mat3 toOnes() {
        super.toOnes();

        return this;
    }

    @Override
    public Mat3 toIdentity() {
        super.toIdentity();

        return this;
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

    @Override
    public Mat3 add(Mat mat) {
        super.add(mat);

        return this;
    }

    @Override
    public Mat3 add(float[][] m) {
        super.add(m);

        return this;
    }

    public Mat3 add(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        m[0][0]+=m00; m[0][1]+=m01; m[0][2]+=m02;
        m[1][0]+=m10; m[1][1]+=m11; m[1][2]+=m12;
        m[2][0]+=m20; m[2][1]+=m21; m[2][2]+=m22;

        return this;
    }

    @Override
    public Mat3 sub(Mat mat) {
        super.sub(mat);

        return this;
    }

    @Override
    public Mat3 sub(float[][] m) {
        super.sub(m);

        return this;
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

    @Override
    public Mat3 mult(float s) {
        super.mult(s);

        return this;
    }

    @Override
    public Mat3 mult(Mat mat) {
        super.mult(mat);

        return this;
    }

    @Override
    public Mat3 mult(float[][] m) {
        super.mult(m);

        return this;
    }

    public Mat3 mult(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        tmpM[0][0] = m[0][0]*m00 + m[0][1]*m10 + m[0][2]*m20;
        tmpM[0][1] = m[0][0]*m01 + m[0][1]*m11 + m[0][2]*m21;
        tmpM[0][2] = m[0][0]*m02 + m[0][1]*m12 + m[0][2]*m22;

        tmpM[1][0] = m[1][0]*m00 + m[1][1]*m10 + m[1][2]*m20;
        tmpM[1][1] = m[1][0]*m01 + m[1][1]*m11 + m[1][2]*m21;
        tmpM[1][2] = m[1][0]*m02 + m[1][1]*m12 + m[1][2]*m22;

        tmpM[2][0] = m[2][0]*m00 + m[2][1]*m10 + m[2][2]*m20;
        tmpM[2][1] = m[2][0]*m01 + m[2][1]*m11 + m[2][2]*m21;
        tmpM[2][2] = m[2][0]*m02 + m[2][1]*m12 + m[2][2]*m22;

        // set matrix
        super.setTo(tmpM);

        return this;
    }

    @Override
    public void mult(Vec vec) {
        if(vec instanceof Vec2) {
            float x = m[0][0]*vec.v[0] + m[0][1]*vec.v[1] + m[0][2];
            float y = m[1][0]*vec.v[0] + m[1][1]*vec.v[1] + m[1][2];

            vec.v[0] = x;
            vec.v[1] = y;
        } else if(vec instanceof Vec3) {
            float x = m[0][0]*vec.v[0] + m[0][1]*vec.v[1] + m[0][2]*vec.v[2];
            float y = m[1][0]*vec.v[0] + m[1][1]*vec.v[1] + m[1][2]*vec.v[2];
            float z = m[2][0]*vec.v[0] + m[2][1]*vec.v[1] + m[2][2]*vec.v[2];

            vec.v[0] = x;
            vec.v[1] = y;
            vec.v[2] = z;
        } else {
            throw new IllegalArgumentException("Only 2 or 3 dimensional " +
                    "vectors can be multiplied through a Matrix3");
        }
    }

    @Override
    public Mat3 div(float s) {
        super.div(s);

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
            super.toZeros();
            
            return this;
        }

        tmpM[0][0] = m[1][1]*m[2][2] - m[1][2]*m[2][1];
        tmpM[0][1] = m[0][2]*m[2][1] - m[0][1]*m[2][2];
        tmpM[0][2] = m[0][1]*m[1][2] - m[0][2]*m[1][1];
        tmpM[1][0] = m[1][2]*m[2][0] - m[1][0]*m[2][2];
        tmpM[1][1] = m[0][0]*m[2][2] - m[0][2]*m[2][0];
        tmpM[1][2] = m[0][2]*m[1][0] - m[0][0]*m[1][2];
        tmpM[2][0] = m[1][0]*m[2][1] - m[1][1]*m[2][0];
        tmpM[2][1] = m[0][1]*m[2][0] - m[0][0]*m[2][1];
        tmpM[2][2] = m[0][0]*m[1][1] - m[0][1]*m[1][0];

        float one_over_det = 1.0f/det;
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                m[r][c] = tmpM[r][c]*one_over_det;
        }

        return this;
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

}
