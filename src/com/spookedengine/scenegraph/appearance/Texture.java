package com.spookedengine.scenegraph.appearance;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;

/**
 *
 * @author Oliver Winks
 */
public class Texture {

    protected Bitmap[] bitmaps;
    protected ByteBuffer[] imgData;
    protected int[] imgWidth;
    protected int[] imgHeight;
    protected int[] texPtr;
    protected boolean hasAlpha;

    public Texture() {
//        texPtr = null;
        texPtr = new int[1];
    }
    
    public int[] getTexturePointer() {
        return texPtr;
    }

    public void setData(ByteBuffer[] imgData) {
        this.imgData = imgData;
    }

    public void setDimensions(int[] width, int[] height) {
        imgWidth = width;
        imgHeight = height;
    }

    public ByteBuffer getData(int img) {
        return imgData[img];
    }

    public int getWidth(int img) {
        return imgWidth[img];
    }

    public int getHeight(int img) {
        return imgHeight[img];
    }

    public void setBitmap(Bitmap[] bitmaps) {
        this.bitmaps = bitmaps;

        // check if any of the textures are transparent
        for(int i=0; i<bitmaps.length; i++) {
            if(bitmaps[0].hasAlpha()) {
                hasAlpha = true;
                break;
            }
        }
    }

    public Bitmap[] getBitmaps() {
        return bitmaps;
    }

    public boolean hasAlpha() {
        return hasAlpha;
    }

}
