package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Mat2 extends Mat {

    public Mat2() {
        super(2);
    }

    public Mat2(Mat2 mat) {
        super(mat);
    }

    public Mat2(float[][] m) {
        super(2);

        super.setTo(m);
    }

    @Override
    public Mat2 setTo(Mat mat) {
        super.setTo(mat);

        return this;
    }

    @Override
    public Mat2 setTo(float[][] m) {
        super.setTo(m);

        return this;
    }

    public Mat2 setTo(
            float m00, float m01,
            float m10, float m11) {
        m[0][0]=m00; m[0][1]=m01;
        m[1][0]=m10; m[1][1]=m11;

        return this;
    }

    @Override
    public Mat2 toZeros() {
        super.toZeros();

        return this;
    }

    @Override
    public Mat2 toOnes() {
        super.toOnes();

        return this;
    }

    @Override
    public Mat2 toIdentity() {
        super.toIdentity();

        return this;
    }

    @Override
    public Mat2 add(Mat mat) {
        super.add(mat);

        return this;
    }

    @Override
    public Mat2 add(float[][] m) {
        super.add(m);

        return this;
    }

    public Mat2 add(
            float m00, float m01,
            float m10, float m11) {
        m[0][0]+=m00; m[0][1]+=m01;
        m[1][0]+=m10; m[1][1]+=m11;

        return this;
    }

    @Override
    public Mat2 sub(Mat mat) {
        super.sub(mat);

        return this;
    }

    @Override
    public Mat2 sub(float[][] m) {
        super.sub(m);

        return this;
    }

    public Mat2 sub(
            float m00, float m01,
            float m10, float m11) {
        m[0][0]-=m00; m[0][1]-=m01;
        m[1][0]-=m10; m[1][1]-=m11;

        return this;
    }

    @Override
    public Mat2 mult(float s) {
        super.mult(s);

        return this;
    }

    @Override
    public Mat2 mult(Mat mat) {
        super.mult(mat);

        return this;
    }

    @Override
    public Mat2 mult(float[][] m) {
        super.mult(m);

        return this;
    }
    
    public Mat2 mult(
            float m00, float m01,
            float m10, float m11) {
        tmpM[0][0] = m[0][0]*m00 + m[0][1]*m10;
        tmpM[0][1] = m[0][0]*m01 + m[0][1]*m11;

        tmpM[1][0] = m[1][0]*m00 + m[1][1]*m10;
        tmpM[1][1] = m[1][0]*m01 + m[1][1]*m11;

        // set matrix
        super.setTo(tmpM);

        return this;
    }

    @Override
    public void mult(Vec vec) {
        if(vec instanceof Vec2) {
            float x = m[0][0]*vec.v[0] + m[0][1]*vec.v[1];
            float y = m[1][0]*vec.v[0] + m[1][1]*vec.v[1];

            vec.v[0] = x;
            vec.v[1] = y;
        } else {
            throw new IllegalArgumentException("Only 2 or 3 dimensional " +
                    "vectors can be multiplied through a Matrix3");
        }
    }
    
    @Override
    public Mat2 div(float s) {
        super.div(s);

        return this;
    }

    public Mat2 invert() {
        float det = m[0][0]*m[1][1] - m[0][1]*m[1][0];

        if(FastMath.abs(det) <= 0) {
            super.toZeros();

            return this;
        }

        float one_over_det = 1.0f/det;
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                m[r][c] = tmpM[r][c]*one_over_det;
        }

        return this;
    }

}
