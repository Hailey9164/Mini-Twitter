/* Hailey Campbell 
 * CSC 3560
 * 6/15/2026
 * Class: StatsVisitor
 * This class is used to visit users and user groups and update the statistics.
 * 
 * Design Pattern: Visitor
 */

import java.util.*;

public class StatsVisitor implements Visitor {
    // Counters for the number of users, groups, tweets, and positive tweets
    private int userCount = 0;

    // Counters for the number of groups, tweets, and positive tweets
    private int groupCount = 0;

    // Counters for the number of tweets and positive tweets
    private int tweetCount = 0;

    // Counter for the number of positive tweets
    private int positiveTweets = 0;

    // Set of positive words for sentiment analysis
    private final Set<String> positiveWords = Set.of("good", "great", "excellent", "amazing", "wonderful", "fantastic", "nice", "awesome");

    /* Mehtod: visitUser
     * Visits a user and updates the statistics.
     */
    @Override
    public void visitUser(User user) {
        userCount++;

        for (String msg : user.getNewsFeed()) {
            tweetCount++;
            if (isPositive(msg)) {
                positiveTweets++;
            }
        }
    }

    /* Method: visitUserGroup
     * Visits a user group and updates the statistics.
     */
    @Override
    public void visitUserGroup(UserGroup group) {
        groupCount++;
    }

    /* Method: isPositive
     * Checks if a message is positive.
     */
    private boolean isPositive(String msg) {
        String lower = msg.toLowerCase();
        for (String word : positiveWords) {
            if (lower.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /* Method: getUserCount
     * Returns the number of users.
     */
    public int getUserCount() {
        return userCount;
    }

    /* Method: getGroupCount
     * Returns the number of groups.
     */
    public int getGroupCount() {
        return groupCount;
    }

    /* Method: getTweetCount
     * Returns the number of tweets.
     */
    public int getTweetCount() {
        return tweetCount;
    }

    /* Method: getPositivePercentage
     * Returns the percentage of positive tweets.
     */
    public int getPositivePercentage() {
        if (tweetCount == 0) {
            return 0;
        }
        return (int) ((double) positiveTweets / tweetCount * 100);
    }
}