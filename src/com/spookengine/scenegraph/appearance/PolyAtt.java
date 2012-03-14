package com.spookengine.scenegraph.appearance;

/**
 *
 * @author Oliver Winks
 */
public class PolyAtt {
    public enum CullFace {FRONT, BACK, NONE};

    public CullFace cullFace;

    public PolyAtt() {
        toDefault();
    }

    public void setTo(PolyAtt att) {
        this.cullFace = att.cullFace;
    }
    
    public void toDefault() {
        cullFace = CullFace.NONE;
    }

}
