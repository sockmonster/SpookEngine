package com.spookengine.scenegraph.collision;

import com.spookengine.core.events.EventHandler;
import com.spookengine.scenegraph.Bound;
import com.spookengine.scenegraph.Node;
import com.spookengine.scenegraph.collision.CollisionEvent.EventType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The CollisionDetector checks the scenegraph's bounding volume hierarchy for
 * collisions.
 *
 * The collision detection algorithm used by this CollisionDetector is as follows:
 * <ul>
 * <li> First, a recursive search is made of the tree starting from the root to
 * find 'collider' objects (any object that has a bounding volume and is a leaf
 * node).
 * <li> Once a 'collider' has been found a second recursive function is called
 * which accends the tree to the root by following the parent links.
 * <li> Each time the accent function moves up a level to a parent node, the
 * child node that it came from is passed as a paramater. This allows the
 * accent function to ignore all links to the 'left' of that child.
 * <li> At each parent a third function is called that recursively checks the
 * subtree (rooted at the parent) for collisions. <i>(As mentioned above; only
 * links to the 'right' of the child that the function accended from are
 * followed)</i>
 * </ul>
 *
 * <pre>
 * NOTE: This algorithm requires the scenegraph to be ordered 
 * such that all leaf nodes are on the left of non-leaf nodes.
 *
 * For example:
 * This is legal
 *                  0
 *                /   \
 *               /     \
 *              0       0
 *                     / \
 *                    /   \
 *                   0     0
 *
 * This is NOT legal
 *                  0
 *                /   \
 *               /     \
 *              0       0
 *             / \
 *            /   \
 *           0     0
 * </pre>
 * @author Oliver Winks
 */
public class CollisionDetector {
    private static Map<String, CollisionDetector> instances = new HashMap<String, CollisionDetector>();

    private List<CollisionEvent> events;

    // convenience vars
    private Bound collider;
    private CollisionEvent evt;
    private EventHandler eventHandler;

    private CollisionDetector() {
        events = new LinkedList<CollisionEvent>();
        evt = null;
    }

    public static CollisionDetector getInstance(String name) {
        CollisionDetector detector = instances.get(name);
        if(detector == null)
            instances.put(name, detector = new CollisionDetector());

        return detector;
    }

    private boolean hasBounds(Node node) {
        return node instanceof Bound && ((Bound) node).getBounds() != null;
    }

    private void checkCollisions(Node node) {
        boolean evtReUsed = false;
        boolean shouldRecurse = true;
        if(hasBounds(node)) {
            if(((Bound) node).boundGroup == collider.boundGroup &&
                    ((Bound) node).getBounds().collides(collider.getBounds())) {
                if(node.isLeaf()) {
                    /* 
                     * convert collision event if it already exists.
                     */
                    for(int i=0; i<events.size(); i++) {
                        evt = events.get(i);
                        if(!evt.inUse && evt.isCollisionBetween(collider, (Bound) node)) {
                            // modify event
                            evtReUsed = true;
                            evt.type = EventType.COLLISION;
                            evt.inUse = evtReUsed;

                            break;
                        }
                    }

                    /*
                     * else create a new event
                     */
                    if(!evtReUsed) {
                        evt = new CollisionEvent();
                        evt.type = EventType.COLLISION_ENTER;
                        evt.inUse = true;
                        events.add(evt);
                    }

                    // fill out collision event and post
                    evt.A = collider;
                    evt.B = (Bound) node;
                    eventHandler.postEvent("collision_evt", evt);

                    shouldRecurse = false;
                }
            } else {
                shouldRecurse = false;
            }
        }

        // recurse?
        if(shouldRecurse) {
            for(int i=0; i<node.getChildren().size(); i++) {
                checkCollisions(node.getChildren().get(i));
            }
        }
    }

    private void accend(Node parent, Node child) {
        if(parent != null) {
            // keep going right!
            int start = parent.getChildren().indexOf(child) + 1;
            for(int i=start; i<parent.getChildren().size(); i++) {
                checkCollisions(parent.getChildren().get(i));
            }

            // accend
            accend(parent.getParent(), parent);
        }
    }

    private void findColliders(Node node) {
        if(hasBounds(node) && node.isLeaf()) {
            /*
             * collider found! Accend the tree checking for collisions on all
             * subtrees to the right until the whole tree has been searched.
             */
            collider = (Bound) node;
            accend(node.getParent(), node);
        } else {
            // recurse
            for(int i=0; i<node.getChildren().size(); i++)
                findColliders(node.getChildren().get(i));
        }
    }

    public void detect(Node root, EventHandler eventHandler) {
        collider = null;
        findColliders(root);

        // check for collision exit events / reset events
        for(int i=0; i<events.size(); i++) {
            evt = events.get(i);
            if(evt.inUse) {
                // reset
                evt.inUse = false;
            } else if(evt.type != EventType.COLLISION_EXIT) {
                // convert event to collision exit
                evt.type = EventType.COLLISION_EXIT;
                this.eventHandler = eventHandler;
                this.eventHandler.postEvent("collision_evt", evt);
            } else {
                // dispose of the event
                events.remove(i);
            }
        }
    }

}
