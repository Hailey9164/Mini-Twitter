/* Hailey Campbell 
 * CSC 3560
 * 6/15/2026
 * Class: User
 * This class is a leaf in the composite design pattern, representing an 
 * individual user. It is a Subject and an Observer for the observer pattern. 
 */

import java.util.ArrayList;
import java.util.List;

public class User extends UserComponent implements Subject, Observer {
    // Users who follow this user
    private final List<User> followers = new ArrayList<>();

    // Users this user is following
    private final List<User> followings = new ArrayList<>();

    // News feed of the user (own + followed users)
    private final List<String> newsFeed = new ArrayList<>();

    // List of observers for the observer pattern
    private final List<Observer> observers = new ArrayList<>();

    /* Method: User
     * This method is used to initialize the user with an ID using the parent class constructor.
     */
    public User(String id){
        super(id);
    }

    /* Method: follow
     * This method is used to follow another user.
     * Registers the current user as a Observer of the target user.
     */
    public void follow(User targetUser) {
        followings.add(targetUser);
        targetUser.attach(this);
    }

    /* Method: postTweet
     * This method is used to post a tweet to the user's news feed.
     * Notifies all observers of the new tweet.
     */
    public void postTweet(String message) {
        newsFeed.add(message);
        notifyObservers(message);
    }

    /* Method: getNewsFeed
     * This method is used to get the news feed of the user.
     */
    public List<String> getNewsFeed() {
        return newsFeed;
    }
    
    /* Method: getFollowings
     * This method is used to get the list of users this user is following.
     */
    public List<User> getFollowings() {
        return followings;
    }

    // Subject Methods

    /* Method: attach
     * This method is used to attach an observer to the subject.
     */
    @Override
    public void attach(Observer o) {
        followers.add((User) o);
    }

    /* Method: detach
     * This method is used to detach an observer from the subject.
     */
    @Override
    public void detach(Observer o) {
        followers.remove((User) o);
    }

    /* Method: notifyObservers
     * This method is used to notify all observers of the subject.
     * It notifies each observer and folllowers with the updated information.
     */
    @Override
    public void notifyObservers(Object arg) {
        for (User u : followers) {
            u.update(this, arg);
        }

        for (Observer obs : observers) {
            obs.update(this, arg);
        }
    }

    // Observer Methods

    /* Method: update
     * This method is used to update the observer with new information.
     */
    @Override
    public void update(Subject subject, Object arg) {
        newsFeed.add((String) arg);
        notifyGUI();
    }

    // GUI observers

    /* Method: attachGUI
     * This method is used to attach a GUI observer to the user.
     */
    public void attachGUI(Observer obs) {
        observers.add(obs);
    }

    /* Method: notifyGUI
     * This method is used to notify the GUI observer with new information.
     */
    private void notifyGUI() {
        for (Observer obs : observers) {
            obs.update(this, null);
        }
    }

    // visitor 

    /* Method: accept
     * This method is used to accept a visitor.
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visitUser(this);
    }
}