package org.apache.tomcat.util.threads;

public interface ThreadPoolRunnable {

    /*
    Called when this object is first loaded in the thread pool.
    Important: all workers in a pool must be of the same type, otherwise the mechanism becomes more complex.
     */
    Object[] getInitData();
    /*
    This method will be executed in one of the pool's threads. The thread will be returned to the pool.
     */
    void runIt(Object[] thData);
}
