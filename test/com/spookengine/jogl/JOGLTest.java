package com.spookengine.jogl;

import com.jogamp.opengl.util.Animator;
import com.spookengine.core.camera.Cam;
import com.spookengine.core.camera.CameraMan;
import com.spookengine.core.events.Task;
import com.spookengine.core.events.TaskScheduler;
import com.spookengine.core.renderer.Renderer;
import com.spookengine.maths.FastMath;
import com.spookengine.maths.Vec3;
import com.spookengine.platform.desktop.JOGLRenderer3;
import com.spookengine.platform.desktop.ObjLoader;
import com.spookengine.scenegraph.Spatial;
import com.spookengine.scenegraph.Visual;
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
    private Spatial A, B;
    private Visual C;
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
        camMan = new CameraMan("camera", new Cam(Cam.Projection.PERSPECTIVE));
        camMan.getLocalTransform().moveTo(0, -10, 0);
        camMan.getLocalTransform().update();
        root.attachChild(camMan);
        
        /* ******** ******** ******** */
        /*          LIGHTING          */
        /* ******** ******** ******** */
//        LightBulb sun = new LightBulb(0.8f, 0.8f, 0.8f);
//        sun.hasAmbience = true;
//        LightMan lm = new LightMan("sun", sun);
//        root.attachChild(lm);
//        
//        root.getLocalAppearance().override(App.LIGHTING);
//        root.getLocalAppearance().addLight(sun);
        
        try {
            Spatial A = new Spatial("A");
            A.getLocalTransform().moveTo(0, 0, 0);
            A.getLocalTransform().rotateToPRY(FastMath.toRadians(45), 0, FastMath.toRadians(45));
            A.getLocalTransform().update();
            
            Spatial B = new Spatial("B");
            B.getLocalTransform().moveTo(0, 0, 0);
            B.getLocalTransform().rotateToPRY(0, 0, 0);
            B.getLocalTransform().update();
            A.attachChild(B);
            
            C = ObjLoader.getInstance().loadModel("/com/spookengine/jogl/", "TestCube.obj");
            C.getLocalTransform().add(B.getLocalTransform());
            C.getLocalTransform().update();
            B.attachChild(C);
            
            root.attachChild(A);
        } catch(IOException ex) {
            System.err.println("Shit!\n" + ex.getMessage());
        }
        
        /* ******** ******** ******** */
        /*           TASKS            */
        /* ******** ******** ******** */
        TaskScheduler.getInstance("Main").schedule(1, new RotateTask(C));
    }

    @Override
    public void init(GLAutoDrawable glad) {
        GL2 gl2 = glad.getGL().getGL2();
        Renderer.clearColour = new Vec3(1f, 0.75f, 0.75f);
        JOGLRenderer3.getInstance("Main", gl2).onSurfaceCreated();
        
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
        JOGLRenderer3.getInstance("Main", gl2).onSurfaceChanged(width, height);
    }
    
    @Override
    public void display(GLAutoDrawable glad) {
        GL2 gl2 = glad.getGL().getGL2();
        JOGLRenderer3.getInstance("Main", gl2).onDrawFrame(root, camMan.getCamera());
        
        TaskScheduler.getInstance("Main").update();
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
    }
    
    public class RotateTask extends Task {
        private Spatial obj;
        private float angle;
        
        public RotateTask(Spatial obj) {
            this.obj = obj;
        }
        
        @Override
        public TaskState perform(float tpf) {
            obj.getLocalTransform().rotateTo(angle += 0.01f, 1, 0, 0).moveTo((float) Math.sin(angle)*2, (float) Math.cos(angle)*2, 0).update();
            
            return TaskState.CONTINUE_RUN;
        }
        
    }
}
