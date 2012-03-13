package com.spookedengine.scenegraph.collision;

import com.spookedengine.scenegraph.Bound;

/**
 *
 * @author Oliver Winks
 */
public class CollisionEvent {
    public enum EventType {COLLISION_ENTER, COLLISION, COLLISION_EXIT};

    protected boolean inUse;
    protected EventType type;
    protected Bound A;
    protected Bound B;
    
    public EventType getType() {
        return type;
    }

    public Bound getA() {
        return A;
    }

    public Bound getB() {
        return B;
    }

    protected boolean isCollisionBetween(Bound A, Bound B) {
        if(this.A == A && this.B == B)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "" + type + "";
    }

}
