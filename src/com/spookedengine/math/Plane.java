package com.spookedengine.math;

/**
 *
 * @author Oliver Winks
 */
public class Plane {
    
    public Vec3 normal;
    public float D;

    // convienience vars
    private Vec3 temp1, temp2;

    public Plane() {
            temp1 = new Vec3();
            temp2 = new Vec3();

            normal = new Vec3(0,1,0);
            D = 0f;
    }

    public Plane(Vec3 normal, float D) {
            temp1 = new Vec3();
            temp2 = new Vec3();

            this.normal = new Vec3(normal);
            this.D = D;
    }

    public Plane(Vec3 a, Vec3 b, Vec3 c) {
            temp1 = new Vec3(c).sub(a).norm();
            temp2 = new Vec3(c).sub(b).norm();

            normal = new Vec3();
            setTo(a, b, c);
    }

    public Plane setTo(Plane plane) {
            normal.setTo(plane.normal);
            D = plane.D;

            return this;
    }

    public Plane setTo(Vec3 normal, float D) {
            this.normal.setTo(normal);
            this.D = D;

            return this;
    }

    public Plane setTo(Vec3 a, Vec3 b, Vec3 c) {
            temp1.setTo(c).sub(a).norm();
            temp2.setTo(c).sub(b).norm();

            ((Vec3) normal.setTo(temp1)).cross(temp2).norm();

            temp1.setTo(0,0,0).sub(a);
            D = temp1.dot(normal);

            return this;
    }

    public float dist(Vec3 pos) {
            return normal.dot(pos) + D;
    }

    public boolean findIntersection(Vec3 rayPos, Vec3 rayDir, Vec3 intersection) {
        // check if ray actually intersects the plane
        float a = normal.dot(rayDir);
        if(a > -0.00001f && a < 0.00001f)
            return false;

        // check if ray position is behind the plane
        float b = -(normal.dot(rayPos) - D)/a;
        if (b < 0.00001f)
            return false;

        intersection.setTo(rayDir).mult(b).add(rayPos);

        return true;
    }

    @Override
    public String toString() {
        return normal.toString() + " + " + D;
    }
}
