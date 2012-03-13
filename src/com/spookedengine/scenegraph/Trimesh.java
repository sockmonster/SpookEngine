package com.spookedengine.scenegraph;

import com.spookedengine.math.Vec2;
import com.spookedengine.math.Vec3;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * The Trimesh class defines geometric shapes which can be rendered by the
 * PureRenderer.
 * 
 * @author Oliver Winks
 */
public class Trimesh {
    public static enum DrawMode {POINTS, LINES, LINE_STRIP, LINE_LOOP, TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN}

    public DrawMode drawMode;
    private FloatBuffer vertices;
    private FloatBuffer normals;
    private List<FloatBuffer> texCoords;

    public Trimesh() {
        texCoords = new ArrayList<FloatBuffer>();
    }

    public Trimesh(DrawMode drawMode, float[] vertexArray) {
        this.drawMode = drawMode;

        ByteBuffer buf = ByteBuffer.allocateDirect(vertexArray.length*4);
        buf.order(ByteOrder.nativeOrder());
        vertices = buf.asFloatBuffer();
        vertices.put(vertexArray);
        vertices.position(0);

        texCoords = new ArrayList<FloatBuffer>();
    }

    public Trimesh(DrawMode drawMode, Vec3[] vertexArray) {
        this.drawMode = drawMode;

        ByteBuffer bbuf = ByteBuffer.allocateDirect(vertexArray.length*12);
        bbuf.order(ByteOrder.nativeOrder());
        vertices = bbuf.asFloatBuffer();
        for(int v=0; v<vertexArray.length; v++) {
            vertices.put(vertexArray[v].v);
        }
        vertices.position(0);

        texCoords = new ArrayList<FloatBuffer>();
    }

    public Trimesh(DrawMode drawMode, float[] vertexArray, float[] normalArray) {
        this.drawMode = drawMode;

        ByteBuffer buf;
        buf = ByteBuffer.allocateDirect(vertexArray.length*4);
        buf.order(ByteOrder.nativeOrder());
        vertices = buf.asFloatBuffer();
        vertices.put(vertexArray);
        vertices.position(0);

        buf = ByteBuffer.allocateDirect(normalArray.length*4);
        buf.order(ByteOrder.nativeOrder());
        normals = buf.asFloatBuffer();
        normals.put(normalArray);
        normals.position(0);

        texCoords = new ArrayList<FloatBuffer>();
    }

    public Trimesh(DrawMode drawMode, Vec3[] vertexArray, Vec3[] normalArray) {
        this.drawMode = drawMode;

        ByteBuffer buf;

        buf = ByteBuffer.allocateDirect(vertexArray.length*12);
        buf.order(ByteOrder.nativeOrder());
        vertices = buf.asFloatBuffer();
        for(int v=0; v<vertexArray.length; v++) {
            vertices.put(vertexArray[v].v);
        }
        vertices.position(0);

        buf = ByteBuffer.allocateDirect(normalArray.length*12);
        buf.order(ByteOrder.nativeOrder());
        normals = buf.asFloatBuffer();
        for(int v=0; v<normalArray.length; v++) {
            normals.put(normalArray[v].v);
        }
        normals.position(0);

        texCoords = new ArrayList<FloatBuffer>();
    }

    /**
     * Creates a new line Trimesh.
     *
     * @param points The points in the line.
     * @return A new line Trimesh.
     */
    public static Trimesh Line(int dim, DrawMode drawMode, Vec3[] points) {
        return new Trimesh(drawMode, points);
    }

    /**
     * Creates a new Triangle Trimesh.
     *
     * @param width The width of the triangle.
     * @param height The height of the triangle.
     * @return A new triangle Trimesh.
     */
    public static Trimesh Triangle(float width, float height) {
        float halfWidth = width/2;
        float halfHeight = height/2;

        Vec3[] vertexArray = new Vec3[] {
            new Vec3( 0,            1*halfHeight, 0),
            new Vec3(-1*halfWidth, -1*halfHeight, 0),
            new Vec3( 1*halfWidth, -1*halfHeight, 0)
        };
        Vec2[] texCoordArray = new Vec2[] {
            new Vec2(0.5f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 0.0f)
        };

        Trimesh tri = new Trimesh(DrawMode.TRIANGLES, vertexArray);
        tri.addTexCoords(texCoordArray);
        return tri;
    }

