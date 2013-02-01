package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Mat3 {
    
    // convenience vars
    private final int dim = 3;
    private Mat3 tmpM;
    private Mat3 tmpRot;
    
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
        float a = m[0][0]; float b = m[1][0]; float c = m[2][0];
        float d = m[0][1]; float e = m[1][1]; float f = m[2][1];
        float g = m[0][2]; float h = m[1][2]; float i = m[2][2];
        
        return a*e*i + b*f*g + c*d*h - c*e*g - b*d*i - a*f*h;
    }

    public Mat3 invert() {
        float det = determinant();

        if(FastMath.abs(det) <= 0) {
            toZeros();
            
            return this;
        }
        
        if(tmpM == null)
            tmpM = new Mat3();
        
        transpose();
        
        tmpM.m[0][0] = +1*(m[1][1]*m[2][2] - m[2][1]*m[1][2]);
        tmpM.m[0][1] = -1*(m[1][0]*m[2][2] - m[1][2]*m[2][0]);
        tmpM.m[0][2] = +1*(m[1][0]*m[2][1] - m[1][1]*m[2][0]);
        tmpM.m[1][0] = -1*(m[0][1]*m[2][2] - m[0][2]*m[2][1]);
        tmpM.m[1][1] = +1*(m[0][0]*m[2][2] - m[0][2]*m[2][0]);
        tmpM.m[1][2] = -1*(m[0][0]*m[2][1] - m[0][1]*m[2][0]);
        tmpM.m[2][0] = +1*(m[0][1]*m[1][2] - m[0][2]*m[1][1]);
        tmpM.m[2][1] = -1*(m[0][0]*m[1][2] - m[0][2]*m[1][0]);
        tmpM.m[2][2] = +1*(m[0][0]*m[1][1] - m[0][1]*m[1][0]);

        float one_over_det = 1.0f/det;
        setTo(tmpM);
        mult(one_over_det);

        return this;
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
