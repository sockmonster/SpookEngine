package com.spookengine.scenegraph.appearance;

/**
 *
 * @author Oliver Winks
 */
public class PointAtt {

    public boolean isAntiAliased;
    private float pointSize;

    public PointAtt() {
        toDefault();
    }

    public void setTo(PointAtt att) {
        this.isAntiAliased = att.isAntiAliased;
        this.pointSize = att.pointSize;
    }

    public void setPointSize(float pointSize) {
        this.pointSize = pointSize;
    }

    public float getPointSize() {
        return pointSize;
    }

    public void toDefault() {
        isAntiAliased = false;
        pointSize = 1f;
    }

}
