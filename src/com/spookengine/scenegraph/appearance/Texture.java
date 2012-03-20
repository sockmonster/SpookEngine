package com.spookengine.scenegraph.appearance;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oliver Winks
 */
public class Texture {

    protected List<ByteBuffer> imgData;
    protected List<Integer> imgWidth;
    protected List<Integer> imgHeight;
    protected int[] texPtr;
    protected boolean hasAlpha;

    public Texture() {
        imgData = new ArrayList<ByteBuffer>();
        imgWidth = new ArrayList<Integer>();
        imgHeight = new ArrayList<Integer>();
        texPtr = new int[1];
    }
    
    public int[] getTexturePointer() {
        return texPtr;
    }

    public void setDimensions(List<Integer> width, List<Integer> height) {
        imgWidth = width;
        imgHeight = height;
    }

    public int getWidth(int i) {
        return imgWidth.get(i);
    }

    public int getHeight(int i) {
        return imgHeight.get(i);
    }
    
    public void setBitmaps(List<ByteBuffer> bitmaps) {
        imgData.clear();
        imgData.addAll(bitmaps);
    }
    
    public void setBitmap(int i, ByteBuffer bitmap) {
        if(i>=0 && i<imgData.size()) {
            imgData.set(i, bitmap);
        }
    }
    
    public void addBitmap(ByteBuffer bitmap) {
        imgData.add(bitmap);
    }
    
    public ByteBuffer removeBitmap(int i) {
        return imgData.remove(i);
    }

    public List<ByteBuffer> getBitmaps() {
        return imgData;
    }
    
    public ByteBuffer getBitmap(int i) {
        return imgData.get(i);
    }

    public boolean hasAlpha() {
        return hasAlpha;
    }

}
