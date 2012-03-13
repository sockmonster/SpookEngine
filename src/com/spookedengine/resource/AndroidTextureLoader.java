package com.spookedengine.resource;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import com.spookedengine.scenegraph.appearance.Texture;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author Oliver Winks
 */
public class AndroidTextureLoader extends TextureLoader {
    private Context context;

    public AndroidTextureLoader(Context context) {
        this.context = context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    /**
     * Loads a texture from the given filenames. If multiple filenames are given
     * then the texture is assumed to be mipmapped and each image is a level
     * (from largest to smallest) in the mipmap.
     *
     * <i>NOTE: If mipmapped then ALL levels from yXy to 1X1 must be given.</i>
     * <i>NOTE: For some strange reason textures loaded by Android are
     * upside-down and back-to-front. Be sure to flip the image horizontally and
     * rotate it by 180 deg before using it</i>
     * 
     * @param filenames
     * @return
     * @throws IOException
     */
    @Override
    public Texture loadTexture(String... filenames) throws IOException {
        int numOfImages = filenames.length;

        ByteBuffer[] bufs = new ByteBuffer[numOfImages];
        int[] width = new int[numOfImages];
        int[] height = new int[numOfImages];

        Bitmap[] bitmaps = new Bitmap[numOfImages];
        for(int i=0; i<numOfImages; i++) {
            // Get the texture from the Android resource directory
            InputStream is = context.getAssets().open(filenames[i]);
            bitmaps[i] = BitmapFactory.decodeStream(is);

            /* !!HACK!! */
            Matrix aMatrix = new Matrix();
            aMatrix.preScale(-1.0f, 1.0f);
            aMatrix.postRotate(180);
            bitmaps[i] = Bitmap.createBitmap(bitmaps[i], 0, 0,
                    bitmaps[i].getWidth(), bitmaps[i].getHeight(), aMatrix, false);
            width[i] = bitmaps[i].getWidth();
            height[i] = bitmaps[i].getHeight();
            /* ******** */

            // TODO: TO SLOW TO USE AT THE MO
//            width[i] = bitmaps[i].getWidth();
//            height[i] = bitmaps[i].getHeight();
//
//            bufs[i] = ByteBuffer.allocateDirect(height[i]*width[i]*4);
//            bufs[i].order(ByteOrder.nativeOrder());
//
//            for(int y=0; y<height[i]; y++) {
//                for(int x=0; x<width[i]; x++) {
//                    int pixel = bitmaps[i].getPixel(x, y);
//
//                    bufs[i].put(new byte[] {
//                        (byte) Color.red(pixel), (byte) Color.green(pixel),
//                        (byte) Color.blue(pixel), (byte) Color.alpha(pixel)});
//                }
//            }
//
//            bufs[i].position(0);
        }

        if(numOfImages > 0) {
            Texture tex = new Texture();
            tex.setBitmap(bitmaps);
            tex.setData(bufs);
            tex.setDimensions(width, height);

            return tex;
        } else {
            return null;
        }
    }

}
