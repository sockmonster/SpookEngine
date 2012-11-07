package com.spookengine.core.lights;

import com.spookengine.maths.Vec3;

/**
 *
 * @author Oliver Winks
 */
public class LightBulb extends Light {

    protected Vec3 pos;
    protected float constAttenuation;
    protected float linearAttenuation;
    protected float quadAttenuation;
    public boolean hasAmbience;

    public LightBulb() {
        super();

        pos = new Vec3(0,0,0);
        constAttenuation = 1;
        linearAttenuation = 0;
        quadAttenuation = 0;
    }

    public LightBulb(float[] rgbArray) {
        super(rgbArray);

        pos = new Vec3();
        constAttenuation = 1;
        linearAttenuation = 0;
        quadAttenuation = 0;
    }

    public LightBulb(float r, float g, float b) {
        super(r, g, b);

        pos = new Vec3();
        constAttenuation = 1;
        linearAttenuation = 0;
        quadAttenuation = 0;
    }

    public Vec3 getPos() {
        return pos;
    }

    public void setAttenuation(float constant, float linear, float quadratic) {
        if(constant >= 0)
            constAttenuation = constant;

        if(linear >= 0)
            linearAttenuation = linear;

        if(quadratic >= 0)
            quadAttenuation = quadratic;
    }

    public float getConstAttenuation() {
        return constAttenuation;
    }

    public float getLinearAttenuation() {
        return linearAttenuation;
    }

    public float getQuadAttenuation() {
        return quadAttenuation;
    }

    @Override
    public Light clone() {
        LightBulb clone = new LightBulb();

        clone.on = on;

        for(int i=0; i<4; i++)
            clone.colour[i] = colour[i];

        clone.pos.setTo(pos);

        clone.constAttenuation = constAttenuation;
        clone.linearAttenuation = linearAttenuation;
        clone.quadAttenuation = quadAttenuation;

        return clone;
    }

}
