package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Mat4 {
    
    // convenience vars
    private final int dim = 4;
    private Mat4 tmpM;
    
    public final float[][] m = new float[dim][dim];

    public Mat4() {
    }

    public Mat4(Mat4 mat) {
        tmpM = new Mat4();
        setTo(
                mat.m[0][0], mat.m[0][1], mat.m[0][2], mat.m[0][3],
                mat.m[1][0], mat.m[1][1], mat.m[1][2], mat.m[1][3],
                mat.m[2][0], mat.m[2][1], mat.m[2][2], mat.m[2][3],
                mat.m[3][0], mat.m[3][1], mat.m[3][2], mat.m[3][3]);
    }

    public Mat4(float[][] m) {
        setTo(
                m[0][0], m[0][1], m[0][2], m[0][3],
                m[1][0], m[1][1], m[1][2], m[1][3],
                m[2][0], m[2][1], m[2][2], m[2][3],
                m[3][0], m[3][1], m[3][2], m[3][3]);
    }
    
    public Mat4(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        setTo(
                m00, m01, m02, m03, 
                m10, m11, m12, m13, 
                m20, m21, m22, m23, 
                m30, m31, m32, m33);
    }
    
    /* ******** ******** ******** */
    /*          SETTERS           */
    /* ******** ******** ******** */
    public final Mat4 setTo(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        m[0][0]=m00; m[0][1]=m01; m[0][2]=m02; m[0][3]=m03;
        m[1][0]=m10; m[1][1]=m11; m[1][2]=m12; m[1][3]=m13;
        m[2][0]=m20; m[2][1]=m21; m[2][2]=m22; m[2][3]=m23;
        m[3][0]=m30; m[3][1]=m31; m[3][2]=m32; m[3][3]=m33;

        return this;
    }
    
    public Mat4 setTo(float[][] m) {
        return setTo(
                m[0][0], m[0][1], m[0][2], m[0][3],
                m[1][0], m[1][1], m[1][2], m[1][3],
                m[2][0], m[2][1], m[2][2], m[2][3],
                m[3][0], m[3][1], m[3][2], m[3][3]);
    }
    
    public Mat4 setTo(Mat4 mat) {
        return setTo(
                mat.m[0][0], mat.m[0][1], mat.m[0][2], mat.m[0][3],
                mat.m[1][0], mat.m[1][1], mat.m[1][2], mat.m[1][3],
                mat.m[2][0], mat.m[2][1], mat.m[2][2], mat.m[2][3],
                mat.m[3][0], mat.m[3][1], mat.m[3][2], mat.m[3][3]);
    }
    
    public Mat4 toZeros() {
        return setTo(
                0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    public Mat4 toOnes() {
        return setTo(
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public Mat4 toIdentity() {
        return setTo(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
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
        M[ 0]=m[0][0]; M[ 1]=m[1][0]; M[ 2]=m[2][0]; M[ 3]=m[3][0];
        M[ 4]=m[0][1]; M[ 5]=m[1][1]; M[ 6]=m[2][1]; M[ 7]=m[3][1];
        M[ 8]=m[0][2]; M[ 9]=m[1][2]; M[10]=m[2][2]; M[11]=m[3][2];
        M[12]=m[0][3]; M[13]=m[1][3]; M[14]=m[2][3]; M[15]=m[3][3];
    }
    
    /* ******** ******** ******** */
    /*         OPERATORS          */
    /* ******** ******** ******** */
    public Mat4 add(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        m[0][0]+=m00; m[0][1]+=m01; m[0][2]+=m02; m[0][3]+=m03;
        m[1][0]+=m10; m[1][1]+=m11; m[1][2]+=m12; m[1][3]+=m13;
        m[2][0]+=m20; m[2][1]+=m21; m[2][2]+=m22; m[2][3]+=m23;
        m[3][0]+=m30; m[3][1]+=m31; m[3][2]+=m32; m[3][3]+=m33;

        return this;
    }
    
    public Mat4 add(float[][] m) {
        return add(
                m[0][0], m[0][1], m[0][2], m[0][3],
                m[1][0], m[1][1], m[1][2], m[1][3],
                m[2][0], m[2][1], m[2][2], m[2][3],
                m[3][0], m[3][1], m[3][2], m[3][3]);
    }
    
    public Mat4 add(Mat4 mat) {
        return add(
                mat.m[0][0], mat.m[0][1], mat.m[0][2], mat.m[0][3],
                mat.m[1][0], mat.m[1][1], mat.m[1][2], mat.m[1][3],
                mat.m[2][0], mat.m[2][1], mat.m[2][2], mat.m[2][3],
                mat.m[3][0], mat.m[3][1], mat.m[3][2], mat.m[3][3]);
    }
    
    public Mat4 sub(float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        m[0][0]-=m00; m[0][1]-=m01; m[0][2]-=m02; m[0][3]-=m03;
        m[1][0]-=m10; m[1][1]-=m11; m[1][2]-=m12; m[1][3]-=m13;
        m[2][0]-=m20; m[2][1]-=m21; m[2][2]-=m22; m[2][3]-=m23;
        m[3][0]-=m30; m[3][1]-=m31; m[3][2]-=m32; m[3][3]-=m33;

        return this;
    }
    
    public Mat4 sub(float[][] m) {
        return sub(
                m[0][0], m[0][1], m[0][2], m[0][3],
                m[1][0], m[1][1], m[1][2], m[1][3],
                m[2][0], m[2][1], m[2][2], m[2][3],
                m[3][0], m[3][1], m[3][2], m[3][3]);
    }
    
    public Mat4 sub(Mat4 mat) {
        return sub(
                mat.m[0][0], mat.m[0][1], mat.m[0][2], mat.m[0][3],
                mat.m[1][0], mat.m[1][1], mat.m[1][2], mat.m[1][3],
                mat.m[2][0], mat.m[2][1], mat.m[2][2], mat.m[2][3],
                mat.m[3][0], mat.m[3][1], mat.m[3][2], mat.m[3][3]);
    }
    
    public Mat4 mult(float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        if(tmpM == null)
            tmpM = new Mat4();
        
        tmpM.m[0][0] = m[0][0]*m00 + m[0][1]*m10 + m[0][2]*m20 + m[0][3]*m30;
        tmpM.m[0][1] = m[0][0]*m01 + m[0][1]*m11 + m[0][2]*m21 + m[0][3]*m31;
        tmpM.m[0][2] = m[0][0]*m02 + m[0][1]*m12 + m[0][2]*m22 + m[0][3]*m32;
        tmpM.m[0][3] = m[0][0]*m03 + m[0][1]*m13 + m[0][2]*m23 + m[0][3]*m33;

        tmpM.m[1][0] = m[1][0]*m00 + m[1][1]*m10 + m[1][2]*m20 + m[1][3]*m30;
        tmpM.m[1][1] = m[1][0]*m01 + m[1][1]*m11 + m[1][2]*m21 + m[1][3]*m31;
        tmpM.m[1][2] = m[1][0]*m02 + m[1][1]*m12 + m[1][2]*m22 + m[1][3]*m32;
        tmpM.m[1][3] = m[1][0]*m03 + m[1][1]*m13 + m[1][2]*m23 + m[1][3]*m33;

        tmpM.m[2][0] = m[2][0]*m00 + m[2][1]*m10 + m[2][2]*m20 + m[2][3]*m30;
        tmpM.m[2][1] = m[2][0]*m01 + m[2][1]*m11 + m[2][2]*m21 + m[2][3]*m31;
        tmpM.m[2][2] = m[2][0]*m02 + m[2][1]*m12 + m[2][2]*m22 + m[2][3]*m32;
        tmpM.m[2][3] = m[2][0]*m03 + m[2][1]*m13 + m[2][2]*m23 + m[2][3]*m33;

        tmpM.m[3][0] = m[3][0]*m00 + m[3][1]*m10 + m[3][2]*m20 + m[3][3]*m30;
        tmpM.m[3][1] = m[3][0]*m01 + m[3][1]*m11 + m[3][2]*m21 + m[3][3]*m31;
        tmpM.m[3][2] = m[3][0]*m02 + m[3][1]*m12 + m[3][2]*m22 + m[3][3]*m32;
        tmpM.m[3][3] = m[3][0]*m03 + m[3][1]*m13 + m[3][2]*m23 + m[3][3]*m33;

        // set matrix
        setTo(tmpM);

        return this;
    }
    
    public Mat4 mult(float[][] m) {
        return mult(
                m[0][0], m[0][1], m[0][2], m[0][3],
                m[1][0], m[1][1], m[1][2], m[1][3],
                m[2][0], m[2][1], m[2][2], m[2][3],
                m[3][0], m[3][1], m[3][2], m[3][3]);
    }
    
    public Mat4 mult(Mat4 mat) {
        return mult(
                mat.m[0][0], mat.m[0][1], mat.m[0][2], mat.m[0][3],
                mat.m[1][0], mat.m[1][1], mat.m[1][2], mat.m[1][3],
                mat.m[2][0], mat.m[2][1], mat.m[2][2], mat.m[2][3],
                mat.m[3][0], mat.m[3][1], mat.m[3][2], mat.m[3][3]);
    }
    
    public Mat4 mult(float s) {
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                m[r][c] *= s;
        }

        return this;
    }
    
    public void mult(Vec3 vec) {
        float x = m[0][0]*vec.v[0] + m[0][1]*vec.v[1] + m[0][2]*vec.v[2] + m[0][3];
        float y = m[1][0]*vec.v[0] + m[1][1]*vec.v[1] + m[1][2]*vec.v[2] + m[1][3];
        float z = m[2][0]*vec.v[0] + m[2][1]*vec.v[1] + m[2][2]*vec.v[2] + m[2][3];

        vec.v[0] = x;
        vec.v[1] = y;
        vec.v[2] = z;
    }
    
    public Mat4 div(float s) {
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
    public Mat4 transpose() {
        if(tmpM == null)
            tmpM = new Mat4();
        
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
        float a0 = m[0][0]*m[1][1] - m[0][1]*m[1][0];
        float a1 = m[0][0]*m[1][2] - m[0][2]*m[1][0];
        float a2 = m[0][0]*m[1][3] - m[0][3]*m[1][0];
        float a3 = m[0][1]*m[1][2] - m[0][2]*m[1][1];
        float a4 = m[0][1]*m[1][3] - m[0][3]*m[1][1];
        float a5 = m[0][2]*m[1][3] - m[0][3]*m[1][2];
        float b0 = m[2][0]*m[3][1] - m[2][1]*m[3][0];
        float b1 = m[2][0]*m[3][2] - m[2][2]*m[3][0];
        float b2 = m[2][0]*m[3][3] - m[2][3]*m[3][0];
        float b3 = m[2][1]*m[3][2] - m[2][2]*m[3][1];
        float b4 = m[2][1]*m[3][3] - m[2][3]*m[3][1];
        float b5 = m[2][2]*m[3][3] - m[2][3]*m[3][2];

        return a0*b5 - a1*b4 + a2*b3 + a3*b2 - a4*b1 + a5*b0;
    }

    public Mat4 invert() {
        float a0 = m[0][0]*m[1][1] - m[0][1]*m[1][0];
        float a1 = m[0][0]*m[1][2] - m[0][2]*m[1][0];
        float a2 = m[0][0]*m[1][3] - m[0][3]*m[1][0];
        float a3 = m[0][1]*m[1][2] - m[0][2]*m[1][1];
        float a4 = m[0][1]*m[1][3] - m[0][3]*m[1][1];
        float a5 = m[0][2]*m[1][3] - m[0][3]*m[1][2];
        float b0 = m[2][0]*m[3][1] - m[2][1]*m[3][0];
        float b1 = m[2][0]*m[3][2] - m[2][2]*m[3][0];
        float b2 = m[2][0]*m[3][3] - m[2][3]*m[3][0];
        float b3 = m[2][1]*m[3][2] - m[2][2]*m[3][1];
        float b4 = m[2][1]*m[3][3] - m[2][3]*m[3][1];
        float b5 = m[2][2]*m[3][3] - m[2][3]*m[3][2];
        float det = a0*b5 - a1*b4 + a2*b3 + a3*b2 - a4*b1 + a5*b0;

        if(FastMath.abs(det) <= 0) {
            toZeros();
            
            return this;
        }
        
        if(tmpM == null)
            tmpM = new Mat4();
        
        tmpM.m[0][0] = + m[1][1]*b5 - m[1][2]*b4 + m[1][3]*b3;
        tmpM.m[1][0] = - m[1][0]*b5 + m[1][2]*b2 - m[1][3]*b1;
        tmpM.m[2][0] = + m[1][0]*b4 - m[1][1]*b2 + m[1][3]*b0;
        tmpM.m[3][0] = - m[1][0]*b3 + m[1][1]*b1 - m[1][2]*b0;
        tmpM.m[0][1] = - m[0][1]*b5 + m[0][2]*b4 - m[0][3]*b3;
        tmpM.m[1][1] = + m[0][0]*b5 - m[0][2]*b2 + m[0][3]*b1;
        tmpM.m[2][1] = - m[0][0]*b4 + m[0][1]*b2 - m[0][3]*b0;
        tmpM.m[3][1] = + m[0][0]*b3 - m[0][1]*b1 + m[0][2]*b0;
        tmpM.m[0][2] = + m[3][1]*a5 - m[3][2]*a4 + m[3][3]*a3;
        tmpM.m[1][2] = - m[3][0]*a5 + m[3][2]*a2 - m[3][3]*a1;
        tmpM.m[2][2] = + m[3][0]*a4 - m[3][1]*a2 + m[3][3]*a0;
        tmpM.m[3][2] = - m[3][0]*a3 + m[3][1]*a1 - m[3][2]*a0;
        tmpM.m[0][3] = - m[2][1]*a5 + m[2][2]*a4 - m[2][3]*a3;
        tmpM.m[1][3] = + m[2][0]*a5 - m[2][2]*a2 + m[2][3]*a1;
        tmpM.m[2][3] = - m[2][0]*a4 + m[2][1]*a2 - m[2][3]*a0;
        tmpM.m[3][3] = + m[2][0]*a3 - m[2][1]*a1 + m[2][2]*a0;

        float one_over_det = 1.0f/det;
        setTo(tmpM);
        mult(one_over_det);

        return this;
    }
    
    /* ******** ******** ******** */
    /*         ROTATIONS          */
    /* ******** ******** ******** */
    // TODO: COPY FROM MAT3
    
    @Override
    public boolean equals(Object mat) {
        if(mat instanceof Mat4) {
            for(int r=0; r<dim; r++) {
                for(int c=0; c<dim; c++) {
                    if(m[r][c] != ((Mat4) mat).m[r][c])
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
