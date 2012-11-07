package com.spookengine.core.events;

import java.util.*;

/**
 * The TaskScheduler is responsible for calling Tasks. It has an internal timer
 * that is used to schedule Tasks. The TaskScheduler alos implements a messaging
 * framework that allows Tasks to be invoked on any event. Events can come from
 * the OS (user interaction) or from within the game itself (collision events or
 * user defined events).
 *
 * @author Oliver Winks
 */
public final class TaskScheduler implements Cloneable {
    private static Map<String, TaskScheduler> instances = new HashMap<String, TaskScheduler>();

    // timer
    public boolean firstRun;
    private long prevTime;
    private long tpf;

    // normal tasks
    private SortedMap<Integer, List<Task>> tasks;
    private SortedMap<Integer, List<Task>> addTasks;
    private List<Task> removalList;

    private TaskScheduler() {
        // init timer
        firstRun = true;
        prevTime = 0;
        tpf = 0;

        // init tasks
        tasks = new TreeMap<Integer, List<Task>>();
        addTasks = new TreeMap<Integer, List<Task>>();
        removalList = new ArrayList<Task>();
    }

    /**
     * Returns the single instance of the TaskScheduler.
     *
     * @return The TaskScheduler instance.
     */
    public static TaskScheduler getInstance(String name) {
        TaskScheduler scheduler = instances.get(name);
        if(scheduler == null)
            instances.put(name, scheduler = new TaskScheduler());

        return scheduler;
    }


    public void schedule(int priority, Task task) {
        if(!addTasks.containsKey(priority))
            addTasks.put(priority, new ArrayList<Task>());

        addTasks.get(priority).add(task);
    }

    public void unschedule(Task task) {
        for(int priority : tasks.keySet())
            tasks.get(priority).remove(task);
    }

    public void unscheduleAll() {
        tasks.clear();
    }

    public void update() {
        // schedule new tasks
        for(int priority : addTasks.keySet()) {
            for(Task task : addTasks.get(priority)) {
                if(!tasks.containsKey(priority))
                    tasks.put(priority, new ArrayList<Task>());

                tasks.get(priority).add(task);
            }
        }
        addTasks.clear();

        // is this the first run?
        if(firstRun) {
            prevTime = System.currentTimeMillis();
            firstRun = false;
        }
        tpf = System.currentTimeMillis() - prevTime;

        for(int priority : tasks.keySet()) {
            for(Task task : tasks.get(priority)) {

                switch(task.state) {
                    case DONE:
                        removalList.add(task);
                        break;

                    case PAUSE:
                        task.runTime = 0;
                        task.timeElapsed = 0;
                        task.pauseTime += (float) tpf/1000;
                        task.whilePaused((float) tpf/1000);
                        break;

                    case CONTINUE_RUN:
                        task.runTime += (float) tpf/1000;
                        task.timeElapsed = 0;
                        task.pauseTime = 0;
                        task.frames++;
                        task.state = task.perform((float) tpf/1000);
                        break;

                    case DELAY_RUN:
                        task.runTime += (float) tpf/1000;
                        task.timeElapsed += (float) tpf/1000;
                        task.pauseTime = 0;

                        if(task.timeElapsed >= task.delay) {
                            task.frames++;
                            task.state = task.perform((float) tpf/1000);
                            task.timeElapsed = 0;
                        }
                        break;
                }
                
            }

            // remove tasks
            if(!removalList.isEmpty()) {
                for(int i=0; i<removalList.size(); i++)
                    tasks.get(priority).remove(removalList.get(i));

                removalList.clear();
            }
        }

        prevTime = System.currentTimeMillis();
    }

    @Override
    public Object clone() {
        throw new UnsupportedOperationException("TaskScheduler is a singleton!");
    }

}
