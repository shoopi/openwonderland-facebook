
package uk.ac.essex.wonderland.modules.facebook.server;



public interface FacebookRunnable<V> {
    /**
     * Run the query with the given connection
     */
    void run();

    /**
     * Return the result
     */
    V getResult();

    /**
     * Return the error
     */
//    int getWidth();
    
    Throwable getError();
}
