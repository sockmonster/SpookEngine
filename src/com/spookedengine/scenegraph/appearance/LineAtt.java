package com.spookedengine.scenegraph.appearance;

/**
 *
 * @author Oliver Winks
 */
public class LineAtt {

    public boolean isAntiAliased;
    private float lineWidth; // TODO: ISSUE WITH ANDROID MEANS THIS DOESN'T WORK!

    public LineAtt() {
        toDefault();
    }

    public void setTo(LineAtt att) {
        this.isAntiAliased = att.isAntiAliased;
        this.lineWidth = att.lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        if(lineWidth >= 0)
            this.lineWidth = lineWidth;
    }

    public float getLineWidth() {
        return lineWidth;
    }
    
    public void toDefault() {
        isAntiAliased = false;
        lineWidth = 1f;
    }

}
