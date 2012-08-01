package com.spookengine.scenegraph.renderer;

import com.spookengine.maths.Vec3;
import com.spookengine.scenegraph.*;
import com.spookengine.scenegraph.camera.Cam;
import com.spookengine.scenegraph.collision.BoundingVolume;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public abstract class Renderer {
    private static final Logger logger = Logger.getLogger(Renderer.class.getName());
    
    public static boolean renderBounds;
    public static int canvasWidth;
    public static int canvasHeight;
    public static Vec3 clearColour = new Vec3(1, 1, 1);
    
    // traversal vars
    protected boolean canvasChanged;
    protected Cam camera;
    protected Trfm worldTransform;
    protected App worldAppearance;

    protected Stack<Spatial> spatialStack;
    protected Stack<Visual> visualStack;
    protected Stack<Visual> renderStack;
    protected Stack<Visual> deferredStack;
    protected Stack<BoundingVolume> boundsStack;

    protected boolean spatialStackEmpty;
    protected boolean visualStackEmpty;
    
    protected Renderer() {
        spatialStack = new Stack<Spatial>();
        visualStack = new Stack<Visual>();
        renderStack = new Stack<Visual>();
        deferredStack = new Stack<Visual>();
        boundsStack = new Stack<BoundingVolume>();
    }
    
    public abstract void onSurfaceCreated();
    
    public abstract void onSurfaceChanged(int width, int height);
    
    public abstract void onDrawFrame(Spatial root, Cam cam);
    
    protected void update(Node node) {
        synchronized(node.lock) {
            /* ******** ******** ******** */
            /*   UPDATE WORLD TRANSFORM   */
            /* ******** ******** ******** */
            boolean isLeaf;
            boolean spatialObjectPushed = false;
            boolean visualObjectPushed = false;
            if(node instanceof Spatial) {
                logger.log(Level.FINEST, "Updating {0}", node.name);
                Spatial spatial = (Spatial) node; // TODO: CHECK IF THIS CREATES GARBAGE!
                isLeaf = spatial.isLeaf();

                // apply the transform on this spatial object?
                boolean localTransformModified = spatial.hasTransformed();
                boolean worldTransformModified = spatialStackEmpty ? false : spatialStack.peek().hasTransformed();
                if(worldTransformModified || localTransformModified) {
                    logger.log(Level.FINEST, "\n{0}", worldTransform.at);
                    logger.log(Level.FINEST, "****\n{0}", spatial.getLocalTransform().at);
                    
                    // push world transform
                    worldTransform.add(spatial.getLocalTransform());
                    
                    logger.log(Level.FINEST, "====\n{0}", worldTransform.at);

                    spatial.applyTransform(worldTransform);
                } else if(!isLeaf) {
                    worldTransform.setTo(spatial.getWorldTransform());
                }
                spatialObjectPushed = true;
                spatialStackEmpty = false;
                spatialStack.push(spatial);

                /* ******** ******** ******** */
                /* UPDATE WORLD VISUAL STATE  */
                /* ******** ******** ******** */
                if(node instanceof Visual) {
                    Visual visual = (Visual) node; // TODO: CHECK IF THIS CREATES GARBAGE!

                    // push world appearance
    //                worldAppearance.add(visual.getLocalAppearance());

                    boolean localVisualStateChanged = visual.hasVisualStateChanged();
                    boolean worldVisualStateChanged = visualStackEmpty ? false : visualStack.peek().hasVisualStateChanged();
                    if(worldVisualStateChanged || localVisualStateChanged) {
                        // push world appearance
                        worldAppearance.add(visual.getLocalAppearance());

                        // apply the world appearance on this visual object
                        visual.applyAppearance(worldAppearance);
                    } else if(!isLeaf) {
                        worldAppearance.setTo(visual.getWorldAppearance());
                    }
                    visualObjectPushed = true;
                    visualStackEmpty = false;
                    visualStack.push(visual);
                }
            }

            /* ******** ******** ******** */
            /*          RECURSE           */
            /* ******** ******** ******** */
            int nChildren = node.getChildren().size();
            for(int i=0; i<nChildren; i++) {
                update(node.getChildren().get(i));

                // pop world transform, light state & appearance
                worldTransform.setTo(spatialStack.peek().getWorldTransform());

                if(!visualStackEmpty)
                    worldAppearance.setTo(visualStack.peek().getWorldAppearance());
            }
            if(spatialObjectPushed) {
                spatialStack.pop();

                if(visualObjectPushed)
                    visualStack.pop();
            }
        }
    }
    
    protected int boundAndStack(Node node) {
        synchronized(node.lock) {
            int boundedChildren = 0;
            int nChildren = node.getChildren().size();
            for(int i=0; i<nChildren; i++)
                boundedChildren += boundAndStack(node.getChildren().get(i));

            boolean isVisible = true;
            if(node instanceof Bound) {
                Bound bound = (Bound) node; // TODO: CHECK IF THIS CREATES GARBAGE!

                if(bound.getBounds() != null) {
                    bound.updateBounds(boundsStack, boundsStack.size() - boundedChildren, boundsStack.size());

                    /*
                    * clear the childBounds list because this
                    * BoundingVolume now contains all child bounds.
                    */
                    if(!node.isLeaf()) {
                        for(int i=0; i<boundedChildren; i++)
                            boundsStack.pop();
                    }
                    boundsStack.push(bound.getBounds());

                    // is visible?
                    if(!camera.isVisible(bound.getBounds())) {
                        isVisible = false;
                    }

                    boundedChildren = 1;
                }
            }

            // render
            if(node instanceof Spatial) {
                ((Spatial) node).hasTransformed(false);

                if(node instanceof Visual && !((Visual) node).isHidden) {
                    Visual visual = (Visual) node; // TODO: CHECK IF THIS CREATES GARBAGE!
                    visual.hasVisualStateChanged(false);

                    if(isVisible) {
                        // defer rendering?
                        App worldApp = visual.getWorldAppearance();
                        int nTextures = worldApp.getTextures().size();
                        boolean hasTransparentTexture = false;
                        for(int i=0; i<nTextures; i++) {
                            if(worldApp.getTextures().get(i).hasAlpha()) {
                                hasTransparentTexture = true;
                                break;
                            }
                        }

                        if(worldApp.alpha == null && !hasTransparentTexture) {
                            renderStack.push(visual);
                        } else {
                            deferredStack.push(visual);
                        }
                    }
                }
            }

            return boundedChildren;
        }
    }
    
}
