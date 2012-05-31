package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Vec3 {
    
    // convenience vars
    private final int dim = 3;
    
    public final float[] v = new float[3];

    public Vec3() {
    }

    public Vec3(float x, float y, float z) {
        setTo(x, y, z);
    }
    
    public Vec3(float[] v) {
        setTo(v[0], v[1], v[2]);
    }

    public Vec3(Vec3 vec) {
        setTo(vec.v[0], vec.v[1], vec.v[2]);
    }

    public float x() {
        return v[0];
    }

    public float y() {
        return v[1];
    }

    public float z() {
        return v[2];
    }
    
    public final Vec3 setTo(float x, float y, float z) {
        this.v[0] = x;
        this.v[1] = y;
        this.v[2] = z;

        return this;
    }
    
    public Vec3 setTo(float[] v) {
        return setTo(v[0], v[1], v[2]);
    }
    
    public Vec3 setTo(Vec3 vec) {
        return setTo(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    public Vec3 toZeros() {
        return setTo(0, 0, 0);
    }
    
    public Vec3 toOnes() {
        return setTo(1, 1, 1);
    }
    
    public Vec3 add(float x, float y, float z) {
        this.v[0] += x;
        this.v[1] += y;
        this.v[2] += z;

        return this;
    }
    
    public Vec3 add(float[] v) {
        return add(v[0], v[1], v[2]);
    }
    
    public Vec3 add(Vec3 vec) {
        return add(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    public Vec3 sub(float x, float y, float z) {
        v[0] -= x;
        v[1] -= y;
        v[2] -= z;

        return this;
    }
    
    public Vec3 sub(float[] v) {
        return sub(v[0], v[1], v[2]);
    }
    
    public Vec3 sub(Vec3 vec) {
        return sub(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    public Vec3 mult(float x, float y, float z) {
        v[0] *= x;
        v[1] *= y;
        v[2] *= z;

        return this;
    }
    
    public Vec3 mult(float[] v) {
        return mult(v[0], v[1], v[2]);
    }
    
    public Vec3 mult(Vec3 vec) {
        return mult(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    public Vec3 mult(float s) {
        return mult(s, s, s);
    }
    
    public Vec3 div(float x, float y, float z) {
        v[0] /= x;
        v[1] /= y;
        v[2] /= z;

        return this;
    }
    
    public Vec3 div(float[] v) {
        return div(v[0], v[1], v[2]);
    }
    
    public Vec3 div(Vec3 vec) {
        return div(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    public Vec3 div(float s) {
        return div(s, s, s);
    }
    
    public float dot(float x, float y, float z) {
        return v[0]*x + v[1]*y + v[2]*z;
    }
    
    public float dot(float[] v) {
        return dot(v[0], v[1], v[2]);
    }
    
    public float dot(Vec3 vec) {
        return dot(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    /**
     * Calculates the cross product between this Vec3 and the vector defined
     * by the given x, y and z values and stores the result in this Vec3.
     *
     * @param x
     * @param y
     * @param z
     * @return cross product.
     */
    public Vec3 cross(float x, float y, float z) {
        float nx = this.v[1]*z - this.v[2]*y;
    	float ny = this.v[2]*x - this.v[0]*z;
    	float nz = this.v[0]*y - this.v[1]*x;

    	v[0] = nx;
    	v[1] = ny;
    	v[2] = nz;

        return this;
    }
    
    /**
     * Calculates the cross product between this Vec3 and the given Vec3
     * and stores the result in this Vec3.
     *
     * @param vec The Vec3 to perform the cross product calculation with.
     * @return The cross product between this Vec3 and the given Vec3.
     */
    public Vec3 cross(float[] v) {
        return cross(v[0], v[1], v[2]);
    }
    
    /**
     * Calculates the cross product between this Vec3 and the given Vec3
     * and stores the result in this Vec3.
     *
     * @param vec The Vec3 to perform the cross product calculation with.
     * @return The cross product between this Vec3 and the given Vec3.
     */
    public Vec3 cross(Vec3 vec) {
    	return cross(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    public float dist(float x, float y, float z) {
        float tx = this.v[0] - x;
        float ty = this.v[1] - y;
        float tz = this.v[2] - z;

        return (float) Math.sqrt(tx*tx + ty*ty + tz*tz);
    }
    
    public float dist(float[] v) {
        return dist(v[0], v[1], v[2]);
    }
    
    public float dist(Vec3 vec) {
        return dist(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    /**
     * @return the length (magnitude) of this vector.
     */
    public float length() {
        float length = 0;
        for(int i=0; i<dim; i++)
            length += v[i]*v[i];

        return (float) Math.sqrt(length);
    }

    /**
     * @return the squared length (magnitude) of this vector.
     */
    public float lengthSquared() {
        float length = 0;
        for(int i=0; i<dim; i++)
            length += v[i]*v[i];

        return length;
    }
    
    public Vec3 norm() {
        float length = length();

        if(length != 0)
            return div(length());
        else
            return this;
    }
    
    @Override
    public boolean equals(Object vec) {
        if(vec instanceof Vec3) {
            for(int i=0; i<dim; i++) {
                if(v[i] != ((Vec3) vec).v[i])
                    return false;
            }
                
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        String str = "[ ";
        for(int i=0; i<dim; i++)
            str += v[i] + " ";
        str += "]";

        return str;
    }

}
