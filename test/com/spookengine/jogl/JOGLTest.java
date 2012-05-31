package com.spookengine.jogl;

import com.jogamp.opengl.util.Animator;
import com.spookengine.events.Task;
import com.spookengine.events.Task.TaskState;
import com.spookengine.events.TaskScheduler;
import com.spookengine.maths.Vec3;
import com.spookengine.platform.desktop.JOGLRenderer3;
import com.spookengine.scenegraph.*;
import com.spookengine.scenegraph.appearance.Material;
import com.spookengine.scenegraph.camera.Cam;
import com.spookengine.scenegraph.camera.CameraMan;
import com.spookengine.scenegraph.lights.LightBulb;
import com.spookengine.scenegraph.lights.LightMan;
import com.spookengine.scenegraph.renderer.Renderer;
import com.spookengine.scenegraph.renderer.Renderer.CoordinateSystem;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLJPanel;

/**
 *
 * @author Oliver Winks
 */
public class JOGLTest implements GLEventListener {
    
    private Visual root;
    private CameraMan camMan;
    private Spatial G1;
    private Spatial G2;
    
    public static void main(String[] args) {
        JOGLTest test = new JOGLTest();
    }
    
    public JOGLTest() {
        // setup JOGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities glCaps = new GLCapabilities(glp);
        GLJPanel glCanvas = new GLJPanel(glCaps);
        
        // setup window
        Frame frame = new Frame();
        frame.setSize(600, 400);
        frame.add(glCanvas);
        frame.setVisible(true);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        // start render loop
        glCanvas.addGLEventListener(this);
        
        Animator animator = new Animator();
        animator.add(glCanvas);
        animator.start();
    }
    
    private void setupScene() throws IOException {
        root = new Visual("root");
        
        /* ******** ******** ******** */
        /*           Camera           */
        /* ******** ******** ******** */
        camMan = new CameraMan("camera", new Cam());
        camMan.getLocalTransform().tr.add( 0, 0, 0);
        camMan.updateLocalTransform();
        root.attachChild(camMan);
        
        /* ******** ******** ******** */
        /*          LIGHTING          */
        /* ******** ******** ******** */
        LightBulb sun = new LightBulb(0.8f, 0.8f, 0.8f);
        sun.hasAmbience = true;
        LightMan lm = new LightMan("sun", sun);
        lm.getLocalTransform().tr.add(3, 7, -3);
        lm.updateLocalTransform();
        root.attachChild(lm);
        root.getLocalAppearance().addLight(sun);
        
        // Parent
        final Spatial parent = new Spatial("parent");
        parent.getLocalTransform().tr.setTo(4, 10, 0);
        parent.updateLocalTransform();
        root.attachChild(parent);
        
        Geom parentBox = new Geom("parent_box", Trimesh.Cube(2, 2, 2));
        parentBox.getLocalAppearance().override(App.MATERIAL);
        parentBox.getLocalAppearance().material = new Material(new float[] {0.7f, 0.2f, 0.2f});
        parent.attachChild(parentBox);
        
//        // Child
//        final Spatial child = new Spatial("child");
//        child.getLocalTransform().tr.add(0, -3, 0);
//        child.updateLocalTransform();
//        parent.attachChild(child);
//        
//        Geom childBox = new Geom("child_box", Trimesh.Cube(1, 1, 1));
//        childBox.getLocalAppearance().override(App.MATERIAL);
//        childBox.getLocalAppearance().material = new Material(new float[] {0.2f, 0.2f, 0.7f});
//        child.attachChild(childBox);
        
        // Behvaiour
        TaskScheduler.getInstance().schedule(1, new Task() {
            @Override
            public TaskState perform(float tpf) {
//                parent.getLocalTransform().ro.rotateByXYZ(tpf, tpf, tpf);
//                parent.updateLocalTransform();
                
//                child.getLocalTransform().ro.rotateByZXY(tpf, 0, 0);
//                child.updateLocalTransform();
                
//                camMan.getLocalTransform().ro.rotateBy(tpf, 1, 0, 0);
//                camMan.updateLocalTransform();
                
                return TaskState.CONTINUE_RUN;
            }
        });
    }

    @Override
    public void init(GLAutoDrawable glad) {
        GL2 gl2 = glad.getGL().getGL2();
        Renderer.clearColour = new Vec3(1f, 0.75f, 0.75f);
        Renderer.coordSys = CoordinateSystem.Z_UP;
        JOGLRenderer3.getInstance(gl2).onSurfaceCreated();
        
        // setup scene
        try {
            setupScene();
        } catch (IOException ex) {
            Logger.getLogger(JOGLTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        GL2 gl2 = glad.getGL().getGL2();
        JOGLRenderer3.getInstance(gl2).onSurfaceChanged(width, height);
    }
    
    @Override
    public void display(GLAutoDrawable glad) {
        GL2 gl2 = glad.getGL().getGL2();
        JOGLRenderer3.getInstance(gl2).onDrawFrame(root, camMan.getCamera());
        
        TaskScheduler.getInstance().update();
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
    }
    
}
