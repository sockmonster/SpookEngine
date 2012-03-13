package com.spookedengine.math;

/**
 *
 * @author Oliver Winks
 */
public class Vec {

    public float[] v;
    
    public Vec(int dim) {
        v = new float[dim];
    }

    public Vec(float[] v) {
        v = (float[]) v.clone();
    }

    public Vec(Vec vec) {
        v = (float[]) vec.v.clone();
    }

    /**
     * Sets this vector to the paramater vector. This method makes no size or
     * bounds validation, it assumes that the vectors are the same size.
     *
     * @param vec Vector to set this vector to.
     * @return this vector.
     */
    public Vec setTo(Vec vec) {
        for(int i=0; i<v.length; i++)
            this.v[i] = v[i];

        return this;
    }

    /**
     * Sets this vector to the paramater array. This method makes no size or
     * bounds validation, it assumes that the vector and the array are the same
     * size.
     *
     * @param v Vector to set this vector to.
     * @return this vector.
     */
    public Vec setTo(float[] v) {
        for(int i=0; i<v.length; i++)
            this.v[i] = v[i];

        return this;
    }

    public Vec setTo(float s) {
        for(int i=0; i<v.length; i++)
            v[i] = s;

        return this;
    }

    /**
     * Fills this vector with zeros.
     *
     * @return this vector.
     */
    public Vec toZeros() {
        for(int i=0; i<v.length; i++)
            v[i] = 0;

        return this;
    }

    /**
     * Fills this vector with ones.
     *
     * @return this vector.
     */
    public Vec toOnes() {
        for(int i=0; i<v.length; i++)
            v[i] = 1;

        return this;
    }

    /**
     * Adds the paramter vector to this vector. This method makes no size or
     * bounds validation, it assumes that the vectors are the same size.
     *
     * @param vec Vector to add.
     * @return this vector.
     */
    public Vec add(Vec vec) {
        for(int i=0; i<v.length; i++)
            v[i] += vec.v[i];

        return this;
    }

    /**
     * Add the paramter array to this vector. This method makes no size or
     * bounds validation, it assumes that the vector and the array are the same
     * size.
     *
     * @param v Vector to add.
     * @return this vector.
     */
    public Vec add(float[] v) {
        for(int i=0; i<v.length; i++)
            this.v[i] += v[i];

        return this;
    }

    /**
     * Subtracts the paramter vector from this vector. This method makes no
     * size or bounds validation, it assumes that the vectors are the same
     * size.
     *
     * @param vec Vector to subtract.
     * @return this vector.
     */
    public Vec sub(Vec vec) {
        for(int i=0; i<v.length; i++)
            v[i] -= vec.v[i];

        return this;
    }

    /**
     * Subtracts the paramter array from this vector. This method makes no size
     * or bounds validation, it assumes that the vector and the array are the
     * same size.
     *
     * @param vec Vector to subtract.
     * @return this vector.
     */
    public Vec sub(float[] v) {
        for(int i=0; i<v.length; i++)
            this.v[i] -= v[i];

        return this;
    }

    /**
     * Multiplies the scalar paramter and this vector together.
     *
     * @param s Scalar to multiply this vector with.
     * @return this vector.
     */
    public Vec mult(float s) {
        for(int i=0; i<v.length; i++)
            v[i] *= s;

        return this;
    }

    /**
     * Multiplies the paramter vector and this vector together. This method
     * makes no size or bounds validation, it assumes that the vectors are the
     * same size.
     *
     * @param vec Vector to multiply this vector with.
     * @return this vector.
     */
    public Vec mult(Vec vec) {
        for(int i=0; i<v.length; i++)
            v[i] *= vec.v[i];

        return this;
    }

    /**
     * Multiplies the paramter array and this vector together. This method
     * makes no size or bounds validation, it assumes that the vector and the
     * array are the same size.
     *
     * @param v Vector to multiply this vector with.
     * @return this vector.
     */
    public Vec mult(float[] v) {
        for(int i=0; i<v.length; i++)
            this.v[i] *= v[i];

        return this;
    }

