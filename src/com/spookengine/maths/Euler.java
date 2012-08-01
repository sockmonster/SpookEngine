package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class Euler {
    public float yaw;
    public float pitch;
    public float roll;
    
    public Euler() {
    }
    
    public Euler(float yaw, float pitch, float roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }
    
    @Override
    public String toString() {
        return "[ Yaw: " + yaw + ", Pitch: " + pitch + ", Roll: " + roll + " ]";
    }
}
