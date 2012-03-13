package com.spookedengine.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Oliver Winks
 */
public final class EventHandler implements Cloneable {
    private static EventHandler instance;

    private Map<String, Queue<Object>> events;
    private Map<String, List<Action>> actions;

    public EventHandler() {
        events = new HashMap<String, Queue<Object>>();
        actions = new HashMap<String, List<Action>>();
    }

    public static EventHandler getInstance() {
        if(instance == null)
            instance = new EventHandler();

        return instance;
    }

    /**
     *
     * @param eventType
     * @param action
     */
    public void listen(String eventType, Action action) {
        if(!actions.containsKey(eventType)) {
            actions.put(eventType, new ArrayList<Action>());
        }

        actions.get(eventType).add(action);
    }

    /**
     *
     * @param eventType
     * @param action
     */
    public void ignore(String eventType, Action action) {
        if(actions.containsKey(eventType)) {
            actions.get(eventType).remove(action);
        }
    }

    public void ignoreAll() {
        actions.clear();
    }

    /**
     * Stores the event object in the events buffer, this event is then posted
     * to all actions listening for events of this type.
     *
     * @param eventType This is a developer defined type, it's simply a string
     * which characterises the event (for example 'touch', 'collision' etc)
     * @param event The object containing data about the particular event (for
     * example MotionEvent, KeyEvent etc).
     */
    public void postEvent(String eventType, Object event) {
        if(!events.containsKey(eventType)) {
            events.put(eventType, new ConcurrentLinkedQueue<Object>());
        }

        if(event != null)
            events.get(eventType).add(event);
    }

    /**
     *
     */
    public void update() {
        // iterate through events
        for(String eventType : events.keySet()) {
            if(!events.get(eventType).isEmpty() && actions.containsKey(eventType)) {
                // perform actions that are listening for these events
                for(Action action : actions.get(eventType))
                    action.perform(events.get(eventType));
            }

            // consume all events
            events.get(eventType).clear();
        }
    }

    @Override
    public Object clone() {
        throw new UnsupportedOperationException("EventHandler is a singleton");
    }

}
