package com.spookengine.maths;

/**
 *
 * @author Oliver Winks
 */
public class FastMath {
    private static boolean isInitialized = false;
    private static final float[] sinTable = new float[720];
    private static final float[] cosTable = new float[720];
    private static final float step = (float) (2f * Math.PI / 720);
    public static final float PI = (float) Math.PI;
    public static final float PI2 = PI*2;
    public static final float HALFPI = PI/2;

    /**
     * build sin table with size elements
     * @param size
     */
    private static final void build() {
        // build sin table
        for(int i=0; i<720; i++)
            sinTable[i] = (float) Math.sin(step*i);

        // build cos table
        for(int i=0; i<720; i++)
            cosTable[i] = (float) Math.cos(step*i);

        isInitialized = true;
    }

    public static final float toRadians(float a) {
        return a*0.0174532925f;
    }

    public static final float toDegrees(float a) {
        return a*57.2957795f;
    }

    /**
     * Not sure why this is quicker than Math.abs but it is!
     * Returns the absolute value of a.
     *
     * @param a The value to get the absolute of.
     * @return The absolute value of a
     */
    public static final float abs(float a) {
        int sign = a<0 ? -1 : 1;
        return sign*a;
    }

    /**
     * Calculates fast sin, but with low precision.
     *
     * @param a angle in radians.
     * @return sin of angle a.
     */
    public static final float sin(float a) {
        if(!isInitialized)
            build();

        int sign = a <0 ? -1 : 1;
        a = a%6.283185308f;

        int index = (int) ((sign*a)/step);
        return sign*sinTable[index];
    }

    /**
     * Calculates fast cos, but with low prescision.
     *
     * @param a angle in radians.
     * @return cos of angle a.
     */
    public static final float cos(float a) {
        if(!isInitialized)
            build();

        int sign = a <0 ? -1 : 1;
        a = a%6.283185308f;

        int index = (int) ((sign*a)/step);
        return cosTable[index];
    }

}
