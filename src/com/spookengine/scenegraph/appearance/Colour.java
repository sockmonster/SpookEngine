package com.spookengine.scenegraph.appearance;

/**
 *
 * @author Oliver Winks
 */
public class Colour {

    private float[] colour;

    public Colour() {
        colour = new float[3];
        toDefault();
    }

    public Colour(float... rgbArray) {
        colour = new float[3];
        for(int i=0; i<3; i++) {
            colour[i] = rgbArray[i];
        }
    }

    public void setTo(Colour att) {
        for(int i=0; i<colour.length; i++)
            this.colour[i] = att.colour[i];
    }

    public void setColour(float... rgbArray) {
        for(int i=0; i<3; i++) {
            colour[i] = rgbArray[i];
        }
    }

    public float[] getColour() {
        return colour;
    }
    
    public void toDefault() {
        for(int i=0; i<3; i++)
            colour[i] = 1;
    }

}
