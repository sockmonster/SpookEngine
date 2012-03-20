package com.spookengine.scenegraph.appearance;

import com.spookengine.events.Task;
import com.spookengine.events.TaskScheduler;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oliver Winks
 */
public class AnimTexture extends Texture {
    private static final Logger logger = Logger.getLogger(AnimTexture.class.getName());

    private Texture[] frames;
    private float[] times;
    private int currFrame;

    private AnimateTask animate;
    private boolean isAnimating;

    public AnimTexture() {
        super();

        isAnimating = false;
        animate = new AnimateTask();
    }
    
    @Override
    public int[] getTexturePointer() {
        return frames[currFrame].getTexturePointer();
    }
    
    @Override
    public int getWidth(int img) {
        return frames[currFrame].getWidth(img);
    }

    @Override
    public int getHeight(int img) {
        return frames[currFrame].getHeight(img);
    }
    
    @Override
    public void setBitmaps(List<ByteBuffer> bitmaps) {
        // do nothing
        logger.log(Level.WARNING, "Cannot set bitmaps on an animated texture. " +
                "Try getting the current texture and setting it's bitmaps.");
    }
    
    @Override
    public void setBitmap(int i, ByteBuffer bitmap) {
        // do nothing
        logger.log(Level.WARNING, "Cannot set a bitmap on an animated texture. " +
                "Try getting the current texture and setting it's bitmap.");
    }
    
    @Override
    public void addBitmap(ByteBuffer bitmap) {
        // do nothing
        logger.log(Level.WARNING, "Cannot add a bitmap on an animated texture. " +
                "Try getting the current texture and adding to it's bitmap data.");
    }
    
    @Override
    public ByteBuffer removeBitmap(int i) {
        // do nothing
        logger.log(Level.WARNING, "Cannot remove a bitmap from an animated texture. " +
                "Try getting the current texture and removing the bitmap from that.");
        
        return null;
    }
    
    @Override
    public List<ByteBuffer> getBitmaps() {
        return frames[currFrame].getBitmaps();
    }
    
    @Override
    public ByteBuffer getBitmap(int i) {
        return frames[currFrame].getBitmap(i);
    }

    @Override
    public boolean hasAlpha() {
        return frames[currFrame].hasAlpha;
    }

    public void setFrames(Texture... frames) {
        this.frames = frames;
    }

    public Texture[] getFrames() {
        return frames;
    }

    public void setTimes(float... times) {
        this.times = times;
    }

    public float[] getTimes() {
        return times;
    }

    public Texture getCurrentFrame() {
        return frames[currFrame];
    }

    public void start() {
        if(!isAnimating) {
            isAnimating = true;
            TaskScheduler.getInstance().schedule(0, animate);
        }
    }

    public void stop() {
        if(isAnimating) {
            isAnimating = false;
            TaskScheduler.getInstance().unschedule(animate);
        }
    }

    private class AnimateTask extends Task {
        private float frameTime;
        
        @Override
        public TaskState perform(float tpf) {
            frameTime += tpf;
            if(frameTime > times[currFrame]) {
                frameTime = 0;
                currFrame++;

                if(currFrame >= frames.length)
                    currFrame = 0;
            }

            return TaskState.CONTINUE_RUN;
        }
    }

}
