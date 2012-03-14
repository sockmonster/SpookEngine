package com.spookengine.events;

import java.util.Queue;

/**
 *
 * @author Oliver Winks
 */
public abstract class Action {
    public boolean isActive;
    
    public abstract void perform(Queue<Object> events);

}
