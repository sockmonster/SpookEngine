package com.spookengine.scenegraph.lights;

/**
 *
 * @author Oliver Winks
 */
public class Light implements Cloneable {
    
    public static int activeLights = 0;

    public boolean on;
    protected float[] colour;

    public Light() {
        on = true;

        colour = new float[4];
        for(int i=0; i<4; i++)
            colour[i] = 1;
    }

    public Light(float[] rgbArray) {
        on = true;

        colour = new float[4];
        for(int i=0; i<3; i++)
            colour[i] = rgbArray[i];
        colour[3] = 1;
    }

    public Light(float red, float green, float blue) {
        on = true;

        colour = new float[4];
        colour[0] = red;
        colour[1] = green;
        colour[2] = blue;
        colour[3] = 1;
    }

    public void setColour(float[] rgbArray) {
        for(int i=0; i<3; i++)
            colour[i] = rgbArray[i];
        colour[3] = 1;
    }

    public void setColour(float r, float g, float b) {
        colour[0] = r;
        colour[1] = g;
        colour[2] = b;
        colour[3] = 1;
    }

    public float[] getColour() {
        return colour;
    }

    @Override
    public Light clone() {
        Light clone = new Light();

        clone.on = on;
        for(int i=0; i<4; i++)
            clone.colour[i] = colour[i];

        return clone;
    }

}
