package com.spookengine.platform.desktop;

import com.spookengine.maths.Vec2;
import com.spookengine.maths.Vec3;
import com.spookengine.scenegraph.App;
import com.spookengine.scenegraph.Geom;
import com.spookengine.scenegraph.Trimesh;
import com.spookengine.scenegraph.Visual;
import com.spookengine.scenegraph.appearance.Alpha;
import com.spookengine.scenegraph.appearance.Colour;
import com.spookengine.scenegraph.appearance.Texture;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Oliver Winks
 */
public class ObjLoader {
    private static final Logger logger = Logger.getLogger(ObjLoader.class.getName());
    
    private static ObjLoader instance;
    
    // mesh details
    private int numOfGroups;
    private boolean hasNormals;
    private boolean hasTexCoords;
    private Group currGroup = new Group();
    private List<Group> groups = new ArrayList<Group>();
    private List<Vec3> vertices = new ArrayList<Vec3>();
    private List<Vec3> normals = new ArrayList<Vec3>();
    private List<Vec2> texCoords = new ArrayList<Vec2>();

    // file data
    private Map<String, Material> materials = new HashMap<String, Material>();
    private Map<String, Texture> textures = new HashMap<String, Texture>();
    
    private ObjLoader() {
    }
    
    public static ObjLoader getInstance() {
        if(instance == null)
            instance = new ObjLoader();
        
        return instance;
    }
    
