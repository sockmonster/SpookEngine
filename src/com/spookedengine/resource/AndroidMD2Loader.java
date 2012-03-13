package com.spookedengine.resource;

import android.app.Activity;
import com.spookedengine.resource.ResourceManager.ResourceScope;
import com.spookedengine.scenegraph.Trfm3;
import com.spookedengine.scenegraph.Visual;
import com.spookedengine.scenegraph.App3;
import java.io.DataInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public class AndroidMD2Loader extends ModelLoader {
    private Logger logger = Logger.getLogger(AndroidOBJLoader.class.getName());
    private Activity context;

    private DataInputStream dis;

    // constants
    private static final int MD2_MAGIC = 844121161;
    private static final int MD2_VERSION = 8;

    // HEADER
    private int magic;              // This is used to identify the file
    private int version;            // The version number of the file (Must be 8)
    private int skinWidth;          // The skin width in pixels
    private int skinHeight;         // The skin height in pixels
    private int frameSize;          // The size in bytes the frames are
    private int nSkins;           // The number of skins associated with the model
    private int nVertices;        // The number of vertices (constant for each frame)
    private int nTexCoords;       // The number of texture coordinates
    private int nTriangles;       // The number of faces (polygons)
    private int nGlCommands;      // The number of gl commands
    private int nFrames;          // The number of animation frames
    private int offsetSkins;        // The offset in the file for the skin data
    private int offsetTexCoords;    // The offset in the file for the texture data
    private int offsetTriangles;    // The offset in the file for the face data
    private int offsetFrames;       // The offset in the file for the frames data
    private int offsetGlCommands;   // The offset in the file for the gl commands data
    private int offsetEnd;          // The end of the file offset

    public AndroidMD2Loader(Activity context) {
        this.context = context;
    }

    private int swapEndian(int i) {
        return (
                ((i&0x000000FF)<<24)+
                ((i&0x0000FF00)<< 8)+
                ((i&0x00FF0000)>> 8)+
                ((i&0xFF000000)>>24)
                );
    }

    @Override
    public Visual<Trfm3, App3> loadModel(ResourceScope scope, String filename) throws Exception {
        logger.log(Level.INFO, "Loading Model!");
        Visual<Trfm3, App3> visObj = null;

        // load model
        dis = new DataInputStream(context.getAssets().open(filename));

        magic = swapEndian(dis.readInt());
        version = swapEndian(dis.readInt());
        skinWidth = swapEndian(dis.readInt());
        skinHeight = swapEndian(dis.readInt());
        frameSize = swapEndian(dis.readInt());
        nSkins = swapEndian(dis.readInt());
        nVertices = swapEndian(dis.readInt());
        nTexCoords = swapEndian(dis.readInt());
        nTriangles = swapEndian(dis.readInt());
        nGlCommands = swapEndian(dis.readInt());
        nFrames = swapEndian(dis.readInt());
        offsetSkins = swapEndian(dis.readInt());
        offsetTexCoords = swapEndian(dis.readInt());
        offsetTriangles = swapEndian(dis.readInt());
        offsetFrames = swapEndian(dis.readInt());
        offsetGlCommands = swapEndian(dis.readInt());
        offsetEnd = swapEndian(dis.readInt());

        System.out.println(skinWidth + " " + skinHeight);
        if(magic != MD2_MAGIC || version != MD2_VERSION) {
            logger.log(Level.WARNING, "File is not an MD2 file!");
            return null;
        }

        for(int i=0; i<nFrames; i++) {

        }

        return visObj;
    }

}
