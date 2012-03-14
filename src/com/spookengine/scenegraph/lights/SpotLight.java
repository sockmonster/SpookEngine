package com.spookengine.scenegraph.lights;

import com.spookengine.math.Vec3;

/**
 * A SpotLight.
 *
 * <i>NOTE: If a spot light is too close to an object it will NOT illuminate
 * that object. This is an OpenGL thing that has caught me out twice!!</i>
 *
 * @author Oliver Winks
 */
public class SpotLight extends LightBulb {

    protected Vec3 dir;
    private float angle;
    private float focus;

    public SpotLight() {
        super();

        dir = new Vec3(0,0,1);
        setAngle(40.0f);
        setFocus(30.0f);
    }

    public SpotLight(float angle, float focus) {
        super();

        dir = new Vec3(0,0,1);
        setAngle(angle);
        setFocus(focus);
    }

    public SpotLight(float[] rgbArray, float angle, float focus) {
        super(rgbArray);

        dir = new Vec3(0,0,1);
        setAngle(angle);
        setFocus(focus);
    }

    public SpotLight(float r, float g, float b, float angle, float focus) {
        super(r, g, b);

        dir = new Vec3(0,0,1);
        setAngle(angle);
        setFocus(focus);
    }

    public Vec3 getDir() {
        return dir;
    }

    public void setAngle(float angle) {
        if( (angle >= 0 && angle <= 90) || angle == 180)
            this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    public void setFocus(float focus) {
        this.focus = focus;
    }

    public float getFoucs() {
        return focus;
    }

    @Override
    public Light clone() {
        SpotLight clone = new SpotLight();

        clone.on = on;
        
        for(int i=0; i<4; i++)
            clone.colour[i] = colour[i];

        clone.pos.setTo(pos);
        clone.dir.setTo(dir);
        clone.constAttenuation = constAttenuation;
        clone.linearAttenuation = linearAttenuation;
        clone.quadAttenuation = quadAttenuation;

        return clone;
    }

}
