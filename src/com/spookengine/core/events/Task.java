package com.spookengine.core.events;

/**
 *
 * @author Oliver Winks
 */
public abstract class Task {
    public enum TaskState {DONE, CONTINUE_RUN, DELAY_RUN, PAUSE}
    
    // vars read/write by TaskScheduler
    public TaskState state;
    public float delay;
    float timeElapsed;
    float runTime;
    float pauseTime;
    int frames;

    public Task() {
        state = TaskState.CONTINUE_RUN;
    }

    public Task(float delay) {
        state = TaskState.DELAY_RUN;
        this.delay = delay;
    }

    /**
     * Returns the amount of time (in milliseconds) that this task has been
     * running including delay time.
     *
     * @return The total run time of this task (including delay time).
     */
    public float getRunTime() {
        return runTime;
    }

    /**
     * Returns the number of times this task has been performed.
     *
     * @return The number of times this task has been performed.
     */
    public int getFrames() {
        return frames;
    }

    public TaskState whilePaused(float tpf) {
        return TaskState.PAUSE;
    }

    public abstract TaskState perform(float tpf);

}
