/* Hailey Campbell 
 * CSC 3560
 * 6/15/2026
 * Interface: Subject
 * This interface is a Subject in the observer pattern, representing an object that can be observed. 
 * Implemented by User
 */

public interface Subject {
    /* Method: addObserver
     * This method is used to add an observer to the subject.
     */
    void attach(Observer o);

    /* Method: removeObserver
     * This method is used to remove an observer from the subject.
     */
    void detach(Observer o);

    /* Method: notifyObservers
     * This method is used to notify all observers of a change.
     */
    void notifyObservers(Object arg);
}