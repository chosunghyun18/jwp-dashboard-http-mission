package org.apache.tomcat.util.threads;

import java.util.Vector;

public class ThreadPool {
    static class ControlRunnable {
        void kill(){}
        void run(){}
        void runIt(Runnable toRun){}
        void runIt(ThreadPoolRunnable toRun){}
        void stop(){}
        void terminate(){}

    }
    static class MonitorRunnable {

    }
    interface ThreadPoolListener {
        void threadEnd(ThreadPool tp,Thread t);
        void threadStart(ThreadPool tp,Thread t);
    }

    protected int currentThreadCount; // Current number of threads in the pool
    protected int currentThreadsBusy; // Number of threads that are actively working
    protected boolean isDaemon = true;

    // Listeners to handle various thread pool events
    protected Vector<ThreadPoolListener> listeners = new Vector<>();

    public static int MAX_SPARE_THREADS = 50; // Maximum number of idle threads
    public static int MAX_THREADS = 200;      // Maximum number of threads
    public static int MAX_THREADS_MIN = 10;   // Minimum threshold for MAX_THREADS

    protected int maxSpareThreads = MAX_SPARE_THREADS; // Assigning default max spare threads
    protected int maxThreads = MAX_THREADS;            // Assigning default max threads
    protected int minSpareThreads = 10;                // Minimum number of spare threads

    public static int MIN_SPARE_THREADS = 4; // Minimum number of idle threads allowed
}