    public Visual loadModel(String location, String filename) throws IOException {
        logger.log(Level.INFO, "loading model");
        
        // add a trailing / if one doesn't already exist
        location = location.trim();
        if(!location.endsWith("/"))
            location = location + "/";
        
        // remove begining /
        filename = filename.trim();
        if(filename.startsWith("/"))
            filename = filename.substring(1);
        
        // create input stream for .obj file
        InputStream objInputStream = getClass().getResourceAsStream(location + filename);

        // reset resource loader
        numOfGroups = 0;
        hasNormals = false;
        hasTexCoords = false;
        currGroup = new Group();
        groups.clear();
        vertices.clear();
        normals.clear();
        texCoords.clear();

        // load model
        BufferedReader objReader = new BufferedReader(
                new InputStreamReader(objInputStream));

        StringBuilder strBuf = new StringBuilder();
        int strLength = 0;
        char[] charArray = new char[1024];
        int n = objReader.read(charArray);
        while(n >= 0) {
            strBuf.append(charArray, 0, n);
            strLength += n;
            n = objReader.read(charArray);
        }
        objReader.close();

        // loop through lines
        int[] token = new int[2];
        int prevLine = 0;
        for(int i=0; i<strLength; i++) {
            if(strBuf.charAt(i) == '\n') {
                // trim
                int lineBegin = -1;
                int lineEnd = -1;
                for(int j=0; j<i - prevLine; j++) {
                    if(lineBegin == -1 && strBuf.charAt(prevLine + j) != ' ')
                        lineBegin = prevLine + j;

                    if(lineEnd == -1 && strBuf.charAt(i - (j + 1)) != ' ')
                        lineEnd = i - (j + 1);

                    if(lineBegin != -1 && lineEnd != -1)
                        break;
                }

                // ignore comments
                if(lineEnd - lineBegin > 0 && strBuf.charAt(lineBegin) != '#') {
                    // get first token
                    nextToken(strBuf, lineBegin, lineEnd, ' ', token);
                    int tokSize = token[1] - token[0];

                    if(tokSize == 0) {
                        switch(strBuf.charAt(token[0])) {
                            case 'v':
                                // create a new vertex
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                float x = toFloat(strBuf, token[0], token[1]);
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                float y = toFloat(strBuf, token[0], token[1]);
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                float z = toFloat(strBuf, token[0], token[1]);

                                vertices.add(new Vec3(x, y, z));
                                break;

                            case 'f':
                                // create a new face
                                if(countTokens(strBuf, token[1] + 1, lineEnd, ' ') == 3) {
                                    Face face = new Face();

                                    // read the 3 points of the triangle
                                    int tokBegin;
                                    int tokEnd;
                                    for(int p=0; p<3; p++) {
                                        // get first point
                                        nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                        tokBegin = token[0];
                                        tokEnd = token[1];

                                        // get vertex
                                        nextToken(strBuf, tokBegin, tokEnd, '/', token);
                                        face.vertexIndices[p] = toInt(strBuf, token[0], token[1]);
                                        if(hasTexCoords) {
                                            nextToken(strBuf, token[1] + 1, tokEnd, '/', token);
                                            face.texCoordIndices[p] = toInt(strBuf, token[0], token[1]);
                                        }
                                        if(hasNormals) {
                                            nextToken(strBuf, token[1] + 1, tokEnd, '/', token);
                                            face.normalIndices[p] = toInt(strBuf, token[0], token[1]);
                                        }
                                    }
                                    currGroup.faces.add(face);
                                    currGroup.numOfFaces++;
                                } else {
                                    logger.log(Level.SEVERE, "Object must be triangulated!");
                                }
                                break;

                            case 'g':
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);

                                if (numOfGroups != 0)
                                    currGroup = new Group();
                                currGroup.name = strBuf.substring(token[0], token[1] + 1);

                                logger.log(Level.INFO, "Loading Group {0}...", strBuf.substring(token[0], token[1] + 1));

                                numOfGroups++;
                                groups.add(currGroup);
                                break;
                        }
                    } else if(tokSize == 1) {
                        switch(strBuf.charAt(token[0] + 1)) {
                            case 'n':
                                // create a new normal
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                float x = toFloat(strBuf, token[0], token[1]);
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                float y = toFloat(strBuf, token[0], token[1]);
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                float z = toFloat(strBuf, token[0], token[1]);

                                normals.add(new Vec3(x, y, z));
                                hasNormals = true;
                                break;

                            case 't':
                                // create a new tex coord
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                float u = toFloat(strBuf, token[0], token[1]);
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                float v = toFloat(strBuf, token[0], token[1]);

                                texCoords.add(new Vec2(u, v));
                                hasTexCoords = true;
                                break;
                        }
                    } else if(tokSize == 5) {
                        switch(strBuf.charAt(token[0])) {
                            case 'u':
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                String materialName = strBuf.substring(token[0], token[1] + 1);
                                if(!materialName.equals("(null)"))
                                    currGroup.mtl = materials.get(materialName);
                                break;
                            case 'm':
                                // load mtllib
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);

                                // load material
                                logger.log(Level.INFO, "loading material {0}", strBuf.substring(token[0], token[1] + 1));
                                loadMTL(new BufferedReader(
                                        new InputStreamReader(getClass().getResourceAsStream(location + strBuf.substring(token[0], token[1] + 1)))));
                                break;
                        }
                    }

                }

                // ready for next line
                prevLine = i + 1;
            }
        }

        if (numOfGroups == 0)
            groups.add(currGroup);

        // build the model
        if(numOfGroups > 1) {
            Visual nodeGroup = new Visual("");
            for(int i=0; i<groups.size(); i++) {
                Visual node = groups.get(i).convert();
                nodeGroup.attachChild(node);
            }

            return nodeGroup;
        } else {
            Visual node = groups.get(0).convert();

            return node;
        }
    }
    
    protected void loadMTL(BufferedReader reader) throws IOException {
        Material mat = null;
        StringBuilder strBuf = new StringBuilder();

        int strLength = 0;
        char[] charArray = new char[2048];
        int n = reader.read(charArray);
        while(n >= 0) {
            strLength += n;
            strBuf.append(charArray, 0, n);
            n = reader.read(charArray);
        }
        reader.close();

        // loop through lines
        int[] token = new int[2];
        int prevLine = 0;
        for(int i=0; i<strLength; i++) {
            if(strBuf.charAt(i) == '\n') {
                // trim
                int lineBegin = -1;
                int lineEnd = -1;
                for(int j=0; j<i - prevLine; j++) {
                    if(lineBegin == -1 && !Character.isWhitespace(strBuf.charAt(prevLine + j)) || strBuf.charAt(prevLine + j) == '\n' || strBuf.charAt(prevLine + j) == '\r')
                        lineBegin = prevLine + j;

                    if(lineEnd == -1 && !Character.isWhitespace(strBuf.charAt(i - (j + 1))) || strBuf.charAt(i - (j + 1)) == '\n' || strBuf.charAt(i - (j + 1)) == '\r')
                        lineEnd = i - (j + 1);

                    if(lineBegin != -1 && lineEnd != -1)
                        break;
                }

                // ignore comments
                if(lineEnd - lineBegin > 0 && strBuf.charAt(lineBegin) != '#') {
                    // get first token
                    nextToken(strBuf, lineBegin, lineEnd, ' ', token);
                    int tokSize = token[1] - token[0];
                    
                    if(tokSize == 5 && strBuf.charAt(token[0]) == 'n') {
                        // new material
                        nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                        mat = new Material();
                        materials.put(strBuf.substring(token[0], token[1] + 1), mat);
                    } else if(tokSize == 1 && strBuf.charAt(token[0]) == 'N') {
                        // shininess
                        nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                        mat.shininess = toFloat(strBuf, token[0], token[1]);
                    } else if(tokSize == 1 && strBuf.charAt(token[0]) == 'K') {
                        switch(strBuf.charAt(token[0] + 1)) {
                            case 'a':
                                // ambient material
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                mat.ambient[0] = toFloat(strBuf, token[0], token[1]);

                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                mat.ambient[1] = toFloat(strBuf, token[0], token[1]);

                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                mat.ambient[2] = toFloat(strBuf, token[0], token[1]);
                                break;

                            case 'd':
                                // diffuse material
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                mat.diffuse[0] = toFloat(strBuf, token[0], token[1]);

                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                mat.diffuse[1] = toFloat(strBuf, token[0], token[1]);

                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                mat.diffuse[2] = toFloat(strBuf, token[0], token[1]);
                                break;

                            case 's':
                                // specular material
                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                mat.specular[0] = toFloat(strBuf, token[0], token[1]);

                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                mat.specular[1] = toFloat(strBuf, token[0], token[1]);

                                nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                                mat.specular[2] = toFloat(strBuf, token[0], token[1]);
                                break;
                        }
                    } else if((tokSize == 0 && strBuf.charAt(token[0]) == 'd') || (tokSize == 2 && strBuf.charAt(token[0]) == 'T')) {
                        // alpha
                        nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                        mat.alpha = toFloat(strBuf, token[0], token[1]);
                    } else if((tokSize == 4 || tokSize == 5) && strBuf.charAt(token[0]) == 'm') {
                        // load texture
                        nextToken(strBuf, token[1] + 1, lineEnd, ' ', token);
                        String texName = strBuf.substring(token[0], token[1]+1);
                        System.out.println("Loading Texture " + texName);
                        BufferedImage texImg = ImageIO.read(getClass().getResourceAsStream(texName));
                        textures.put(texName, TextureLoader.getInstance().loadTexture(texImg));
                        mat.textures.add(texName);
                    }

                }

                // ready for next line
                prevLine = i + 1;
            }
        }
    }

    /**
     * Returns a int from the character sequence.
     *
     * @param str
     * @param from
     * @param to
     * @return An int.
     */
    private int toInt(CharSequence str, int from, int to) {
        int result = 0;
        int digit = 0;

        boolean isNegative = str.charAt(from) == '-' ? true : false;
        int i = isNegative || str.charAt(from) == '+' ? from + 1 : from;
        while(i <= to) {
            if(str.charAt(i) >= '0' && str.charAt(i) <= '9')
                digit = str.charAt(i) - '0';
            else
                throw new NumberFormatException("Number contains illegal " +
                        "character(s) " + str.charAt(i));

            result = 10*result + digit;
            i++;
        }

        return isNegative ? -result : result;
    }

    /**
     * Returns a float from the character sequence.
     *
     * @param str The character sequence.
     * @param from The index to begin from in the character sequence.
     * @param to The end index in the character sequence.
     * @return A float.
     */
    private float toFloat(CharSequence str, int from, int to) {
        float result = 0.0f;
        int digit = 0;
        boolean isNegative = str.charAt(from) == '-' ? true : false;
        int i = isNegative || str.charAt(from) == '+' ? from + 1 : from;
        int decPlaces = 0;

        // is number valid?
        if(str.charAt(i) == 'N')
            return Float.NaN;
        else if(str.charAt(i) == 'I')
            return isNegative ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;

        boolean hasFraction = false;
        boolean hasExponent = false;

        // read integer part
        while(i < to + 1) {
            if(str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                digit = str.charAt(i) - '0';
            } else if(str.charAt(i) == '.') {
                hasFraction = true;
                i++;
                break;
            } else if(str.charAt(i) == 'e' || str.charAt(i) == 'E') {
                hasExponent = true;
                i++;
                break;
            } else {
                throw new NumberFormatException("Float contains illegal " +
                        "character(s) " + str.charAt(i));
            }
            result = 10*result + digit;
            i++;
        }

        // read fraction part (if any)
        if(hasFraction) {
            while(i < to + 1) {
                if(str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                    digit = str.charAt(i) - '0';
                    decPlaces++;
                } else if(str.charAt(i) == 'e' || str.charAt(i) == 'E') {
                    hasExponent = true;
                    i++;
                    break;
                } else {
                    throw new NumberFormatException("Float contains illegal " +
                        "character(s) " + str.charAt(i));
                }
                result = 10*result + digit;
                i++;
            }
        }
        result = result / (float) Math.pow(10, decPlaces);

        // do exponent
        if(hasExponent) {
            int E = 0;
            boolean isNegativeE = str.charAt(i) == '-' ? true : false;
            int j = isNegativeE || str.charAt(i) == '+' ? i+1 : i;

            while(j < to + 1) {
                if(str.charAt(j) >= '0' && str.charAt(j) <= '9')
                    digit = str.charAt(j) - '0';
                else
                    throw new NumberFormatException("Float contains illegal " +
                            "character(s) " + str.charAt(j));

                E = 10*E + digit;
                j++;
            }
            E = isNegativeE ? -E : E;

            result = result * (float) Math.pow(10, E);
        }

        return isNegative ? -result : result;
    }
    
    /**
     * Returns the next token after 'from' in the string and stores the token's
     * begin and end index in the store array.
     *
     * @param str the string.
     * @param begin where in the string to start dearching from.
     * @param end the end of the line.
     * @param store the token begin and end index's.
     */
    private void nextToken(StringBuilder str, int begin, int end, char del, int[] store) {
        //  get first token (line type)
        int tokBegin = -1;
        int tokEnd;
        for(int j=begin; j<=end; j++) {
            if(tokBegin == -1 && str.charAt(j) != del)
                tokBegin = j;

            if(tokBegin != -1) {
                if(str.charAt(j) == del || str.charAt(j) == '\n' || str.charAt(j) == '\r') {
                    tokEnd = j - 1;
                    store[0] = tokBegin;
                    store[1] = tokEnd;
                    break;
                } else if(j == end) {
                    tokEnd = j;
                    store[0] = tokBegin;
                    store[1] = tokEnd;
                    break;
                }
            }
        }
    }

    /**
     * Returns the number of tokens in the line.
     *
     * @param str
     * @param begin
     * @param end
     * @param del
     * @return
     */
    private int countTokens(StringBuilder str, int begin, int end, char del) {
        int numTokens = 0;
        int tokBegin = -1;
        for(int j=begin; j<=end; j++) {
            if(tokBegin == -1 && str.charAt(j) != del)
                tokBegin = j;

            if(tokBegin != -1) {
                if(str.charAt(j) == del) {
                    numTokens++;
                    tokBegin = -1;
                    j++;
                } else if(j == end) {
                    numTokens++;
                    tokBegin = -1;
                    j++;
                }
            }
        }

        return numTokens;
    }

    private class Group {
        public String name;
        public int numOfFaces;
        public ArrayList<Face> faces;
        public Material mtl;

        public Group() {
            faces = new ArrayList<Face>();
        }

        public Visual convert() {
            // create the geometry
            Vec3[] vertexArray = new Vec3[numOfFaces*3];
            Vec3[] normalArray = new Vec3[numOfFaces*3];
            Vec2[] texCoordArray = new Vec2[numOfFaces*3];
            for (int f=0; f<numOfFaces; f++) {
                Face face = faces.get(f);

                for(int i=0; i<3; i++) {
                    vertexArray[f*3 + i] = vertices.get(face.vertexIndices[i] - 1); // REUSE VECTOR3

                    if(hasTexCoords)
                        texCoordArray[f*3 + i] = texCoords.get(face.texCoordIndices[i] - 1); // REUSE VECTOR2
                    if(hasNormals)
                        normalArray[f*3 + i] = normals.get(face.normalIndices[i] - 1); // REUSE VECTOR3
                }
            }

            Geom model;
            if(hasTexCoords && hasNormals) {
                model = new Geom(name, new Trimesh(Trimesh.DrawMode.TRIANGLES, vertexArray, normalArray));
                model.getTrimesh().addTexCoords(texCoordArray);
            } else if (hasNormals) {
                model = new Geom(name, new Trimesh(Trimesh.DrawMode.TRIANGLES, vertexArray, normalArray));
            } else {
                model = new Geom(name, new Trimesh(Trimesh.DrawMode.TRIANGLES, vertexArray));
            }

            // create the material
            if(mtl != null) {
                logger.log(Level.INFO, "Adding colour & material to geometry");
                model.getLocalAppearance().override(App.MATERIAL | App.COLOUR);
                
                // add colour
                model.getLocalAppearance().colour = new Colour(mtl.diffuse);
                
                // add material
                com.spookengine.scenegraph.appearance.Material newMat = new com.spookengine.scenegraph.appearance.Material();
                newMat.setShininess(mtl.shininess);
                newMat.setAmbient(mtl.ambient[0], mtl.ambient[1], mtl.ambient[2]);
                newMat.setDiffuse(mtl.diffuse[0], mtl.diffuse[1], mtl.diffuse[2]);
                newMat.setSpecular(mtl.specular[0], mtl.specular[1], mtl.specular[2]);

                model.getLocalAppearance().material = newMat;
                if(mtl.alpha < 1) {
                    model.getLocalAppearance().override(App.ALPHA);

                    Alpha tran = new Alpha(mtl.alpha);
                    model.getLocalAppearance().alpha = tran;
                }

                // create textures
                for(String texture : mtl.textures) {
                    model.getLocalAppearance().addTexture(textures.get(texture));
                }
            }

            return model;
        }
    }
    
    private class Face {
        public int[] vertexIndices = new int[3];
        public int[] normalIndices = new int[3];
        public int[] texCoordIndices = new int[3];
    }

    private class Material {
        public float shininess;
        public float[] ambient = new float[3];
        public float[] diffuse = new float[3];
        public float[] specular = new float[3];
        public float alpha;
        public ArrayList<String> textures = new ArrayList<String>();
    }
}
