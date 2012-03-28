package com.spookengine.mobile.android;

import android.opengl.GLU;
import static com.spookengine.scenegraph.App.*;
import com.spookengine.scenegraph.*;
import com.spookengine.scenegraph.appearance.PolyAtt.CullFace;
import com.spookengine.scenegraph.appearance.*;
import com.spookengine.scenegraph.camera.Cam;
import com.spookengine.scenegraph.renderer.Renderer;
import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 * @author Oliver Winks
 */
public class AndroidRenderer2 extends Renderer {
    private static final Logger logger = Logger.getLogger(AndroidRenderer2.class.getName());
    private static AndroidRenderer2 instance;
    
    private GL10 gl;
    
    private final LayerComparator layerComp = new LayerComparator();

    // convenience vars
    private float[] AT = new float[16];

    public static AndroidRenderer2 getInstance(GL10 gl) {
        if(instance == null) {
            instance = new AndroidRenderer2();
            
            instance.worldTransform = new Trfm2();
            instance.worldAppearance = new App2();
        }
        
        instance.gl = gl;
        return instance;
    }

    private AndroidRenderer2() {
        super();
    }
    
    @Override
    public void onSurfaceCreated() {
        // initialise OpenGL
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        // enable alpha blending
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        canvasChanged = true;
        canvasWidth = width;
        canvasHeight = height;
    }

    @Override
    public void onDrawFrame(Spatial root, Cam cam) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // canvas changed?
        if(canvasChanged) {
            logger.log(Level.INFO, "GLRenderer 2D canvas changed!");
            cam.onCanvasChanged(canvasWidth, canvasHeight);
        }

        // viewport
        if(camera != cam || cam.viewportChanged) {
            logger.log(Level.INFO, "GLRenderer 2D viewport changed {0}, {1}", new Object[]{cam.getViewport().getPos(), cam.getViewport().getDimensions()});
            gl.glViewport(
                    (int) cam.getViewport().getPos().v[0],
                    (int) cam.getViewport().getPos().v[1],
                    (int) cam.getViewport().getDimensions().v[0],
                    (int) cam.getViewport().getDimensions().v[1]);
            cam.viewportChanged = false;

            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            
            GLU.gluOrtho2D(gl,
                    (int) cam.getViewport().getPos().v[0],
                    (int) cam.getViewport().getDimensions().v[0],
                    (int) cam.getViewport().getPos().v[1],
                    (int) cam.getViewport().getDimensions().v[1]);
        }

        // camera
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        cam.getModelView().toOpenGL(AT);
        gl.glMultMatrixf(AT, 0);
        camera = cam;

        // reset state
        worldTransform.toIdentity();
        boundsStack.clear();

        // update, bound and build render stacks
        spatialStackEmpty = true;
        visualStackEmpty = true;
        update(root);
        boundAndStack(root);

        // sort render stack
        Collections.sort(renderStack, layerComp);
        
        // render
        int nNodes = renderStack.size();
        Visual<Trfm2, App2> currNode;
        for(int j=0; j<nNodes; j++) {
            currNode = renderStack.pop();
            if(currNode instanceof Geom) {
                Geom geom = (Geom) currNode;

                enableApp(currNode.getWorldAppearance());
                gl.glPushMatrix();
                    currNode.getWorldTransform().getAffineTransform().toOpenGL(AT);
                    gl.glMultMatrixf(AT, 0);

                    render(geom);
                gl.glPopMatrix();
                disableApp(currNode.getWorldAppearance());
            }
        }
        