    /**
     * Creates a new rectangular Trimesh.
     *
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @return A new rectangle Trimesh.
     */
    public static Trimesh Quad(float width, float height) {
        float halfWidth = width/2;
        float halfHeight = height/2;

        Vec3[] vertexArray = new Vec3[] {
            new Vec3( halfWidth,  halfHeight, 0),
            new Vec3(-halfWidth,  halfHeight, 0),
            new Vec3( halfWidth, -halfHeight, 0),
            new Vec3(-halfWidth, -halfHeight, 0)
        };
        Vec2[] texCoordArray = new Vec2[] {
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 1.0f),
            new Vec2(1.0f, 0.0f),
            new Vec2(0.0f, 0.0f)
        };

        Trimesh quad = new Trimesh(DrawMode.TRIANGLE_STRIP, vertexArray);
        quad.addTexCoords(texCoordArray);
        return quad;
    }

    /**
     * Create a new ellipse Trimesh.
     *
     * @param width The width of the ellipse.
     * @param height The height of the ellipse.
     * @param edges The number of edges the ellipse will contain.
     * @return A new ellipse Trimesh.
     */
    public static Trimesh Ellipse(float width, float height, int edges) {
        float radw = width/2f;
        float radh = height/2f;

	if(edges > 2) {
            int vertices = edges + 2;
            Vec3[] vertexArray = new Vec3[vertices];
            Vec2[] texCoordArray = new Vec2[vertices];

            // add central vertex
            vertexArray[0] = new Vec3(0, 0, 0);
            texCoordArray[0] = new Vec2(0.5f, 0.5f);

            int count = 1;
            float angle = 0;
            for(float i=0; i<vertices-1; i++) {
                angle += 360.0f/edges;
                float x = (float) (radw*Math.cos(Math.toRadians(angle)));
                float y = (float) (radh*Math.sin(Math.toRadians(angle)));

                vertexArray[count] = new Vec3(x, y, 0);
                texCoordArray[count] = new Vec2( (x + radw)/width, (y + radh)/height );
                count++;
            }

            Trimesh ellipse = new Trimesh(DrawMode.TRIANGLE_FAN, vertexArray);
            ellipse.addTexCoords(texCoordArray);
            return ellipse;
        }
        return null;
    }

    
    public static Trimesh Cube(float width, float height, float depth) {
        float halfWidth = width/2;
        float halfHeight = height/2;
        float halfDepth = depth/2;

        Vec3[] vertexArray = new Vec3[] {
            // front face
            new Vec3( halfWidth,  halfHeight, -halfDepth), // 1
            new Vec3(-halfWidth,  halfHeight, -halfDepth), // 2
            new Vec3(-halfWidth, -halfHeight, -halfDepth), // 3
            new Vec3( halfWidth,  halfHeight, -halfDepth), // 1
            new Vec3(-halfWidth, -halfHeight, -halfDepth), // 3
            new Vec3( halfWidth, -halfHeight, -halfDepth), // 4
            
            // back face
            new Vec3(-halfWidth,  halfHeight,  halfDepth), // 1
            new Vec3( halfWidth,  halfHeight,  halfDepth), // 2
            new Vec3( halfWidth, -halfHeight,  halfDepth), // 3
            new Vec3(-halfWidth,  halfHeight,  halfDepth), // 1
            new Vec3( halfWidth, -halfHeight,  halfDepth), // 3
            new Vec3(-halfWidth, -halfHeight,  halfDepth), // 4
            
            // right face
            new Vec3( halfWidth,  halfHeight,  halfDepth), // 1
            new Vec3( halfWidth,  halfHeight, -halfDepth), // 2
            new Vec3( halfWidth, -halfHeight, -halfDepth), // 3
            new Vec3( halfWidth,  halfHeight,  halfDepth), // 1
            new Vec3( halfWidth, -halfHeight, -halfDepth), // 3
            new Vec3( halfWidth, -halfHeight,  halfDepth), // 4
            
            // left face
            new Vec3(-halfWidth,  halfHeight, -halfDepth), // 1
            new Vec3(-halfWidth,  halfHeight,  halfDepth), // 2
            new Vec3(-halfWidth, -halfHeight,  halfDepth), // 3
            new Vec3(-halfWidth,  halfHeight, -halfDepth), // 1
            new Vec3(-halfWidth, -halfHeight,  halfDepth), // 3
            new Vec3(-halfWidth, -halfHeight, -halfDepth), // 4
            
            // top face
            new Vec3( halfWidth,  halfHeight,  halfDepth), // 1
            new Vec3(-halfWidth,  halfHeight,  halfDepth), // 2
            new Vec3(-halfWidth,  halfHeight, -halfDepth), // 3
            new Vec3( halfWidth,  halfHeight,  halfDepth), // 1
            new Vec3(-halfWidth,  halfHeight, -halfDepth), // 3
            new Vec3( halfWidth,  halfHeight, -halfDepth), // 4
            
            // bottom face
            new Vec3( halfWidth, -halfHeight, -halfDepth), // 1
            new Vec3(-halfWidth, -halfHeight, -halfDepth), // 2
            new Vec3(-halfWidth, -halfHeight,  halfDepth), // 3
            new Vec3( halfWidth, -halfHeight, -halfDepth), // 1
            new Vec3(-halfWidth, -halfHeight,  halfDepth), // 3
            new Vec3( halfWidth, -halfHeight,  halfDepth)  // 4
        };
        Vec3[] normalArray = new Vec3[] {
            // front face
            new Vec3( 0,  0, -1), // 1
            new Vec3( 0,  0, -1), // 2
            new Vec3( 0,  0, -1), // 3
            new Vec3( 0,  0, -1), // 1
            new Vec3( 0,  0, -1), // 3
            new Vec3( 0,  0, -1), // 4
            
            // back face
            new Vec3( 0,  0,  1), // 1
            new Vec3( 0,  0,  1), // 2
            new Vec3( 0,  0,  1), // 3
            new Vec3( 0,  0,  1), // 1
            new Vec3( 0,  0,  1), // 3
            new Vec3( 0,  0,  1), // 4
            
            // right face
            new Vec3( 1,  0,  0), // 1
            new Vec3( 1,  0,  0), // 2
            new Vec3( 1,  0,  0), // 3
            new Vec3( 1,  0,  0), // 1
            new Vec3( 1,  0,  0), // 3
            new Vec3( 1,  0,  0), // 4
            
            // left face
            new Vec3(-1,  0,  0), // 1
            new Vec3(-1,  0,  0), // 2
            new Vec3(-1,  0,  0), // 3
            new Vec3(-1,  0,  0), // 1
            new Vec3(-1,  0,  0), // 3
            new Vec3(-1,  0,  0), // 4
            
            // top face
            new Vec3( 0,  1,  0), // 1
            new Vec3( 0,  1,  0), // 2
            new Vec3( 0,  1,  0), // 3
            new Vec3( 0,  1,  0), // 1
            new Vec3( 0,  1,  0), // 3
            new Vec3( 0,  1,  0), // 4
            
            // bottom face
            new Vec3( 0, -1,  0), // 1
            new Vec3( 0, -1,  0), // 2
            new Vec3( 0, -1,  0), // 3
            new Vec3( 0, -1,  0), // 1
            new Vec3( 0, -1,  0), // 3
            new Vec3( 0, -1,  0), // 4
        };
        Vec2[] texCoordArray = new Vec2[] {
            // front face
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 0.0f),
            
            // back face
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 0.0f),
            
            // right face
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 0.0f),
            
            // left face
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 0.0f),
            
            // top face
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 0.0f),
            
            // bottom face
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 1.0f),
            new Vec2(0.0f, 0.0f),
            new Vec2(1.0f, 0.0f),
        };

        Trimesh quad = new Trimesh(DrawMode.TRIANGLES, vertexArray, normalArray);
        quad.addTexCoords(texCoordArray);
        return quad;
    }
    /**
     * Sets this Trimesh to the given Trimesh. This object essentially becomes
     * a clone of the given Trimesh.
     *
     * @param trimesh The Trimesh to set this Trimesh to.
     */
    public void setTo(Trimesh trimesh) {
        this.drawMode = trimesh.drawMode;

        ByteBuffer buf;

        buf = ByteBuffer.allocateDirect(trimesh.vertices.capacity()*12);
        buf.order(ByteOrder.nativeOrder());
        vertices = buf.asFloatBuffer();
        trimesh.vertices.position(0);
        vertices.put(trimesh.vertices);
        vertices.position(0);

        buf = ByteBuffer.allocateDirect(trimesh.normals.capacity()*12);
        buf.order(ByteOrder.nativeOrder());
        normals = buf.asFloatBuffer();
        trimesh.normals.position(0);
        normals.put(trimesh.normals);
        normals.position(0);

        for(int i=0; i<trimesh.texCoords.size(); i++) {
            buf = ByteBuffer.allocateDirect(trimesh.texCoords.get(i).capacity()*12);
            buf.order(ByteOrder.nativeOrder());
            FloatBuffer texCoordsBuf = buf.asFloatBuffer();
            trimesh.texCoords.get(i).position(0);
            texCoordsBuf.put(trimesh.texCoords.get(i));
            texCoordsBuf.position(0);

            texCoords.add(texCoordsBuf);
        }
    }

    /**
     * Sets the vertices that define this Trimesh.
     *
     * @param vertexArray The vertices.
     */
    public void setVertices(float[] vertexArray) {
        ByteBuffer buf = ByteBuffer.allocateDirect(vertexArray.length*4);
        buf.order(ByteOrder.nativeOrder());
        vertices = buf.asFloatBuffer();
        vertices.put(vertexArray);
        vertices.position(0);
    }

    /**
     * Sets the vertices that define this Trimesh.
     *
     * @param vertexArray The vertices.
     */
    public void setVertices(Vec3[] vertexArray) {
        ByteBuffer buf = ByteBuffer.allocateDirect(vertexArray.length*12);
        buf.order(ByteOrder.nativeOrder());
        vertices = buf.asFloatBuffer();
        for(int i=0; i<vertexArray.length; i++) {
            vertices.put(vertexArray[i].v);
        }
        vertices.position(0);
    }

    /**
     * Returns this Trimesh's vertices.
     *
     * @return This Trimesh's vertices.
     */
    public FloatBuffer getVertices() {
        return vertices;
    }

    /**
     * Sets this Trimesh's normals.
     *
     * @param normalArray The normals.
     */
    public void setNormals(float[] normalArray) {
        ByteBuffer buf = ByteBuffer.allocateDirect(normalArray.length*4);
        buf.order(ByteOrder.nativeOrder());
        normals = buf.asFloatBuffer();
        normals.put(normalArray);
        normals.position(0);
    }

    /**
     * Sets this Trimesh's normals.
     *
     * @param normalArray The normals.
     */
    public void setNormals(Vec3[] normalArray) {
        ByteBuffer buf = ByteBuffer.allocateDirect(normalArray.length*12);
        buf.order(ByteOrder.nativeOrder());
        normals = buf.asFloatBuffer();
        for(int i=0; i<normalArray.length; i++) {
            normals.put(normalArray[i].v);
        }
        normals.position(0);
    }

    /**
     * Returns this Trimesh's normals.
     *
     * @return This geometry's normals.
     */
    public FloatBuffer getNormals() {
        return normals;
    }

    public void setTexCoords(int i, float[] texCoordArray) {
        texCoords.get(i).position(0);
        texCoords.get(i).put(texCoordArray);
    }

    public void setTexCoords(int i, Vec2[] texCoordArray) {
        texCoords.get(i).position(0);

        for(int v=0; v<texCoordArray.length; v++)
            texCoords.get(i).put(texCoordArray[v].v);
    }

    /**
     * Add texture coordinates to this Trimesh.
     *
     * @param texCoordArray The texture coordinates to add.
     */
    public void addTexCoords(float[] texCoordArray) {
        ByteBuffer buf = ByteBuffer.allocateDirect(texCoordArray.length*4);
        buf.order(ByteOrder.nativeOrder());
        FloatBuffer texCoordBuf = buf.asFloatBuffer();
        texCoordBuf.put(texCoordArray);
        texCoordBuf.position(0);

        texCoords.add(texCoordBuf);
    }

    /**
     * Add texture coordinates to this Trimesh.
     *
     * @param texCoordArray The texture coordinates to add.
     */
    public void addTexCoords(Vec2[] texCoordArray) {
        ByteBuffer buf = ByteBuffer.allocateDirect(texCoordArray.length*8);
        buf.order(ByteOrder.nativeOrder());
        FloatBuffer texCoordBuf = buf.asFloatBuffer();
        for(int i=0; i<texCoordArray.length; i++) {
            texCoordBuf.put(texCoordArray[i].v[0]);
            texCoordBuf.put(texCoordArray[i].v[1]);
        }
        texCoordBuf.position(0);

        texCoords.add(texCoordBuf);
    }

    /**
     * Removes the i'th set of texture coordinates from this Trimesh. If the
     * Trimesh does not contain these texture coordinates nothing is removed.
     *
     * @param i The index of the texture coordinates to remove.
     * @return The texture coordinates as a FloatBuffer.
     */
    public FloatBuffer removeTexCoords(int i) {
        return texCoords.remove(i);
    }

    /**
     * Returns all of the texture coordinates associated with this Trimesh.
     *
     * @return all of the texture coordinates associated with this Trimesh.
     */
    public List<FloatBuffer> getTexCoords() {
        return texCoords;
    }

    /**
     * Returns the i'th set of texture coordinates associated with this Trimesh.
     *
     * @param i The index of the texture coordinates to return.
     * @return the i'th set of texture coordinates associated with this Trimesh.
     */
    public FloatBuffer getTexCoords(int i) {
        if(i >= 0 && i < texCoords.size())
            return texCoords.get(i);

        return null;
    }

    /**
     * Returns the number of vertices that this Trimesh holds.
     *
     * @return Number of vertices that define this Trimesh.
     */
    public int getVertexCount() {
        return vertices.capacity()/3;
    }

}
