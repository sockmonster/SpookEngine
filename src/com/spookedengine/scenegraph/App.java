package com.spookedengine.scenegraph;

import com.spookedengine.scenegraph.appearance.Alpha;
import com.spookedengine.scenegraph.appearance.Colour;
import com.spookedengine.scenegraph.appearance.LineAtt;
import com.spookedengine.scenegraph.appearance.PointAtt;
import com.spookedengine.scenegraph.appearance.PolyAtt;
import com.spookedengine.scenegraph.appearance.Texture;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public abstract class App {
    private static Logger logger = Logger.getLogger(App.class.getName());

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
    protected PointAtt pointAtt;
    protected LineAtt lineAtt;
    protected PolyAtt polyAtt;
    protected Colour colour;
    protected Alpha alpha;
    protected List<Texture> textures;

    public void setTo(App app) {
        inheritance = app.inheritance;
        pointAtt = app.pointAtt;
        lineAtt = app.lineAtt;
        polyAtt = app.polyAtt;
        colour = app.colour;
        alpha = app.alpha;

        textures.clear();
        for(int i=0; i<app.textures.size(); i++)
            textures.add(app.textures.get(i));
    }

    /**
     * Resets this App to the defulat settings.
     *
     * <i>
     * This method doesn't create any garbage because all appearance
     * attributes are shared between nodes. Therefore removing a pointer to an
     * appearance attribute does not orphan that attribute as it is still
     * pointed to by an App somewhere in the scenegraph!
     * </i>
     */
    public void toDefault() {
        inheritance = 0;
        pointAtt = null;
        lineAtt = null;
        polyAtt = null;
        colour = null;
        alpha = null;
        textures.clear();
    }
    
    public int getInheritance() {
        return inheritance;
    }

    public PointAtt getPointAtt() {
        return pointAtt;
    }

    public void setPointAtt(PointAtt pointAtt) {
        this.pointAtt = pointAtt;
    }

    public LineAtt getLineAtt() {
        return lineAtt;
    }

    public void setLineAtt(LineAtt lineAtt) {
        this.lineAtt = lineAtt;
    }

    public PolyAtt getPolyAtt() {
        return polyAtt;
    }

    public void setPolyAtt(PolyAtt polyAtt) {
        this.polyAtt = polyAtt;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colouring) {
        this.colour = colouring;
    }

    public Alpha getAlpha() {
        return alpha;
    }

    public void setAlpha(Alpha alpha) {
        this.alpha = alpha;
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

    /**
     * Add the given VisualState to this VisualState.
     *
     * @param appearance The VisualState to add to this VisualState.
     */
    public abstract void add(App app);

    public void override(int options) {
        inheritance = (inheritance | options);
    }

    public void inherit(int options) {
        inheritance = inheritance & ~options;
    }

}
