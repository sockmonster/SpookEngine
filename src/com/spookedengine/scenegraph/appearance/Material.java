package com.spookedengine.scenegraph.appearance;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 *
 * @author Oliver Winks
 */
public class Material {
    public static FloatBuffer defaultAmbient;
    public static FloatBuffer defaultDiffuse;
    public static FloatBuffer defaultSpecular;
    public static FloatBuffer defaultEmissive;
    private FloatBuffer ambientBuf;
    private FloatBuffer diffuseBuf;
    private FloatBuffer specularBuf;
    private FloatBuffer emissiveBuf;

    private float shininess;
    private float[] ambient;
    private float[] diffuse;
    private float[] specular;
    private float[] emissive;

    public Material() {
        init();

        toDefault();
    }

    public Material(float[] ambient) {
        init();

        toDefault(); // needed to initialise arrays
        setAmbient(ambient);
    }

    public Material(float[] ambient, float[] diffuse) {
        init();

        toDefault(); // needed to initialise arrays
        setAmbient(ambient);
        setDiffuse(diffuse);
    }

    public Material(float[] ambient, float[] diffuse, float[] specular) {
        init();

        toDefault(); // needed to initialise arrays
        setAmbient(ambient);
        setDiffuse(diffuse);
        setSpecular(specular);
    }

    public Material(float[] ambient, float[] diffuse, float[] specular, float[] emissive) {
        init();

        toDefault(); // needed to initialise arrays
        setAmbient(ambient);
        setDiffuse(diffuse);
        setSpecular(specular);
        setEmissive(emissive);
    }

    private void init() {
        ByteBuffer buf;
        if(defaultAmbient == null) {
            buf = ByteBuffer.allocateDirect(16);
            buf.order(ByteOrder.nativeOrder());
            defaultAmbient = buf.asFloatBuffer();
            defaultAmbient.put(new float[] {0.5f, 0.5f, 0.5f, 1f});
        }

        if(defaultDiffuse == null) {
            buf = ByteBuffer.allocateDirect(16);
            buf.order(ByteOrder.nativeOrder());
            defaultDiffuse = buf.asFloatBuffer();
            defaultDiffuse.put(new float[] {0.5f, 0.5f, 0.5f, 1f});
        }

        if(defaultSpecular == null) {
            buf = ByteBuffer.allocateDirect(16);
            buf.order(ByteOrder.nativeOrder());
            defaultSpecular = buf.asFloatBuffer();
            defaultSpecular.put(new float[] {0f, 0f, 0f, 1f});
        }

        if(defaultEmissive == null) {
            buf = ByteBuffer.allocateDirect(16);
            buf.order(ByteOrder.nativeOrder());
            defaultEmissive = buf.asFloatBuffer();
            defaultEmissive.put(new float[] {0f, 0f, 0f, 1f});
        }

        // initialise FloatBuffers
        buf = ByteBuffer.allocateDirect(16);
        buf.order(ByteOrder.nativeOrder());
        ambientBuf = buf.asFloatBuffer();

        buf = ByteBuffer.allocateDirect(16);
        buf.order(ByteOrder.nativeOrder());
        diffuseBuf = buf.asFloatBuffer();

        buf = ByteBuffer.allocateDirect(16);
        buf.order(ByteOrder.nativeOrder());
        specularBuf = buf.asFloatBuffer();

        buf = ByteBuffer.allocateDirect(16);
        buf.order(ByteOrder.nativeOrder());
        emissiveBuf = buf.asFloatBuffer();

        ambient = new float[3];
        diffuse = new float[3];
        specular = new float[3];
        emissive = new float[3];
    }

    public void setTo(Material att) {
        this.shininess = att.shininess;

        for(int i=0; i<3; i++) {
            ambient[i] = att.ambient[i];
            diffuse[i] = att.diffuse[i];
            specular[i] = att.specular[i];
            emissive[i] = att.emissive[i];
        }
    }

    public void setShininess(float shininess) {
        if(shininess >= 0 && shininess <= 1)
            this.shininess = shininess;
    }

    public float getShininess() {
        return shininess;
    }

    public void setAmbient(float... rgbArray) {
        for(int i=0; i<3; i++)
            ambient[i] = rgbArray[i];
    }

    public float[] getAmbient() {
        return ambient;
    }

    public FloatBuffer getAmbient(float alpha) {
        ambientBuf.position(0);
        ambientBuf.put(ambient);
        ambientBuf.put(alpha);
        ambientBuf.position(0);
        return ambientBuf;
    }

    public void setDiffuse(float... rgbArray) {
        for(int i=0; i<3; i++)
            diffuse[i] = rgbArray[i];
    }

    public float[] getDiffuse() {
        return diffuse;
    }

    public FloatBuffer getDiffuse(float alpha) {
        diffuseBuf.position(0);
        diffuseBuf.put(diffuse);
        diffuseBuf.put(alpha);
        diffuseBuf.position(0);
        return diffuseBuf;
    }

    public void setSpecular(float... rgbArray) {
        for(int i=0; i<3; i++)
            specular[i] = rgbArray[i];
    }

    public float[] getSpecular() {
        return specular;
    }

    public FloatBuffer getSpecular(float alpha) {
        specularBuf.position(0);
        specularBuf.put(specular);
        specularBuf.put(alpha);
        specularBuf.position(0);
        return specularBuf;
    }

    public void setEmissive(float... rgbArray) {
        for(int i=0; i<3; i++)
            emissive[i] = rgbArray[i];
    }

    public float[] getEmissive() {
        return emissive;
    }

    public FloatBuffer getEmissive(float alpha) {
        emissiveBuf.position(0);
        emissiveBuf.put(emissive);
        emissiveBuf.put(alpha);
        emissiveBuf.position(0);
        return emissiveBuf;
    }
    
    public void toDefault() {
        shininess = 0;

        for(int i=0; i<3; i++) {
            ambient[i] = 0.5f;
            diffuse[i] = 0.5f;
            specular[i] = 0f;
            emissive[i] = 0f;
        }
    }

}
