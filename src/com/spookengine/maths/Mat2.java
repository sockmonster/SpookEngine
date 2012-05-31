package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Mat2 {
    
    // convenience vars
    private final int dim = 2;
    private Mat2 tmpM;
    
    public final float[][] m = new float[dim][dim];

    public Mat2() {
    }
    
    public Mat2(
            float m00, float m01,
            float m10, float m11) {
        setTo(
                m00, m01, 
                m10, m11);
    }

    public Mat2(Mat2 mat) {
        setTo(
                mat.m[0][0], mat.m[0][1],
                mat.m[1][0], mat.m[1][1]);
    }

    public Mat2(float[][] m) {
        setTo(
                m[0][0], m[0][1],
                m[1][0], m[1][1]);
    }
    
    /* ******** ******** ******** */
    /*          SETTERS           */
    /* ******** ******** ******** */
    public final Mat2 setTo(
            float m00, float m01,
            float m10, float m11) {
        m[0][0]=m00; m[0][1]=m01;
        m[1][0]=m10; m[1][1]=m11;

        return this;
    }
    
    public Mat2 setTo(Mat2 mat) {
        return setTo(
                mat.m[0][0], mat.m[0][1],
                mat.m[1][0], mat.m[1][1]);
    }
    
    public Mat2 setTo(float[][] m) {
        return setTo(
                m[0][0], m[0][1],
                m[1][0], m[1][1]);
    }

    public Mat2 toZeros() {
        return setTo(
                0, 0, 
                0, 0);
    }
    
    public Mat2 toOnes() {
        return setTo(
                1, 1, 
                1, 1);
    }
    
    public Mat2 toIdentity() {
        return setTo(
                1, 0, 
                0, 1);
    }
    
    /* ******** ******** ******** */
    /*         OPERATORS          */
    /* ******** ******** ******** */
    public Mat2 add(
            float m00, float m01,
            float m10, float m11) {
        m[0][0]+=m00; m[0][1]+=m01;
        m[1][0]+=m10; m[1][1]+=m11;

        return this;
    }
    
    public Mat2 add(float[][] m) {
        return add(
                m[0][0], m[0][1],
                m[1][0], m[1][1]);
    }
    
    public Mat2 add(Mat2 mat) {
        return add(
                mat.m[0][0], mat.m[0][1],
                mat.m[1][0], mat.m[1][1]);
    }
    
    public Mat2 sub(
            float m00, float m01,
            float m10, float m11) {
        m[0][0]-=m00; m[0][1]-=m01;
        m[1][0]-=m10; m[1][1]-=m11;

        return this;
    }
    
    public Mat2 sub(float[][] m) {
        return sub(
                m[0][0], m[0][1],
                m[1][0], m[1][1]);
    }
    
    public Mat2 sub(Mat2 mat) {
        return sub(
                mat.m[0][0], mat.m[0][1],
                mat.m[1][0], mat.m[1][1]);
    }
    
    public Mat2 mult(
            float m00, float m01,
            float m10, float m11) {
        if(tmpM == null)
            tmpM = new Mat2();
        
        tmpM.m[0][0] = m[0][0]*m00 + m[0][1]*m10;
        tmpM.m[0][1] = m[0][0]*m01 + m[0][1]*m11;

        tmpM.m[1][0] = m[1][0]*m00 + m[1][1]*m10;
        tmpM.m[1][1] = m[1][0]*m01 + m[1][1]*m11;

        // set matrix
        setTo(tmpM);

        return this;
    }
    
    public Mat2 mult(float[][] m) {
        return mult(
                m[0][0], m[0][1],
                m[1][0], m[1][1]);
    }
    
    public Mat2 mult(Mat2 mat) {
        return mult(
                mat.m[0][0], mat.m[0][1],
                mat.m[1][0], mat.m[1][1]);
    }
    
    public Mat2 mult(float s) {
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                m[r][c] *= s;
        }

        return this;
    }
    
    public void mult(Vec2 vec) {
        float x = m[0][0]*vec.v[0] + m[0][1]*vec.v[1];
        float y = m[1][0]*vec.v[0] + m[1][1]*vec.v[1];

        vec.v[0] = x;
        vec.v[1] = y;
    }
    
    public Mat2 div(float s) {
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                m[r][c] /= s;
        }

        return this;
    }
   
    public Mat2 transpose() {
        if(tmpM == null)
            tmpM = new Mat2();
        
        // transpose
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                tmpM.m[r][c] = m[c][r];
        }

        // set the transposed view
        setTo(tmpM);

        return this;
    }

    public Mat2 invert() {
        float det = m[0][0]*m[1][1] - m[0][1]*m[1][0];

        if(FastMath.abs(det) <= 0) {
            toZeros();

            return this;
        }

        float one_over_det = 1.0f/det;
        mult(one_over_det);

        return this;
    }
    
    /* ******** ******** ******** */
    /*         ROTATIONS          */
    /* ******** ******** ******** */
    public float getRotation() {
        return (float) Math.acos(m[0][0]);
    }
    
    public void rotateTo(float angle) {
        float cos = FastMath.cos(angle);
        float sin = FastMath.sin(angle);

        m[0][0]= cos; m[0][1]=-sin;
        m[1][0]= sin; m[1][1]= cos;
    }
    
    @Override
    public boolean equals(Object mat) {
        if(mat instanceof Mat2) {
            for(int r=0; r<dim; r++) {
                for(int c=0; c<dim; c++) {
                    if(m[r][c] != ((Mat2) mat).m[r][c])
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
