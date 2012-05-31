package com.spookengine.scenegraph.lights;

import com.spookengine.maths.Vec3;
import com.spookengine.scenegraph.renderer.Renderer;

/**
 *
 * @author Oliver Winks
 */
public class DirLight extends Light {

    private Vec3 dir;

    public DirLight() {
        super();
        
        switch(Renderer.coordSys) {
            case Y_UP:
                dir = new Vec3( 0, -1,  0);
                break;
                
            case Z_UP:
                dir = new Vec3( 0,  0, -1);
                break;
        }
    }

    public DirLight(float[] rgbArray, Vec3 dir) {
        super(rgbArray);

        this.dir = new Vec3(dir);
    }

    public void setDir(Vec3 dir) {
        this.dir.setTo(dir);
    }

    public void setDir(float x, float y, float z) {
        dir.setTo(x, y, z);
    }

    public Vec3 getDir() {
        return dir;
    }

    @Override
    public Light clone() {
        DirLight clone = new DirLight();

        clone.on = on;
        
        for(int i=0; i<4; i++)
            clone.colour[i] = colour[i];

        clone.dir.setTo(dir);

        return clone;
    }

}