    /**
     * Divides this vector by the paramter.
     *
     * @param s scalar to divide this vector with.
     * @return this vector.
     */
    public Vec div(float s) {
        for(int i=0; i<v.length; i++)
            v[i] /= s;

        return this;
    }

    /**
     * Divides this vector by the paramater vector. This method makes no size
     * or bounds validation, it assumes that the vectors are the same size.
     *
     * @param vec Vector to divide this vector by.
     * @return this vector.
     */
    public Vec div(Vec vec) {
        for(int i=0; i<v.length; i++)
            v[i] /= vec.v[i];

        return this;
    }

    /**
     * Divides this vector by the paramater array. This method makes no size
     * or bounds validation, it assumes that the vector and the array are the
     * same size.
     *
     * @param v Vector to divide this vector by.
     * @return this vector.
     */
    public Vec div(float[] v) {
        for(int i=0; i<v.length; i++)
            this.v[i] /= v[i];

        return this;
    }

    /**
     * Calculates the dot product between this vector and the paramater vector.
     * This method makes no size or bounds validation, it assumes that the
     * vectors are the same size.
     *
     * @param vec Vector to calculate the dot product from.
     * @return this vector.
     */
    public float dot(Vec vec) {
        float dot = 0;
        for(int i=0; i<v.length; i++)
            dot += v[i]*vec.v[i];

        return dot;
    }

    /**
     * Calculates the dot product between this vector and the paramater. This
     * method makes no size or bounds validation, it assumes that the vector
     * and the array are the same size.
     *
     * @param v Vector to calculate the dot product from.
     * @return this vector.
     */
    public float dot(float[] v) {
        float dot = 0;
        for(int i=0; i<v.length; i++)
            dot += this.v[i]*v[i];

        return dot;
    }

    /**
     * Calculates the euclidean distance between this vector and the paramater 
     * vector. This method makes no size or bounds validation, it assumes that
     * the vectors are the same size.
     *
     * @param vec The vector to calculate the distance to.
     * @return the distance between this vector and the paramater vector
     */
    public float dist(Vec vec) {
        float dist = 0;
        float d = 0;
        for(int i=0; i<v.length; i++) {
            d = v[i] - vec.v[i];
            dist += d*d;
        }

        return (float) Math.sqrt(dist);
    }

    /**
     * Calculates the euclidean distance between this vector and the paramater 
     * array. This method makes no size or bounds validation, it assumes that
     * the vector and the array are the same size.
     *
     * @param v The Vector to calculate the distance to.
     * @return the distance between this vector and the paramater vector
     */
    public float dist(float[] v) {
        float dist = 0;
        float d = 0;
        for(int i=0; i<v.length; i++) {
            d = this.v[i] - v[i];
            dist += d*d;
        }

        return (float) Math.sqrt(dist);
    }

    /**
     * @return the length (magnitude) of this vector.
     */
    public float length() {
        float length = 0;
        for(int i=0; i<v.length; i++)
            length += v[i]*v[i];

        return (float) Math.sqrt(length);
    }

    /**
     * @return the squared length (magnitude) of this vector.
     */
    public float lengthSquared() {
        float length = 0;
        for(int i=0; i<v.length; i++)
            length += v[i]*v[i];

        return length;
    }

    /**
     * Normalizes this vector.
     *
     * @return This vector.
     */
    public Vec norm() {
        float length = length();

        if(length != 0)
            return div(length());
        else
            return this;
    }
    
    @Override
    public boolean equals(Object vec) {
        if(vec instanceof Vec) {
            for(int i=0; i<v.length; i++) {
                if(v[i] != ((Vec) vec).v[i])
                    return false;
            }
                
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        String str = "[ ";
        for(int i=0; i<v.length; i++)
            str += v[i] + " ";
        str += "]";

        return str;
    }

}
