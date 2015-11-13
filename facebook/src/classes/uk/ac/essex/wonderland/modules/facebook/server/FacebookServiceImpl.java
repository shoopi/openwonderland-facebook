package uk.ac.essex.wonderland.modules.facebook.server;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.auth.Identity;
import com.sun.sgs.impl.sharedutil.LoggerWrapper;
import com.sun.sgs.impl.util.AbstractService;
import com.sun.sgs.impl.util.TransactionContext;
import com.sun.sgs.impl.util.TransactionContextFactory;
import com.sun.sgs.kernel.ComponentRegistry;
import com.sun.sgs.kernel.KernelRunnable;
import com.sun.sgs.service.Transaction;
import com.sun.sgs.service.TransactionProxy;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FacebookServiceImpl extends AbstractService {
    /** The logger for this class. */
    private static final LoggerWrapper logger =
        new LoggerWrapper(Logger.getLogger(FacebookServiceImpl.class.getName()));


    /** manages the context of the current transaction */
    private final TransactionContextFactory<FacebookTransactionContext> ctxFactory;

    /** executor */
    private ScheduledExecutorService executor;

    public FacebookServiceImpl(Properties prop,
            ComponentRegistry registry,
            TransactionProxy transactionProxy) {
        super(prop, registry, transactionProxy, logger);

        // create the transaction context factory
        ctxFactory = new TransactionContextFactoryImpl(transactionProxy);
        //executor.scheduleAtFixedRate(null , 1, 30, TimeUnit.SECONDS);
    }

    @Override
    public String getName() {
        return FacebookServiceImpl.class.getName();
    }

    @Override
    protected void doReady() throws Exception {
        // create the executor thread
        executor = Executors.newSingleThreadScheduledExecutor();
                //newSingleThreadExecutor();
    }

    @Override
    protected void doShutdown() {
        // stop the executor, attempting an orderly shutdown
        boolean shutdown = false;
        executor.shutdown();

        try {
           shutdown = executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            // ignore
        }

        if (!shutdown) {
            List<Runnable> leftover = executor.shutdownNow();
            logger.log(Level.WARNING, "Terminating executor with tasks in" +
                       "  progress: " + leftover);
        }
    }

    @Override
    protected void handleServiceVersionMismatch(Version oldVersion,
            Version currentVersion) {
        throw new IllegalStateException(
                "unable to convert version:" + oldVersion
                + " to current version:" + currentVersion);
    }

    <T> void execute(FacebookRunnable<T> runnable, FacebookCallback<T> callback) {
        // add data to the transaction
        ctxFactory.joinTransaction().add(runnable, callback);
    }

    /**
     * Transaction state
     */
    private class FacebookTransactionContext extends TransactionContext {
        private final List<FacebookRunner> runners = new ArrayList<FacebookRunner>();

        public FacebookTransactionContext(Transaction txn) {
            super (txn);
        }

        public <T> void add(FacebookRunnable<T> runnable, FacebookCallback<T> callback) {
            BigInteger callbackID = null;
            if (callback != null) {
                // wrap the callback if it is not a ManagedObject
                if (!(callback instanceof ManagedObject)) {
                    callback = new FacebookCallbackWrapper<T>(callback);
                }

                // create a managed reference for the callback, which we know
                // is a managed object because it was wrapped above
                ManagedReference<FacebookCallback<T>> callbackRef =
                        AppContext.getDataManager().createReference(callback);

                // get the ID  of the callback
                callbackID = callbackRef.getId();
            }

            // add a runner to our list that will be run on commit
            runners.add(new FacebookRunner(runnable, callbackID,
                                      txnProxy.getCurrentOwner()));
        }
        
        @Override
        public void abort(boolean retryable) {
            runners.clear();
        }

        @Override
        public void commit() {
            // execute each runner on a separate thread
            for (FacebookRunner runner : runners) {
                executor.submit(runner);
            }
            runners.clear();
        }
    }

    /** Private implementation of {@code TransactionContextFactory}. */
    private class TransactionContextFactoryImpl
            extends TransactionContextFactory<FacebookTransactionContext> {

        /** Creates an instance with the given proxy. */
        TransactionContextFactoryImpl(TransactionProxy proxy) {
            super(proxy, FacebookServiceImpl.class.getName());
        }

        /** {@inheritDoc} */
        protected FacebookTransactionContext createContext(Transaction txn) {
            return new FacebookTransactionContext(txn);
        }
    }

    private class FacebookRunner implements Runnable {
        private final FacebookRunnable<?> runnable;
        private final BigInteger callbackID;
        private final Identity identity;

        public FacebookRunner(FacebookRunnable<?> runnable, BigInteger callbackID,
                         Identity identity)
        {
            this.runnable = runnable;
            this.callbackID = callbackID;
            this.identity = identity;
        }

        public void run() {
            runnable.run();

            if (callbackID != null) {
                FacebookCallbackKernelRunnable kernelRunnable =
                    new FacebookCallbackKernelRunnable(callbackID,
                                                  runnable.getResult(),
                                                  runnable.getError());
                transactionScheduler.scheduleTask(kernelRunnable, identity);
                //transactionScheduler.scheduleRecurringTask(kernelRunnable, identity, 2, 30);
            }
        }
    }

    private class FacebookCallbackKernelRunnable implements KernelRunnable {
        private final BigInteger callbackID;
        private final Object result;
        //private final int width;
        private final Throwable error;

        public FacebookCallbackKernelRunnable(BigInteger callbackID, Object result, //int width,
                                         Throwable error)
        {
            this.callbackID = callbackID;
            //this.width = width;
            this.result = result;
            this.error = error;
        }

        public String getBaseTaskType() {
            return FacebookServiceImpl.class.getName() + ".FacebookCallbackKernelRunnable";
        }

        public void run() throws Exception {
            // create a managed reference from the callback id we were given
            ManagedReference<FacebookCallback> callbackRef = (ManagedReference<FacebookCallback>)
                    dataService.createReferenceForId(callbackID);

            // execute the callback
            FacebookCallback callback = callbackRef.get();
            if (error != null) {
                callback.handleError(error);
            } else {
                callback.handleResult(result);
            }

            // clean up the task in the data store
            if (callback instanceof FacebookCallbackWrapper) {
                dataService.removeObject(callback);
            }
        }
    }

    /**
     * Wrapper for SqlCallback implemented as a managed object
     */
    static class FacebookCallbackWrapper<V>
            implements FacebookCallback<V>, ManagedObject, Serializable
    {
        private FacebookCallback<V> callback;

        public FacebookCallbackWrapper(FacebookCallback<V> callback) {
            this.callback = callback;
        }

        public void handleResult(V result) {
            callback.handleResult(result);
        }

        public void handleError(Throwable error) {
            callback.handleError(error);
        }
    }
}
