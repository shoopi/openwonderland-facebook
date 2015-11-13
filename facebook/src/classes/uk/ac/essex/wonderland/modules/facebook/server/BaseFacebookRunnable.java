
package uk.ac.essex.wonderland.modules.facebook.server;

import com.restfb.FacebookClient;


public abstract class BaseFacebookRunnable<V> implements FacebookRunnable<V> {
    private V result;
    private Throwable error;
    private final FacebookClient facebookClient;
    
    public BaseFacebookRunnable(FacebookClient facebookClient) {
        this.facebookClient = facebookClient;
    }
    
    /**
     * An implementation of the run method. Subclasses should override
     * <code>execute()</code> instead of this method.
     * @param connection the connection to execute queries against
     */
    public void run() {
        try {
            setResult(execute(facebookClient));
        } catch (Throwable t) {
            setError(t);
        }
    }

    /**
     * Abstract method to actually perform the Facebook operation. In the case
     * of any error, this method should throw an exception.
     *
     * @param connection the connection to execute the query on
     * @return the return value for this execution
     * @throws Exception any exceptions thrown while executing the query
     */
    public abstract V execute(FacebookClient fbClient) throws Exception;

    /**
     * @inheritDoc
     */
    public V getResult() {
        return result;
    }

    /**
     * Set the result for this query. Used by <code>run()</code> to set the
     * result.
     * @param result the result to set.
     */
    protected void setResult(V result) {
        this.result = result;
    }

    /**
     * @inheritDoc
     */
    public Throwable getError() {
        return error;
    }

    /**
     * Set the error for this query. Used by <code>run()</code> to set the
     * error, if any.
     * @param error the error to set.
     */
    protected void setError(Throwable error) {
        this.error = error;
    }
}
