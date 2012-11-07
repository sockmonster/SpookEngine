package com.spookengine.scenegraph.appearance;

import com.spookengine.scenegraph.appearance.*;
import com.spookengine.core.lights.Light;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public enum LightingPolicy { GLOBAL_FIRST, LOCAL_FIRST }
    public static final int POINT_ATT = 1;
    public static final int LINE_ATT = 2;
    public static final int POLY_ATT = 4;
    public static final int COLOUR = 8;
    public static final int ALPHA = 16;
    public static final int MATERIAL = 32;
    public static final int LIGHTING = 64;
    
    // visual attributes
    protected int inheritance;
    public PointAtt pointAtt;
    public LineAtt lineAtt;
    public PolyAtt polyAtt;
    public Colour colour;
    public Alpha alpha;
    public Material material;
    protected final List<Texture> textures;

    // light state
    public LightingPolicy lightingPolicy;
    protected final List<Light> lights;

    public App() {
        super();

        textures = new ArrayList<Texture>();
        inheritance = LIGHTING; // inherit lights by default
        lightingPolicy = LightingPolicy.LOCAL_FIRST;
        lights = new ArrayList<Light>();
    }
    
    public void setTo(App app) {
        inheritance = app.inheritance;
        pointAtt = app.pointAtt;
        lineAtt = app.lineAtt;
        polyAtt = app.polyAtt;
        colour = app.colour;
        alpha = app.alpha;
        material = app.material;

        textures.clear();
        for(int i=0; i<app.textures.size(); i++)
            textures.add(app.textures.get(i));

        lightingPolicy = app.lightingPolicy;
        lights.clear();
        int nLights = app.lights.size();
        for(int i=0; i<nLights; i++) {
            lights.add(app.lights.get(i));
        }
    }
    
    public void toDefault() {
        inheritance = 64; // inherit lights by default
        pointAtt = null;
        lineAtt = null;
        polyAtt = null;
        colour = null;
        alpha = null;
        material = null;
        
        textures.clear();
        lights.clear();
    }
    
    public void addTexture(Texture texture) {
        if(texture != null)
            textures.add(texture);
        else
            logger.log(Level.WARNING, "Cannot add a NULL texture!");
    }

    public void removeTexture(Texture texture) {
        textures.remove(texture);
    }

    public Texture removeTexture(int i) {
        return textures.remove(i);
    }

    public List<Texture> getTextures() {
        return textures;
    }

    public Texture getTexture(int i) {
        if(i >= 0 && i < textures.size())
            return textures.get(i);

        return null;
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public void removeLight(Light light) {
        lights.remove(light);
    }

    public Light removeLight(int i) {
        return lights.remove(i);
    }

    public List<Light> getLights() {
        return lights;
    }
    
    public int getInheritance() {
        return inheritance;
    }
    
    public void override(int options) {
        inheritance = (inheritance | options);
    }

    public void inherit(int options) {
        inheritance = inheritance & ~options;
    }
    
    public void add(App app) {
        if( (app.inheritance & POINT_ATT) != 0 ) {
            inheritance |= POINT_ATT;
            pointAtt = app.pointAtt;
        }

        if( (app.inheritance & LINE_ATT) != 0 ) {
            inheritance |= LINE_ATT;
            lineAtt = app.lineAtt;
        }

        if( (app.inheritance & POLY_ATT) != 0 ) {
            inheritance |= POLY_ATT;
            polyAtt = app.polyAtt;
        }

        if( (app.inheritance & COLOUR) != 0 ) {
            inheritance |= COLOUR;
            colour = app.colour;
        }

        if( (app.inheritance & MATERIAL) != 0 ) {
            inheritance |= MATERIAL;
            material = app.material;
        }

        if( (app.inheritance & ALPHA) != 0 ) {
            inheritance |= ALPHA;
            alpha = app.alpha;
        }

        // textures are not inherited!
        textures.clear();
        int nTextures = app.textures.size();
        for(int i=0; i<nTextures; i++)
            textures.add(app.textures.get(i));

        int nLights = app.lights.size();
        if( (app.inheritance & LIGHTING) != 0 ) {
            inheritance |= LIGHTING;
            // add local lights
            for(int i=0; i<app.lights.size(); i++) {
                if(!lights.contains(app.lights.get(i)))
                    lights.add(app.lights.get(i));
            }
        } else {
            lights.clear();
            for(int i=0; i<nLights; i++)
                lights.add(app.lights.get(i));
        }
    }

}
