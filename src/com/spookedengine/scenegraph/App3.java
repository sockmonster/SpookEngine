package com.spookedengine.scenegraph;

import com.spookedengine.scenegraph.App.LightingPolicy;
import com.spookedengine.scenegraph.appearance.Material;
import com.spookedengine.scenegraph.appearance.Texture;
import com.spookedengine.scenegraph.lights.Light;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oliver Winks
 */
public class App3 extends App {

    protected Material material;

    // light state
    protected LightingPolicy lightingPolicy;
    protected List<Light> lights;

    public App3() {
        super();

        textures = new ArrayList<Texture>();
        inheritance = LIGHTING; // inherit lights by default
        lightingPolicy = LightingPolicy.LOCAL_FIRST;
        lights = new ArrayList<Light>();
    }

    @Override
    public void setTo(App app) {
        super.setTo(app);

        material = ((App3) app).material;

        lightingPolicy = ((App3) app).lightingPolicy;
        lights.clear();
        int nLights = ((App3) app).lights.size();
        for(int i=0; i<nLights; i++) {
            lights.add(((App3) app).lights.get(i));
        }
    }

    @Override
    public void toDefault() {
        super.toDefault();

        inheritance = 64; // inherit lights by default
        material = null;
        lights.clear();
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public LightingPolicy getLightingPolicy() {
        return lightingPolicy;
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

    @Override
    public void add(App app) {
        if( (app.inheritance & POINT_ATT) != 0 ) {
            inheritance |= POINT_ATT;
            pointAtt = ((App3) app).pointAtt;
        }

        if( (app.inheritance & LINE_ATT) != 0 ) {
            inheritance |= LINE_ATT;
            lineAtt = ((App3) app).lineAtt;
        }

        if( (app.inheritance & POLY_ATT) != 0 ) {
            inheritance |= POLY_ATT;
            polyAtt = ((App3) app).polyAtt;
        }

        if( (app.inheritance & COLOUR) != 0 ) {
            inheritance |= COLOUR;
            colour = ((App3) app).colour;
        }

        if( (app.inheritance & MATERIAL) != 0 ) {
            inheritance |= MATERIAL;
            material = ((App3) app).material;
        }

        if( (app.inheritance & ALPHA) != 0 ) {
            inheritance |= ALPHA;
            alpha = ((App3) app).alpha;
        }

        // textures are not inherited!
        textures.clear();
        int nTextures = ((App3) app).textures.size();
        for(int i=0; i<nTextures; i++)
            textures.add(((App3) app).textures.get(i));

        int nLights = ((App3) app).lights.size();
        if( (app.inheritance & LIGHTING) != 0 ) {
            inheritance |= LIGHTING;
            // add local lights
            for(int i=0; i<((App3) app).lights.size(); i++) {
                if(!lights.contains(((App3) app).lights.get(i)))
                    lights.add(((App3) app).lights.get(i));
            }
        } else {
            lights.clear();
            for(int i=0; i<nLights; i++)
                lights.add(((App3) app).lights.get(i));
        }
    }

}
