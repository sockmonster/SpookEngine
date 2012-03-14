package com.spookengine.game2d;

import com.spookengine.scenegraph.App2;
import com.spookengine.scenegraph.Geom;
import com.spookengine.scenegraph.Trfm2;
import com.spookengine.scenegraph.Trimesh;
import com.spookengine.scenegraph.appearance.AnimTexture;
import com.spookengine.scenegraph.appearance.Texture;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public class Sprite extends Geom<Trfm2, App2> {
    private static final Logger logger = Logger.getLogger(Sprite.class.getName());

    private float width;
    private float height;
    private Map<String, Texture> textures;

    // convenience vars
    private Texture tmpTexture;

    public Sprite(String name, Texture image) {
        super(true, name, Trimesh.Quad(image.getWidth(0), image.getHeight(0)));

        width = image.getWidth(0);
        height = image.getHeight(0);
        textures = new HashMap<String, Texture>();

        logger.log(Level.INFO, "Sprite " + name + " created with size [" + width + ", " + height + "]");

        addTextures(name, image);
        switchTexture(name);
    }
    
    public Sprite(String name, Texture image, float width, float height) {
        super(true, name, Trimesh.Quad(width, height));

        width = image.getWidth(0);
        height = image.getHeight(0);
        textures = new HashMap<String, Texture>();

        logger.log(Level.INFO, "Sprite " + name + " created with size [" + width + ", " + height + "]");

        addTextures(name, image);
        switchTexture(name);
    }
    
    public void resize(float width, float height) {
        // swap the mesh
        this.trimesh = Trimesh.Quad(width, height);
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void addTextures(String name, Texture animation) {
        if(!textures.containsKey(name))
            textures.put(name, animation);
        else
            logger.log(Level.WARNING, "Animation with name " + name +
                    " already exists!");
    }

    public Texture removeTexture(String name) {
        return textures.remove(name);
    }

    public void switchTexture(String name) {
        if(textures.containsKey(name)) {
            if(getLocalAppearance().getTexture(0) != null) {
                /*
                 * remove current texture and, if it's an AnimatedTexture,
                 * stop the animation.
                 */
                tmpTexture = getLocalAppearance().removeTexture(0);
                if(tmpTexture != null && tmpTexture instanceof AnimTexture)
                    ((AnimTexture) tmpTexture).start();
            }

            /*
             * add new texture and, if it's an AnimatedTexture, start the
             * animation.
             */
            tmpTexture = textures.get(name);
            getLocalAppearance().addTexture(tmpTexture);
            if(tmpTexture instanceof AnimTexture)
                ((AnimTexture) tmpTexture).start();
        } else {
            logger.log(Level.WARNING, "Animation with name " + name +
                    " doesn't exist!");
        }
    }

    public void cropTo(int width, int height) {
        //trimesh.setTexCoords(0, );
    }

}
