
package uk.ac.essex.wonderland.modules.facebook.server;

import com.sun.sgs.app.PeriodicTaskHandle;
import com.sun.sgs.app.Task;


/**
 * A service for performing Facebook operations as a Darkstar service.
 */
public class FacebookManager {
    FacebookServiceImpl service;

    /**
     * Constructor, required by a manager object.
     **/
    public FacebookManager(FacebookServiceImpl service) {
        this.service = service;
    }

    /**
     * Execute the given runnable and pass results to the given callback.
     */
    public <T> void execute(FacebookRunnable<T> runnable, FacebookCallback<T> callback) {
        service.execute(runnable, callback);
    }
}
