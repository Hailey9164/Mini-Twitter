/* Hailey Campbell 
 * CSC 3560
 * 6/15/2026
 * Interface: Observer
 * This interface is an Observer in the observer pattern, representing an object that can be notified of changes.
 * Implemented by User and UserView
 */

public interface Observer {
    /* Method: update
     * This method is called when the subject has changed.
     */
    void update(Subject subject, Object arg);
}