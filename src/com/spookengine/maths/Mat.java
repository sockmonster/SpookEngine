package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Mat {

    protected int dim;
    public float[][] m;

    // convenience vars
    protected float[][] tmpM;

    public Mat(int dim) {
        this.dim = dim;
        m = new float[dim][dim];
        tmpM = new float[dim][dim];

        toIdentity();
    }

    public Mat(Mat mat) {
        this.dim = mat.dim;
        m = (float[][]) mat.m.clone();
        tmpM = new float[dim][dim];
    }

    public Mat(float[][] m) {
        dim = m.length;
        m = (float[][]) m.clone();
        tmpM = new float[dim][dim];
    }

    /**
     * Sets this matrix to the paramater matrix. This method makes no size or
     * bounds validation, it assumes that the matrices are the same size.
     *
     * @param mat The matrix to set this matrix to.
     * @return this matrix.
     */
    public Mat setTo(Mat mat) {
        for(int r=0; r<m.length; r++) {
            for(int c=0; c<m[r].length; c++)
                m[r][c] = mat.m[r][c];
        }

        return this;
    }

    /**
     * Sets this matrix to the paramater matrix. This method makes no size or
     * bounds validation, it assumes that the matrices are the same size.
     *
     * @param mat The matrix to set this matrix to.
     * @return this matrix.
     */
    public Mat setTo(float[][] m) {
        for(int r=0; r<m.length; r++) {
            for(int c=0; c<m[r].length; c++)
                this.m[r][c] = m[r][c];
        }

        return this;
    }

    /**
     * Fills this matrix with zeros.
     *
     * @return this matrix.
     */
    public Mat toZeros() {
        for(int r=0; r<m.length; r++) {
            for(int c=0; c<m[r].length; c++)
                m[r][c] = 0;
        }

        return this;
    }

    /**
     * Fills this matrix with ones.
     *
     * @return this matrix.
     */
    public Mat toOnes() {
        for(int r=0; r<m.length; r++) {
            for(int c=0; c<m[r].length; c++)
                m[r][c] = 1;
        }

        return this;
    }

    /**
     * Sets this matrix to the identity matrix.
     *
     * @return this matrix.
     */
    public Mat toIdentity() {
        for(int r=0; r<m.length; r++) {
            for(int c=0; c<m[r].length; c++) {
                if(r == c)
                    m[r][c] = 1;
                else
                    m[r][c] = 0;
            }
        }

        return this;
    }

    /**
     * Adds the parameter matrix to this matrix. This method makes no size or
     * bounds validation, it assumes that the matrices are the same size.
     *
     * @param mat The matrix to add.
     * @return this matrix.
     */
    public Mat add(Mat mat) {
        for(int r=0; r<m.length; r++) {
            for(int c=0; c<m[r].length; c++)
                m[r][c] += mat.m[r][c];
        }

        return this;
    }

    /**
     * Adds the parameter matrix to this matrix. This method makes no size or
     * bounds validation, it assumes that the matrices are the same size.
     *
     * @param mat The matrix to add.
     * @return this matrix.
     */
    public Mat add(float[][] m) {
        for(int r=0; r<m.length; r++) {
            for(int c=0; c<m[r].length; c++)
                this.m[r][c] += m[r][c];
        }

        return this;
    }

    /**
     * Subtracts the parameter matrix from this matrix. This method makes no
     * size or bounds validation, it assumes that the matrices are the same
     * size.
     *
     * @param mat The matrix to subtract from this matrix.
     * @return this matrix.
     */
    public Mat sub(Mat mat) {
        for(int r=0; r<m.length; r++) {
            for(int c=0; c<m[r].length; c++)
                m[r][c] -= mat.m[r][c];
        }

        return this;
    }

    /**
     * Subtracts the parameter matrix from this matrix. This method makes no
     * size or bounds validation, it assumes that the matrices are the same
     * size.
     *
     * @param mat The matrix to subtract from this matrix.
     * @return this matrix.
     */
    public Mat sub(float[][] m) {
        for(int r=0; r<m.length; r++) {
            for(int c=0; c<m[r].length; c++)
                this.m[r][c] -= m[r][c];
        }

        return this;
    }

    /**
     * Multiplies this matrix by a scalar value.
     *
     * @param s The scalar value to multiply this matrix by.
     * @return this matrix.
     */
    public Mat mult(float s) {
        for(int r=0; r<m.length; r++) {
            for(int c=0; c<m[r].length; c++)
                m[r][c] *= s;
        }
        
        return this;
    }

    /**
     * Multiplies this matrix by the parameter matrix. This method makes no
     * size or bounds validation, it assumes that the matrices are the same
     * size.
     *
     * @param mat the matrix to multiply this matrix by.
     * @return this matrix.
     */
    public Mat mult(Mat mat) {
        for(int r=0; r<dim; r++) {
            for(int i=0; i<dim; i++) {
                tmpM[r][i] = 0;
                for(int c=0; c<dim; c++)
                    tmpM[r][i] += m[r][c]*mat.m[c][i];
            }

            for(int i=0; i<dim; i++)
                m[r][i] = tmpM[r][i];
        }

        return this;
    }

    /**
     * Multiplies this matrix by the parameter matrix. This method makes no
     * size or bounds validation, it assumes that the matrices are the same
     * size.
     *
     * @param mat the matrix to multiply this matrix by.
     * @return this matrix.
     */
    public Mat mult(float[][] m) {
        for(int r=0; r<m.length; r++) {
            for(int i=0; i<m.length; i++) {
                tmpM[r][i] = 0;
                for(int c=0; c<m[r].length; c++)
                    tmpM[r][i] += this.m[r][c]*m[c][i];
            }

            for(int i=0; i<m.length; i++)
                this.m[r][i] = tmpM[r][i];
        }

        return this;
    }

    /**
     * Multiplies the parameter vector through this matrix. This method makes
     * no size or bounds validation, it assumes the parameter vector is the
     * correct dimension to multiply through this matrix.
     *
     * @param vec the vector to multiply through this matrix.
     */
    public void mult(Vec vec) {
        for(int r=0; r<m.length; r++) {
            float v = 0;

            for(int c=0; c<m[r].length; c++)
                v += m[r][c]*vec.v[c];
            vec.v[r] = v;
        }
    }

    /**
     * Divides this matrix by a scalar value.
     *
     * @param s the scalar value to divide this matrix by.
     * @return this matrix.
     */
    public Mat div(float s) {
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
    public Mat transpose() {
        // transpose
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                tmpM[r][c] = m[c][r];
        }

        // set the transposed view
        for(int r=0; r<dim; r++) {
            for(int c=0; c<dim; c++)
                m[r][c] = tmpM[r][c];
        }

        return this;
    }

    @Override
    public boolean equals(Object mat) {
        if(mat instanceof Mat) {
            for(int r=0; r<m.length; r++) {
                for(int c=0; c<m[r].length; c++) {
                    if(m[r][c] != ((Mat) mat).m[r][c])
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
