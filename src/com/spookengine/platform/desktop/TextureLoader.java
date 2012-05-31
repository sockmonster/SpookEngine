package com.spookengine.platform.desktop;

import com.spookengine.scenegraph.appearance.Texture;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.nio.ByteBuffer;

/**
 *
 * @author Oliver Winks
 */
public class TextureLoader {
    
    private static TextureLoader instance;
    
    private TextureLoader() {
    }
    
    public static TextureLoader getInstance() {
        if(instance == null)
            instance = new TextureLoader();
        
        return instance;
    }
    
    /**
     * Loads a texture from a BufferedImage
     * @param image
     * @return 
     */
    public Texture loadTexture(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        WritableRaster raster = Raster.createInterleavedRaster(
                DataBuffer.TYPE_BYTE, width, height, 4, null);
        
        ComponentColorModel colorModel = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] {8,8,8,8},
                true,
                false,
                ComponentColorModel.TRANSLUCENT,
                DataBuffer.TYPE_BYTE);
        
        BufferedImage texImage = new BufferedImage(colorModel, raster, false, null);
        
        Graphics2D g2d = texImage.createGraphics();
        g2d.drawImage(image, null, null);
        DataBufferByte texBuf = (DataBufferByte) raster.getDataBuffer();
        byte[] texRGBA = texBuf.getData();
        
        ByteBuffer bb = ByteBuffer.wrap(texRGBA);
        bb.position(0);
        bb.mark();
        
        Texture tex = new Texture();
        tex.setBitmaps(new ByteBuffer[] {bb});
        tex.setDimensions(new int[] {image.getWidth()}, new int[] {image.getHeight()});
        
        return tex;
    }
    
}
