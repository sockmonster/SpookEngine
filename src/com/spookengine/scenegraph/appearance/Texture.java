package com.spookengine.scenegraph.appearance;

import java.nio.ByteBuffer;

/**
 *
 * @author Oliver Winks
 */
public class Texture {
    /** DO NOT MODIFY DIRECTLY! ONLY THE RENDERER SHOULD CHANGE THIS! */
    public boolean texPtrGenerated = false;
    /** DO NOT MODIFY DIRECTLY! ONLY THE RENDERER SHOULD CHANGE THIS! */
    protected int[] texPtr;
    
    // images
    protected ByteBuffer[] bitmaps;
    protected int[] widths;
    protected int[] heights;
    
    // alpha channel
    protected boolean hasAlpha;
    
    // wrapping, repeating, clamping
    public boolean repeatX = true;
    public boolean repeatY = true;

    public Texture() {
        bitmaps = new ByteBuffer[1];
        widths = new int[1];
        heights = new int[1];
        texPtr = new int[1];
    }
    
    public Texture(int mipmapLevels) {
        bitmaps = new ByteBuffer[mipmapLevels];
        widths = new int[mipmapLevels];
        heights = new int[mipmapLevels];
        texPtr = new int[mipmapLevels];
    }
    
    public int[] getTexturePointer() {
        return texPtr;
    }

    public void setDimensions(int[] width, int[] height) {
        if(width.length == height.length && width.length == bitmaps.length) {
            this.widths = width;
            this.heights = height;
        }
    }

    public int getWidth(int i) {
        return widths[i];
    }

    public int getHeight(int i) {
        return heights[i];
    }
    
    public void setBitmaps(ByteBuffer[] bitmaps) {
        if(bitmaps.length == this.bitmaps.length) {
            this.bitmaps = bitmaps;
        }
    }
    
    public void setBitmap(int i, ByteBuffer bitmap) {
        if(i>=0 && i<bitmaps.length) {
            bitmaps[i] = bitmap;
        }
    }

    public ByteBuffer[] getBitmaps() {
        return bitmaps;
    }
    
    public ByteBuffer getBitmap(int i) {
        return bitmaps[i];
    }

    public boolean hasAlpha() {
        return hasAlpha;
    }

}
