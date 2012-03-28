package com.spookengine.jogl;

import com.jogamp.opengl.util.Animator;
import com.spookengine.desktop.TextureLoader;
import com.spookengine.desktop.jogl.JOGLRenderer3;
import com.spookengine.maths.Vec3;
import com.spookengine.scenegraph.*;
import com.spookengine.scenegraph.appearance.Texture;
import com.spookengine.scenegraph.camera.Cam3;
import com.spookengine.scenegraph.camera.CameraMan3;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;

/**
 *
 * @author Oliver Winks
 */
public class JOGLTest implements GLEventListener {
    
    private Visual<Trfm3, App3> root;
    private Cam3 cam;
    
    public static void main(String[] args) {
        JOGLTest test = new JOGLTest();
    }
    
    public JOGLTest() {
        // setup JOGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities glCaps = new GLCapabilities(glp);
        GLCanvas glCanvas = new GLCanvas();
        
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
        
        // setup scene
        setupScene();
        
        // start render loop
        glCanvas.addGLEventListener(this);
        
        Animator animator = new Animator();
        animator.add(glCanvas);
        animator.start();
    }
    
    private void setupScene() {
        root = Visual.new3D("root");
        
        // add a camera
        cam = new Cam3();
        CameraMan3 camMan = new CameraMan3("camera", cam);
        camMan.getLocalTransform().translateBy(0, 0, -5);
        camMan.updateLocalTransform();
        root.attachChild(camMan);
        
        // build floor
        Geom<Trfm3, App3> floor = Geom.new3D("floor", Trimesh.Quad(5, 5, 5));
        
        // colour the floor
//        floor.getLocalAppearance().override(App.COLOUR);
//        floor.getLocalAppearance().setColour(new Colour(1, 1, 0));
        
        // texture the floor
        try {
            BufferedImage checkerboardImg = ImageIO.read(getClass().getResource("floor.png"));
            Texture checkerBoard = TextureLoader.getInstance().loadTexture(checkerboardImg);
            floor.getLocalAppearance().addTexture(checkerBoard);
        } catch (IOException ex) {
            Logger.getLogger(JOGLTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        root.attachChild(floor);
    }

    @Override
    public void init(GLAutoDrawable glad) {
        GL2 gl2 = glad.getGL().getGL2();
        JOGLRenderer3.clearColour = new Vec3(0.75f, 0.75f, 0.75f);
        JOGLRenderer3.getInstance(gl2).onSurfaceCreated();
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        GL2 gl2 = glad.getGL().getGL2();
        JOGLRenderer3.getInstance(gl2).onSurfaceChanged(width, height);
    }
    
    @Override
    public void display(GLAutoDrawable glad) {
        GL2 gl2 = glad.getGL().getGL2();
        JOGLRenderer3.getInstance(gl2).onDrawFrame(root, cam);
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
    }
    
}
