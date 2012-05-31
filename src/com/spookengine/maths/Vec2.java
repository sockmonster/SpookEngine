package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Vec2 {
    
    // convenience vars
    private final int dim = 2;
    
    public final float[] v = new float[2];
    
    public Vec2() {
    }

    public Vec2(float x, float y) {
        setTo(x, y);
    }
    
    public Vec2(float[] v) {
        setTo(v[0], v[1]);
    }

    public Vec2(Vec2 vec) {
        setTo(vec.v[0], vec.v[1]);
    }

    public float x() {
        return v[0];
    }

    public float y() {
        return v[1];
    }
    
    public final Vec2 setTo(float x, float y) {
        this.v[0] = x;
        this.v[1] = y;

        return this;
    }
    
    public Vec2 setTo(float[] v) {
        return setTo(v[0], v[1]);
    }

    public Vec2 setTo(Vec2 vec) {
        return setTo(vec.v[0], vec.v[1]);
    }
    
    public Vec2 toZeros() {
        return setTo(0, 0);
    }
    
    public Vec2 toOnes() {
        return setTo(1, 1);
    }

    public Vec2 add(float x, float y) {
        this.v[0] += x;
        this.v[1] += y;

        return this;
    }
    
    public Vec2 add(float[] v) {
        return add(v[0], v[1]);
    }
    
    public Vec2 add(Vec2 vec) {
        return add(vec.v[0], vec.v[1]);
    }
    
    public Vec2 sub(float x, float y) {
        v[0] -= x;
        v[1] -= y;

        return this;
    }
    
    public Vec2 sub(float[] v) {
        return sub(v[0], v[1]);
    }

    public Vec2 sub(Vec2 vec) {
        return sub(vec.v[0], vec.v[1]);
    }
    
    public Vec2 mult(float x, float y) {
        v[0] *= x;
        v[1] *= y;

        return this;
    }
    
    public Vec2 mult(float[] v) {
        return mult(v[0], v[1]);
    }
    
    public Vec2 mult(Vec2 vec) {
        return mult(vec.v[0], vec.v[1]);
    }
    
    public Vec2 mult(float s) {
        return mult(s, s);
    }
    
    public Vec2 div(float x, float y) {
        v[0] /= x;
        v[1] /= y;

        return this;
    }
    
    public Vec2 div(float[] v) {
        return div(v[0], v[1]);
    }
    
    public Vec2 div(Vec2 vec) {
        return div(vec.v[0], vec.v[1]);
    }
    
    public Vec2 div(float s) {
        return div(s, s);
    }
    
    public float dot(float x, float y) {
        return v[0]*x + v[1]*y;
    }
    
    public float dot(float[] v) {
        return dot(v[0], v[1]);
    }
    
    public float dot(Vec2 vec) {
        return dot(vec.v[0], vec.v[1]);
    }
    
    public float dist(float x, float y) {
        float tx = this.v[0] - x;
        float ty = this.v[1] - y;

        return (float) Math.sqrt(tx*tx + ty*ty);
    }
    
    public float dist(float[] v) {
        return dist(v[0], v[1]);
    }
    
    public float dist(Vec2 vec) {
        return dist(vec.v[0], vec.v[1]);
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
    
    public Vec2 norm() {
        float length = length();

        if(length != 0)
            return div(length());
        else
            return this;
    }
    
    @Override
    public boolean equals(Object vec) {
        if(vec instanceof Vec2) {
            for(int i=0; i<dim; i++) {
                if(v[i] != ((Vec2) vec).v[i])
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
