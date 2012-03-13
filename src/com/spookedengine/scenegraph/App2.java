package com.spookedengine.scenegraph;

import com.spookedengine.scenegraph.appearance.Texture;
import java.util.ArrayList;

/**
 *
 * @author Oliver Winks
 */
public class App2 extends App {

    public App2() {
        super();

        textures = new ArrayList<Texture>();
        inheritance = 0;
    }

    /**
     * Add the given VisualState to this VisualState.
     *
     * @param appearance The VisualState to add to this VisualState.
     */
    @Override
    public void add(App app) {
        if( (app.inheritance & POINT_ATT) != 0 ) {
            inheritance |= POINT_ATT;
            pointAtt = ((App2) app).pointAtt;
        }

        if( (app.inheritance & LINE_ATT) != 0 ) {
            inheritance |= LINE_ATT;
            lineAtt = ((App2) app).lineAtt;
        }

        if( (app.inheritance & POLY_ATT) != 0 ) {
            inheritance |= POLY_ATT;
            polyAtt = ((App2) app).polyAtt;
        }

        if( (app.inheritance & COLOUR) != 0 ) {
            inheritance |= COLOUR;
            colour = ((App2) app).colour;
        }

        if( (app.inheritance & ALPHA) != 0 ) {
            inheritance |= ALPHA;
            alpha = ((App2) app).alpha;
        }

        // textures are not inherited!
        textures.clear();
        int nTextures = ((App2) app).textures.size();
        for(int i=0; i<nTextures; i++)
            textures.add(((App2) app).textures.get(i));
    }

}
