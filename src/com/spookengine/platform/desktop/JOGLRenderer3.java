package com.spookengine.platform.desktop;

import static com.spookengine.scenegraph.App.*;
import com.spookengine.scenegraph.*;
import com.spookengine.scenegraph.appearance.PolyAtt.CullFace;
import com.spookengine.scenegraph.appearance.*;
import com.spookengine.scenegraph.camera.Cam;
import com.spookengine.scenegraph.lights.DirLight;
import com.spookengine.scenegraph.lights.Light;
import com.spookengine.scenegraph.lights.LightBulb;
import com.spookengine.scenegraph.lights.SpotLight;
import com.spookengine.scenegraph.renderer.Renderer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author Oliver Winks
 */
public class JOGLRenderer3 extends Renderer {
    private static final Logger logger = Logger.getLogger(JOGLRenderer3.class.getName());
    private static JOGLRenderer3 instance;
    
    private GL2 gl;
    private GLU glu;

    // render vars
    private static final int[] LIGHTS = new int[] {
        GL2.GL_LIGHT0, GL2.GL_LIGHT1, GL2.GL_LIGHT2, GL2.GL_LIGHT3,
        GL2.GL_LIGHT4, GL2.GL_LIGHT5, GL2.GL_LIGHT6, GL2.GL_LIGHT7
    };
    private int activeLights;
    
    // convenience vars
    private float[] AT = new float[16];
    private float[] CAM_AT = new float[16];
    
    public static JOGLRenderer3 getInstance(GL2 gl) {
        if(instance == null) {
            instance = new JOGLRenderer3();
            
            instance.worldTransform = new Trfm();
            instance.worldAppearance = new App();
        }
        
        instance.gl = gl;
        instance.glu = new GLU();
        
        return instance;
    }
    
    private JOGLRenderer3() {
        super();
        
        logger.log(Level.INFO, "Renormalizing renderer loaded!!");
    }
    
