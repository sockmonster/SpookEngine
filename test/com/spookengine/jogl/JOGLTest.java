package com.spookengine.jogl;

import com.jogamp.opengl.util.Animator;
import com.spookengine.events.Task;
import com.spookengine.events.Task.TaskState;
import com.spookengine.events.TaskScheduler;
import com.spookengine.maths.Euler;
import com.spookengine.maths.FastMath;
import com.spookengine.maths.Mat3;
import com.spookengine.maths.Vec3;
import com.spookengine.platform.desktop.JOGLRenderer3;
import com.spookengine.platform.desktop.ObjLoader;
import com.spookengine.scenegraph.App;
import com.spookengine.scenegraph.Visual;
import com.spookengine.scenegraph.camera.Cam;
import com.spookengine.scenegraph.camera.CameraMan;
import com.spookengine.scenegraph.lights.LightBulb;
import com.spookengine.scenegraph.lights.LightMan;
import com.spookengine.scenegraph.renderer.Renderer;
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
    private Visual testBox, mirrorBox;
    private CameraMan camMan;
    
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
        camMan.getLocalTransform().tr.setTo(0, 0, 0);
//        camMan.getLocalTransform().ro.rotateTo(FastMath.toRadians(20), 0, 1, 0);
        camMan.updateLocalTransform();
        root.attachChild(camMan);
        
        /* ******** ******** ******** */
        /*          LIGHTING          */
        /* ******** ******** ******** */
        LightBulb sun = new LightBulb(0.8f, 0.8f, 0.8f);
        sun.hasAmbience = true;
        LightMan lm = new LightMan("sun", sun);
        root.attachChild(lm);
        
        root.getLocalAppearance().override(App.LIGHTING);
        root.getLocalAppearance().addLight(sun);
        
        try {
            testBox = ObjLoader.getInstance().loadModel("/com/spookengine/jogl/", "TestCube.obj");
            testBox.getLocalTransform().tr.setTo(-1, 10, 0);
//            testBox.getLocalTransform().ro.rotateToPRY(FastMath.toRadians(0), FastMath.toRadians(90), FastMath.toRadians(0));
            testBox.updateLocalTransform();
//            
//            // get rotation
//            Euler euler = new Euler();
//            testBox.getLocalTransform().ro.getRotationPRY(euler);
//            System.out.println(euler);
//            
            mirrorBox = ObjLoader.getInstance().loadModel("/com/spookengine/jogl/", "TestCube.obj");
            mirrorBox.getLocalTransform().tr.setTo(1, 10, 0);
//            mirrorBox.getLocalTransform().ro.rotateToPRY(euler);
            mirrorBox.updateLocalTransform();
            
            root.attachChild(testBox);
            root.attachChild(mirrorBox);
        } catch(IOException ex) {
            System.err.println("Shit!\n" + ex.getMessage());
        }
        
        // Behvaiour
        TaskScheduler.getInstance().schedule(1, new Task() {
            float angle;
            
            @Override
            public TaskState perform(float tpf) {
                angle += tpf;
                testBox.getLocalTransform().ro.rotateToRPY(angle, angle, angle);
                testBox.updateLocalTransform();
                
                // get rotation
                Euler euler = new Euler();
                testBox.getLocalTransform().ro.getRotationRPY(euler);
                
                mirrorBox.getLocalTransform().ro.rotateToRPY(euler);
                mirrorBox.updateLocalTransform();
                
                return TaskState.CONTINUE_RUN;
            }
        });
    }

    @Override
    public void init(GLAutoDrawable glad) {
        GL2 gl2 = glad.getGL().getGL2();
        Renderer.clearColour = new Vec3(1f, 0.75f, 0.75f);
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
