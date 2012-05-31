package com.spookengine.scenegraph;

import java.util.ArrayList;
import java.util.List;

/**
 * The Node class is the basic construct of the scenegraph. It manages
 * connections between parent and child Nodes.
 *
 * @author Oliver Winks
 */
public abstract class Node implements Cloneable {
    public final Object lock = new Object();
    
    public String name;
    protected Node parent;
    protected List<Node> children;

    public Node() {
        name = "";
        children = new ArrayList<Node>();
    }

    public Node(String name) {
        if(name != null)
            this.name = name;
        else
            this.name = "";

        children = new ArrayList<Node>();
    }

    /**
     * Returns the parent of this Node, if it has one.
     *
     * @return The parent of this Node, null if this Node has no parent.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Finds and returns the Node with the given name by recursively checking
     * all of this Node's decendents. Returns null if no Node can be found with
     * the given name.
     *
     * @param name The name of the Node to search for and return.
     * @return The node of with the given name, null if it doesn't exist.
     */
    public Node findChild(String name) {
        if(this.name != null && this.name.equals(name))
            return this;

        Node result = null;
        for(int i=0; i<children.size(); i++) {
            result = children.get(i).findChild(name);

            if(result != null)
                return result;
        }

        return result;
    }

    /**
     * Returns A list containing this Node's children.
     *
     * @return A list containing this Node's children.
     */
    public List<Node> getChildren() {
        return children;
    }

    /**
     * Attaches the given Node to this Node if it is not already attached.
     *
     * @param child The Node to attach to this Node.
     */
    public void attachChild(Node child) {
        synchronized(lock) {
            if(!children.contains(child)) {
                child.parent = this;
                children.add(child);
            }
        }
    }

    /**
     * Detaches the given Node from this Node if it is attached.
     *
     * @param child The Node to detach from this Node.
     */
    public void detachChild(Node child) {
        synchronized(lock) {
            if(children.contains(child)) {
                child.parent = null;
                children.remove(child);
            }
        }
    }

    /**
     * Detaches and returns the child at the given index, if the index is
     * negative or larger than the number of children then null is returned.
     *
     * @param i The index of the child that will be removed.
     * @return The removed child, null if the index is out of bounds.
     */
    public Node detachChild(int i) {
        Node child = null;
        
        synchronized(lock) {
            child = children.remove(i);
            if(child != null)
                child.parent = null;
        }

        return child;
    }

    /**
     * Reparents this Node to the given Node if it is not already a child of
     * that Node.
     *
     * @param newParent This Node's new parent.
     */
    public void reparent(Node newParent) {
        synchronized(lock) {
            if(!newParent.children.contains(this)) {
                parent.detachChild(this);
                newParent.attachChild(this);
            }
        }
    }

    /**
     * Returns true if this Node is a leaf Node, false otherwise.
     *
     * @return True if this Node is a leaf Node, false otherwise.
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }

    /**
     * Creates a new, identical copy of this Node detached from it's parent.
     * This method provides the mechanism for instancing when called on a Model.
     *
     * @return A new identical copy of this Node, detached from it's parent.
     */
    @Override
    public abstract Node clone();

    /**
     * Creates a new, identical copy of this Node detatched from it's parent.
     * This method recursively clones the subtree rooted at this Node. This
     * method provides the basic mechanism for instnacing when called on a
     * Model.
     *
     * @param parent cloneTree is a recursive method, parent is the cloned
     * parent that cloned children of this Node will be parented to.
     * @return A new, identical copy of this Node, detached from it's parent.
     */
    public abstract Node cloneTree(Node parent);
    
    private String toString(Node node, int depth) {
        String str = "";
        for(int i=0; i<depth; i++)
            str += "  ";
        
        str += node.name + '\n';
        
        for(Node child : node.getChildren())
            str += toString((Spatial) child, depth + 1);
            
        return str;
    }
    
    /**
     * @return a String representation of this node including it's decendants.
     */
    @Override
    public String toString() {
        return toString(this, 0);
    }

}