    @Override
    public void onSurfaceCreated() {
        // initialise OpenGL
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(clearColour.x(), clearColour.y(), clearColour.z(), 0.0f);
        
        gl.glTexEnvf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

        // enable depth testing
        gl.glClearDepthf(1f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);

        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_FASTEST);
    }
    
    @Override
    public void onSurfaceChanged(int width, int height) {
        canvasChanged = true;
        canvasWidth = width;
        canvasHeight = height;
    }
    
    @Override
    public void onDrawFrame(Spatial root, Cam cam) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // canvas changed?
        if(canvasChanged) {
            logger.log(Level.INFO, "GLRenderer 3D canvas changed.");
            cam.onCanvasChanged(canvasWidth, canvasHeight);
        }

        // viewport
        if(camera != cam || cam.viewportChanged) {
            logger.log(Level.INFO, "GLRenderer 3D viewport changed {0}, {1}", new Object[]{cam.getViewport().getPos(), cam.getViewport().getDimensions()});
            gl.glViewport(
                    (int) cam.getViewport().getPos().v[0],
                    (int) cam.getViewport().getPos().v[1],
                    (int) cam.getViewport().getDimensions().v[0],
                    (int) cam.getViewport().getDimensions().v[1]);
            cam.viewportChanged = false;
        }

        // projection matrix
        Cam cam3 = (Cam) cam;
        if(camera != cam || cam3.frustumChanged) {
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            glu.gluPerspective(
                    cam3.getFOV(),
                    cam3.getAspectRatio(),
                    cam3.getNearClip(),
                    cam3.getFarClip());
            cam3.frustumChanged = false;

            logger.log(Level.INFO, "Cam [{0}, {1}, {2}, {3}]!!", new Object[]{cam3.getFOV(), cam3.getAspectRatio(), cam3.getNearClip(), cam3.getFarClip()});
        }

        // camera
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        cam.getModelView().toOpenGL(CAM_AT);
        gl.glMultMatrixf(CAM_AT, 0);
        camera = cam;

        // reset state
        worldTransform.toIdentity();
        boundsStack.clear();

        // update, bound and build render stacks
        spatialStackEmpty = true;
        visualStackEmpty = true;
        update(root);
        boundAndStack(root);

        // render
        int nNodes = renderStack.size();
        Visual currNode;
        for(int j=0; j<nNodes; j++) {
            currNode = renderStack.pop();
            if(currNode instanceof Geom) {
                Geom geom = (Geom) currNode;

                enableApp(currNode.getWorldAppearance());
                gl.glPushMatrix();
                    currNode.getWorldTransform().at.toOpenGL(AT);
                    gl.glMultMatrixf(AT, 0);
                    
                    gl.glEnable(GL2.GL_NORMALIZE);
                    render(geom);
                    gl.glDisable(GL2.GL_NORMALIZE);
                gl.glPopMatrix();
                disableApp(currNode.getWorldAppearance());
            }
        }

        // deffered rendering
        if(!deferredStack.isEmpty()) {
            // TODO: sort the stack
//            Collections.sort(deferredStack, depthComparator);
            
            gl.glEnable(GL2.GL_BLEND);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
            gl.glDepthMask(false);

            nNodes = deferredStack.size();
            for(int j=0; j<nNodes; j++) {
                currNode = deferredStack.pop();
                if(currNode instanceof Geom) {
                    Geom geom = (Geom) currNode;

                    enableApp(currNode.getWorldAppearance());
                    gl.glPushMatrix();
                        /*
                         * Transform the VisualNode. Remember, angles must be reversed
                         * because OpenGL matices are the transpose of pure3d matrices,
                         * therefore rotation matrices are reversed!
                         */
                        currNode.getWorldTransform().at.toOpenGL(AT);
                        gl.glMultMatrixf(AT, 0);

                        render(geom);
                    gl.glPopMatrix();
                    disableApp(currNode.getWorldAppearance());
                }
            }

            gl.glDepthMask(true);
            gl.glDisable(GL2.GL_BLEND);
        }
        
        canvasChanged = false;
    }
    
    private void generateTexturePointer(Texture tex) {
//        tex.texPtr = new int[1];

        // generate one texture pointer and bind it.
        gl.glGenTextures(1, tex.getTexturePointer(), 0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTexturePointer()[0]);

        // clamping or repeating
        // TODO: Note, OpenGL ES 1.0 does not support clamping without borders
        gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        
        // if more than one image is defined then use mipmapping
        if(tex.getBitmaps().length > 1) {
            gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST_MIPMAP_NEAREST);
        } else {
            gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        }
        
        // TODO: THIS NEEDS TO BE LOOKED AT SO THAT MIPMAPPING WORKS!!!!
        for(int i=0; i<tex.getBitmaps().length; i++) {
            gl.glTexImage2D(GL2.GL_TEXTURE_2D, i, GL2.GL_RGBA, tex.getWidth(i), tex.getHeight(i), 
                    0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, tex.getBitmap(i));
        }
    }
    
    private void enableApp(App app) {
        int inheritance = app.getInheritance();
        
        /* ******** ******** ******** */
        /*   ENABLE POINT ATTRIBUTES  */
        /* ******** ******** ******** */
        if((inheritance & POINT_ATT) != 0) {
            PointAtt pointAtt = app.pointAtt;
            if(pointAtt.isAntiAliased)
                gl.glEnable(GL2.GL_POINT_SMOOTH);

            if(pointAtt.getPointSize() != 1)
                gl.glPointSize(pointAtt.getPointSize());
        }

        /* ******** ******** ******** */
        /*   ENABLE LINE ATTRIBUTES   */
        /* ******** ******** ******** */
        if((inheritance & LINE_ATT) != 0) {
            LineAtt lineAtt = app.lineAtt;
            if(lineAtt.isAntiAliased)
                gl.glEnable(GL2.GL_LINE_SMOOTH);

            if(lineAtt.getLineWidth() != 1)
                gl.glLineWidth(lineAtt.getLineWidth());
        }

        /* ******** ******** ******** */
        /*   ENABLE POLY ATTRIBUTES   */
        /* ******** ******** ******** */
        if((inheritance & POLY_ATT) != 0) {
            PolyAtt polyAtt = app.polyAtt;
            if(polyAtt.cullFace != CullFace.NONE) {
                gl.glEnable(GL2.GL_CULL_FACE);
                switch(polyAtt.cullFace) {
                    case BACK:
                        gl.glCullFace(GL2.GL_BACK);
                        break;
                    case FRONT:
                        gl.glCullFace(GL2.GL_FRONT);
                        break;
                }
            }
        }

        /* ******** ******** ******** */
        /*        ENABLE ALPHA        */
        /* ******** ******** ******** */
        float alpha = 1f;
        if((inheritance & ALPHA) != 0) {
            alpha = app.alpha.getValue();
        }
        
        /* ******** ******** ******** */
        /*      ENABLE COLOURING      */
        /* ******** ******** ******** */
        if((inheritance & COLOUR) != 0) {
            // enable
            Colour colour = app.colour;
            gl.glColor4f(
                    colour.getColour()[0],
                    colour.getColour()[1],
                    colour.getColour()[2],
                    alpha);
        } 

        /* ******** ******** ******** */
        /*       ENABLE MATERIAL      */
        /* ******** ******** ******** */
        if((inheritance & MATERIAL) != 0) {
            Material material = app.material;
            gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, material.getShininess());
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, material.getAmbient(alpha));
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, material.getDiffuse(alpha));
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, material.getSpecular(alpha));
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, material.getEmissive(alpha));
        } else if((inheritance & COLOUR) != 0) {
            // enable
            Colour colour = app.colour;
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
            gl.glEnable(GL2.GL_TEXTURE_2D);

            for(int i=0; i<textures.size(); i++) {
                if(!textures.get(i).texPtrGenerated) {
                    logger.log(Level.INFO, "Generating texture pointer!");
                    generateTexturePointer(textures.get(i));
                    textures.get(i).texPtrGenerated = true;
                }
                
                // clamping & repeating
                
                gl.glBindTexture(GL2.GL_TEXTURE_2D, textures.get(0).getTexturePointer()[0]);

                if(textures.get(i).hasAlpha())
                    gl.glTexEnvf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
                else
                    gl.glTexEnvf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);
            }
        }

        /* ******** ******** ******** */
        /*       ENABLE LIGHTS        */
        /* ******** ******** ******** */
        activeLights = 0;
        List<Light> lights = app.getLights();
        int numOfLights = lights.size();
        if(numOfLights > 0) {
            gl.glEnable(GL2.GL_LIGHTING);

            // enable lights according to node's lighting policy
            int light;
            switch(app.lightingPolicy) {
                case LOCAL_FIRST:
                    light = numOfLights - 1;
                    while(light >= 0 && activeLights < 8) {
                        Light currLight = lights.get(light);
                        if(currLight.on) {
                            enableLight(currLight);
                            activeLights++;
                        }
                        light--;
                    }
                    break;

                case GLOBAL_FIRST:
                    light = 0;
                    while(light < numOfLights && activeLights < 8) {
                        Light currLight = lights.get(light);
                        if(lights.get(light).on) {
                            enableLight(currLight);
                            activeLights++;
                        }
                        light++;
                    }
                    break;
            }
        }
    }
    
    private void enableLight(Light light) {
        if(light instanceof LightBulb) {
            LightBulb lb = (LightBulb) light;
            if(lb.hasAmbience) {
                gl.glEnable(LIGHTS[activeLights]);
                gl.glLightfv(LIGHTS[activeLights], GL2.GL_AMBIENT, light.getColour(), 0);
            } else {
                gl.glEnable(LIGHTS[activeLights]);
            }

            gl.glLightf(LIGHTS[activeLights], GL2.GL_CONSTANT_ATTENUATION, lb.getConstAttenuation());
            gl.glLightf(LIGHTS[activeLights], GL2.GL_LINEAR_ATTENUATION, lb.getLinearAttenuation());
            gl.glLightf(LIGHTS[activeLights], GL2.GL_QUADRATIC_ATTENUATION, lb.getQuadAttenuation());

            gl.glLightfv(LIGHTS[activeLights], GL2.GL_DIFFUSE, light.getColour(), 0);
            gl.glLightfv(LIGHTS[activeLights], GL2.GL_SPECULAR, light.getColour(), 0);
            gl.glLightfv(LIGHTS[activeLights], GL2.GL_POSITION, new float[] {
                lb.getPos().x(), lb.getPos().y(), lb.getPos().z(), 1}, 0);

            // SpotLight
            if(light instanceof SpotLight) {
                SpotLight sl = (SpotLight) light;

                gl.glLightf(LIGHTS[activeLights], GL2.GL_SPOT_CUTOFF, sl.getAngle());
                gl.glLightf(LIGHTS[activeLights], GL2.GL_SPOT_EXPONENT, sl.getFoucs());
                gl.glLightfv(LIGHTS[activeLights], GL2.GL_SPOT_DIRECTION, new float[] {
                    sl.getDir().x(), sl.getDir().y(), sl.getDir().z()}, 0);
            }
        } else if(light instanceof Light) {
            gl.glEnable(LIGHTS[activeLights]);
            gl.glLightfv(LIGHTS[activeLights], GL2.GL_AMBIENT, light.getColour(), 0);

            // Direction light
            if(light instanceof DirLight) {
                DirLight dl = (DirLight) light;
                gl.glLightfv(LIGHTS[activeLights], GL2.GL_POSITION, new float[] {
                    dl.getDir().x(), dl.getDir().y(), dl.getDir().z(), 0}, 0);
            }
        }
    }
    
    private void disableApp(App app) {
        int inheritance = app.getInheritance();
        
        /* ******** ******** ******** */
        /*  DISABLE POINT ATTRIBUTES  */
        /* ******** ******** ******** */
        if((inheritance & POINT_ATT) != 0) {
            PointAtt pointAtt = app.pointAtt;
            if(pointAtt.isAntiAliased)
                gl.glDisable(GL2.GL_POINT_SMOOTH);

            if(pointAtt.getPointSize() != 1)
                gl.glPointSize(1f);
        }

        /* ******** ******** ******** */
        /*   DISABLE LINE ATTRIBUTES  */
        /* ******** ******** ******** */
        if((inheritance & LINE_ATT) != 0) {
            LineAtt lineAtt = app.lineAtt;
            if(lineAtt.isAntiAliased)
                gl.glDisable(GL2.GL_LINE_SMOOTH);

            if(lineAtt.getLineWidth() != 1)
                gl.glLineWidth(1f);
        }

        /* ******** ******** ******** */
        /*   DISABLE POLY ATTRIBUTES  */
        /* ******** ******** ******** */
        if((inheritance & POLY_ATT) != 0) {
            PolyAtt polyAtt = app.polyAtt;
            if(polyAtt.cullFace != CullFace.NONE)
                gl.glDisable(GL2.GL_CULL_FACE);
        }

        /* ******** ******** ******** */
        /*      DISABLE MATERIAL      */
        /*        OR COLOURING        */
        /* ******** ******** ******** */
        if((inheritance & MATERIAL) != 0) {
            Material.defaultAmbient.position(0);
            Material.defaultDiffuse.position(0);
            Material.defaultSpecular.position(0);
            Material.defaultEmissive.position(0);

            gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, Material.defaultAmbient);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, Material.defaultDiffuse);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, Material.defaultSpecular);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, Material.defaultEmissive);
        } else if((inheritance & COLOUR) != 0) {
            gl.glColor4f(1, 1, 1, 1);
        }

        /* ******** ******** ******** */
        /*  DISABLE TEXTURE MAPPING   */
        /* ******** ******** ******** */
        List<Texture> textures = app.getTextures();
        if(!textures.isEmpty()) {
            gl.glDisable(GL2.GL_TEXTURE_2D);
        }

        /* ******** ******** ******** */
        /*       DISABLE LIGHTS       */
        /* ******** ******** ******** */
        for(int i=0; i<activeLights; i++)
            gl.glDisable(LIGHTS[i]);
        gl.glDisable(GL2.GL_LIGHTING);
    }
    
    private void render(Geom geom) {
        Trimesh trimesh = geom.getTrimesh();
        App worldApp = geom.getWorldAppearance();
        
        FloatBuffer norms = trimesh.getNormals();
        if(norms != null) {
            gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glNormalPointer(GL2.GL_FLOAT, 0, norms);
        }

        int numOfTexCoords = trimesh.getTexCoords().size();
        int numOfTextures = worldApp.getTextures().size();
        if(numOfTexCoords > 0 && numOfTextures > 0) {
            for(int i=0; i<numOfTextures; i++) {
                gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

                if(i > numOfTexCoords - 1)
                    gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, trimesh.getTexCoords(0));
                else
                    gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, trimesh.getTexCoords(i));
            }
        }

        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, trimesh.getVertices());
        switch(trimesh.drawMode) {
        case POINTS:
            gl.glDrawArrays(GL2.GL_POINTS, 0, trimesh.getVertexCount());
            break;
        case LINES:
            gl.glDrawArrays(GL2.GL_LINES, 0, trimesh.getVertexCount());
            break;
        case LINE_STRIP:
            gl.glDrawArrays(GL2.GL_LINE_STRIP, 0, trimesh.getVertexCount());
            break;
        case TRIANGLES:
            gl.glDrawArrays(GL2.GL_TRIANGLES, 0, trimesh.getVertexCount());
            break;
        case TRIANGLE_STRIP:
            gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, trimesh.getVertexCount());
            break;
        case TRIANGLE_FAN:
            gl.glDrawArrays(GL2.GL_TRIANGLE_FAN, 0, trimesh.getVertexCount());
            break;
        }
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);

        if(norms != null) {
            gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        }
    }
    
}