        canvasChanged = false;
    }

    private void generateTexturePointer(Texture tex) {
//        tex.texPtr = new int[1];

        // generate one texture pointer and bind it.
//        gl.glGenTextures(1, tex.texPtr, 0);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex.texPtr[0]);
        gl.glGenTextures(1, tex.getTexturePointer(), 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex.getTexturePointer()[0]);

        // clamping or repeating
        // TODO: Note, OpenGL ES 1.0 does not support clamping without borders
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        // if more than one image is defined then use mipmapping
//        if(tex.getBitmaps().length > 1) {
//            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST_MIPMAP_NEAREST);
//        } else {
//            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
//        }

//        // Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
//        // TODO: THIS NEEDS TO BE LOOKED AT SO THAT MIPMAPPING WORKS!!!!
//        for(int i=0; i<tex.getBitmaps().length; i++) {
//            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, tex.getBitmaps()[i], 0);
//            
//            // TODO: TO SLOW TO USE AT THE MO
//            //gl.glTexImage2D(GL10.GL_TEXTURE_2D, i, GL10.GL_RGBA, tex.getWidth(i), tex.getHeight(i), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, tex.getData(i));
//        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void enableApp(App2 app) {
        int inheritance = app.getInheritance();
        
        /* ******** ******** ******** */
        /*   ENABLE POINT ATTRIBUTES  */
        /* ******** ******** ******** */
        if((inheritance & POINT_ATT) != 0) {
            PointAtt pointAtt = app.getPointAtt();
            if(pointAtt.isAntiAliased)
                gl.glEnable(GL10.GL_POINT_SMOOTH);

            if(pointAtt.getPointSize() != 1)
                gl.glPointSize(pointAtt.getPointSize());
        }

        /* ******** ******** ******** */
        /*   ENABLE LINE ATTRIBUTES   */
        /* ******** ******** ******** */
        if((inheritance & LINE_ATT) != 0) {
            LineAtt lineAtt = app.getLineAtt();
            if(lineAtt.isAntiAliased)
                gl.glEnable(GL10.GL_LINE_SMOOTH);

            if(lineAtt.getLineWidth() != 1)
                gl.glLineWidth(lineAtt.getLineWidth());
        }

        /* ******** ******** ******** */
        /*   ENABLE POLY ATTRIBUTES   */
        /* ******** ******** ******** */
        if((inheritance & POLY_ATT) != 0) {
            PolyAtt polyAtt = app.getPolyAtt();
            if(polyAtt.cullFace != CullFace.NONE) {
                gl.glEnable(GL10.GL_CULL_FACE);
                switch(polyAtt.cullFace) {
                    case BACK:
                        gl.glCullFace(GL10.GL_BACK);
                        break;
                    case FRONT:
                        gl.glCullFace(GL10.GL_FRONT);
                        break;
                }
            }
        }

        /* ******** ******** ******** */
        /*        ENABLE ALPHA        */
        /* ******** ******** ******** */
        float alpha = 1f;
        if((inheritance & ALPHA) != 0) {
            alpha = app.getAlpha().getValue();
        }

        /* ******** ******** ******** */
        /*       ENABLE COLOUR        */
        /* ******** ******** ******** */
        if((inheritance & COLOUR) != 0) {
            Colour colour = app.getColour();
            gl.glColor4f(
                    colour.getColour()[0],
                    colour.getColour()[1],
                    colour.getColour()[2],
                    alpha);
        } else if((inheritance & POINT_ATT) != 0 && alpha < 1) {
            // default colour with transparency
            gl.glColor4f(1,1,1,alpha);
        }

        /* ******** ******** ******** */
        /*   ENABLE TEXTURE MAPPING   */
        /* ******** ******** ******** */
        List<Texture> textures = app.getTextures();
        if(!textures.isEmpty()) {
            gl.glEnable(GL10.GL_TEXTURE_2D);

            for(int i=0; i<textures.size(); i++) {
//                if(app.textures.get(i).texPtr == null)
                if(textures.get(i).getTexturePointer() == null)
                    generateTexturePointer(textures.get(i));

//                gl.glBindTexture(GL_TEXTURE_2D, app.textures.get(0).texPtr[0]);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures.get(0).getTexturePointer()[0]);

                if(textures.get(i).hasAlpha())
                    gl.glTexEnvf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
                else
                    gl.glTexEnvf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_DECAL);
            }
        }
    }

    private void disableApp(App2 app) {
        int inheritance = app.getInheritance();
        
        /* ******** ******** ******** */
        /*  DISABLE POINT ATTRIBUTES  */
        /* ******** ******** ******** */
        if((inheritance & POINT_ATT) != 0) {
            PointAtt pointAtt = app.getPointAtt();
            if(pointAtt.isAntiAliased)
                gl.glDisable(GL10.GL_POINT_SMOOTH);

            if(pointAtt.getPointSize() != 1)
                gl.glPointSize(1f);
        }

        /* ******** ******** ******** */
        /*   DISABLE LINE ATTRIBUTES  */
        /* ******** ******** ******** */
        if((inheritance & LINE_ATT) != 0) {
            LineAtt lineAtt = app.getLineAtt();
            if(lineAtt.isAntiAliased)
                gl.glDisable(GL10.GL_LINE_SMOOTH);

            if(lineAtt.getLineWidth() != 1)
                gl.glLineWidth(1f);
        }

        /* ******** ******** ******** */
        /*   DISABLE POLY ATTRIBUTES  */
        /* ******** ******** ******** */
        if((inheritance & POLY_ATT) != 0) {
            PolyAtt polyAtt = app.getPolyAtt();
            if(polyAtt.cullFace != CullFace.NONE)
                gl.glDisable(GL10.GL_CULL_FACE);
        }

        /* ******** ******** ******** */
        /*    DISABLE COLOURING       */
        /* ******** ******** ******** */
        if((inheritance & COLOUR) != 0)
            gl.glColor4f(1, 1, 1, 1);

        /* ******** ******** ******** */
        /*  DISABLE TEXTURE MAPPING   */
        /* ******** ******** ******** */
        List<Texture> textures = app.getTextures();
        if(!textures.isEmpty())
            gl.glDisable(GL10.GL_TEXTURE_2D);
    }

    private void render(Geom geom) {
        Trimesh trimesh = geom.getTrimesh();
        App worldApp = geom.getWorldAppearance();
        
        FloatBuffer norms = trimesh.getNormals();
        if(norms != null) {
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            gl.glNormalPointer(GL10.GL_FLOAT, 0, norms);
        }

        int numOfTexCoords = trimesh.getTexCoords().size();
        int numOfTextures = worldApp.getTextures().size();
        if(numOfTexCoords > 0 && numOfTextures > 0) {
            for(int i=0; i<numOfTextures; i++) {
                gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

                if(i > numOfTexCoords - 1)
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, trimesh.getTexCoords(0));
                else
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, trimesh.getTexCoords(i));
            }
        }

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, trimesh.getVertices());
        switch(trimesh.drawMode) {
        case POINTS:
            gl.glDrawArrays(GL10.GL_POINTS, 0, trimesh.getVertexCount());
            break;
        case LINES:
            gl.glDrawArrays(GL10.GL_LINES, 0, trimesh.getVertexCount());
            break;
        case LINE_STRIP:
            gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, trimesh.getVertexCount());
            break;
        case TRIANGLES:
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, trimesh.getVertexCount());
            break;
        case TRIANGLE_STRIP:
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, trimesh.getVertexCount());
            break;
        case TRIANGLE_FAN:
            gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, trimesh.getVertexCount());
            break;
        }
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        if(norms != null) {
            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        }
    }

    private class LayerComparator implements Comparator<Visual> {
        @Override
        public int compare(Visual o1, Visual o2) {
            if(o1.layer > o2.layer) {
                return -1;
            }  else if (o1.layer < o2.layer) {
                return 1;
            }  else {
                return 0;
            }
        }
    }

}
