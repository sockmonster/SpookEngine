package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Vec2 extends Vec {

    public Vec2() {
        super(2);
    }

    public Vec2(float x, float y) {
        super(2);

        v[0] = x;
        v[1] = y;
    }

    public Vec2(Vec vec) {
        super(vec);
    }

    public float x() {
        return v[0];
    }

    public float y() {
        return v[1];
    }

    @Override
    public Vec2 setTo(Vec vec) {
        return setTo(vec.v[0], vec.v[1]);
    }

    public Vec2 setTo(float x, float y) {
        this.v[0] = x;
        this.v[1] = y;

        return this;
    }

    @Override
    public Vec2 add(Vec vec) {
        return add(vec.v[0], vec.v[1]);
    }

    public Vec2 add(float x, float y) {
        this.v[0] += x;
        this.v[1] += y;

        return this;
    }

    @Override
    public Vec2 sub(Vec vec) {
        return sub(vec.v[0], vec.v[1]);
    }

    public Vec2 sub(float x, float y) {
        v[0] -= x;
        v[1] -= y;

        return this;
    }

    @Override
    public Vec2 mult(float s) {
        return mult(s, s);
    }

    @Override
    public Vec2 mult(Vec vec) {
        return mult(vec.v[0], vec.v[1]);
    }

    public Vec2 mult(float x, float y) {
        v[0] *= x;
        v[1] *= y;

        return this;
    }

    @Override
    public Vec2 div(float s) {
        return div(s,s);
    }

    @Override
    public Vec2 div(Vec vec) {
        return div(vec.v[0], vec.v[1]);
    }

    public Vec2 div(float x, float y) {
        v[0] /= x;
        v[1] /= y;

        return this;
    }

    @Override
    public float dot(Vec vec) {
        return dot(vec.v[0], vec.v[1]);
    }

    public float dot(float x, float y) {
        return v[0]*x + v[1]*y;
    }

    @Override
    public float dist(Vec vec) {
        return dist(vec.v[0], vec.v[1]);
    }

    public float dist(float x, float y) {
        float tx = this.v[0] - x;
        float ty = this.v[1] - y;

        return (float) Math.sqrt(tx*tx + ty*ty);
    }

    @Override
    public Vec2 norm() {
        float length = length();

        if(length != 0)
            return div(length());
        else
            return this;
    }

}
