package com.spookengine.jogl;

import com.jogamp.opengl.util.Animator;
import com.spookengine.core.camera.Cam;
import com.spookengine.core.renderer.Renderer;
import com.spookengine.maths.Vec3;
import com.spookengine.platform.desktop.JOGLRenderer3;
import com.spookengine.scenegraph.Bound;
import com.spookengine.scenegraph.Spatial;
import com.spookengine.scenegraph.Visual;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;

/**
 *
 * @author oli
 */
public class SceneGraphTest implements GLEventListener {
    
    private Cam camera;
    private Visual root;
    
    public static void main(String[] args) {
        SceneGraphTest test = new SceneGraphTest();
    }
    
    public SceneGraphTest() {
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
        camera = new Cam(Cam.Projection.PERSPECTIVE);
        root = new Visual("root");
        
        // Create scenegraph
        Spatial A = new Spatial("A");
        A.getLocalTransform().moveTo(0, 0, 1).update();
        root.attachChild(A);
        
        Spatial B = new Spatial("B");
        B.getLocalTransform().moveTo(0, 0, 1).update();
        A.attachChild(B);
        
        Spatial C = new Spatial("C");
        C.getLocalTransform().moveTo(0, 0, 1).update();
        B.attachChild(C);
        
        // Test findAncestor
        System.out.println("Ancestor Test\n------------");
        Spatial ancestor = C.findAncestor(Spatial.class);
        System.out.println(ancestor);
        
        // Test findChildren
        System.out.println("Children Test\n------------");
        List<Spatial> children = root.findChildren(Spatial.class);
        for(Bound child : children)
            System.out.println("Child: " + child.name);
        
        // Test LocalToWorld
        System.out.println("LocalToWorld Test\n----------------");
        A.localToWorld();
        System.out.println(A.getWorldTransform().getAffineTransform());
        System.out.println(B.getWorldTransform().getAffineTransform());
        System.out.println(C.getWorldTransform().getAffineTransform());
    }

    @Override
    public void init(GLAutoDrawable glad) {
        GL2 gl2 = glad.getGL().getGL2();
        Renderer.clearColour = new Vec3(0.75f, 0.75f, 0.75f);
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
//        GL2 gl2 = glad.getGL().getGL2();
//        JOGLRenderer3.getInstance("Main", gl2).onSurfaceChanged(width, height);
    }

    @Override
    public void display(GLAutoDrawable glad) {
//        GL2 gl2 = glad.getGL().getGL2();
//        JOGLRenderer3.getInstance("Main", gl2).onDrawFrame(root, camera);
//        
//        TaskScheduler.getInstance("Main").update();
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
    }
}
