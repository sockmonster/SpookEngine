package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Vec3 extends Vec {

    public Vec3() {
        super(3);
    }

    public Vec3(float x, float y, float z) {
        super(3);

        v[0] = x;
        v[1] = y;
        v[2] = z;
    }
    
    public Vec3(float[] v) {
        super(v);
    }

    public Vec3(Vec vec) {
        super(vec);
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

    @Override
    public Vec3 setTo(Vec vec) {
        return setTo(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    @Override
    public Vec3 setTo(float[] v) {
        super.setTo(v);
        
        return this;
    }

    public Vec3 setTo(float x, float y, float z) {
        this.v[0] = x;
        this.v[1] = y;
        this.v[2] = z;

        return this;
    }

    @Override
    public Vec3 add(Vec vec) {
        return add(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    @Override
    public Vec3 add(float[] v) {
        super.add(v);
        
        return this;
    }

    public Vec3 add(float x, float y, float z) {
        this.v[0] += x;
        this.v[1] += y;
        this.v[2] += z;

        return this;
    }

    @Override
    public Vec3 sub(Vec vec) {
        return sub(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    @Override
    public Vec3 sub(float[] v) {
        super.sub(v);
        
        return this;
    }

    public Vec3 sub(float x, float y, float z) {
        v[0] -= x;
        v[1] -= y;
        v[2] -= z;

        return this;
    }

    @Override
    public Vec3 mult(float s) {
        return mult(s, s, s);
    }

    @Override
    public Vec3 mult(Vec vec) {
        return mult(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    @Override
    public Vec3 mult(float[] v) {
        super.mult(v);
        
        return this;
    }

    public Vec3 mult(float x, float y, float z) {
        v[0] *= x;
        v[1] *= y;
        v[2] *= z;

        return this;
    }

    @Override
    public Vec3 div(float s) {
        return div(s, s, s);
    }

    @Override
    public Vec3 div(Vec vec) {
        return div(vec.v[0], vec.v[1], vec.v[2]);
    }
    
    @Override
    public Vec3 div(float[] v) {
        super.div(v);
        
        return this;
    }

    public Vec3 div(float x, float y, float z) {
        v[0] /= x;
        v[1] /= y;
        v[2] /= z;

        return this;
    }

    @Override
    public float dot(Vec vec) {
        return dot(vec.v[0], vec.v[1], vec.v[2]);
    }

    public float dot(float x, float y, float z) {
        return v[0]*x + v[1]*y + v[2]*z;
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

    @Override
    public float dist(Vec vec) {
        return dist(vec.v[0], vec.v[1], vec.v[2]);
    }

    public float dist(float x, float y, float z) {
        float tx = this.v[0] - x;
        float ty = this.v[1] - y;
        float tz = this.v[2] - z;

        return (float) Math.sqrt(tx*tx + ty*ty + tz*tz);
    }

    @Override
    public Vec3 norm() {
        float length = length();

        if(length != 0)
            return div(length());
        else
            return this;
    }

}
