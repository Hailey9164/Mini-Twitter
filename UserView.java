/* Hailey Campbell 
 * CSC 3560
 * 6/15/2026
 * Class: UserView
 * Main GUI window for viewing user information.
 * Manages:
 * - displaying user details
 * - editing user information
 * 
 * Design Pattern: Observer
 */

import java.awt.*;
import javax.swing.*;

public class UserView extends JFrame implements Observer {
    // The user whose information is being displayed
    private final User user;

    // The model for the following list
    private final DefaultListModel<String> followingModel = new DefaultListModel<>();
    private final DefaultListModel<String> feedModel = new DefaultListModel<>();

    //GUI components
    private JList<String> followingList;
    private JList<String> newsfeedList;

    private JTextField followUserField;
    private JTextField tweetField;

    private JButton followButton;
    private JButton postButton;

    /*
     * Method: UserView
     * Constructor to initialize the user view.
     */
    public UserView(User user) {
        super("User View: " + user.getId());
        this.user = user;

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buildUI();
                
        SwingUtilities.invokeLater(() -> {
            user.attachGUI(this);
            refresh();
        });
    }

    /*
     * Method: buildUI
     * Builds the user interface for the view.
     */
    private void buildUI() {
        setLayout(new BorderLayout());

        // Follow user // 
        JPanel followPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        followPanel.add(new JLabel("User ID:"));
        followUserField = new JTextField(15);
        followPanel.add(followUserField);
        followButton = new JButton("Follow User");
        followPanel.add(followButton);
        add(followPanel, BorderLayout.NORTH);

        // Following list + news feed //
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // current following
        JPanel followingPanel = new JPanel(new BorderLayout());
        followingPanel.add(new JLabel("Following:"), BorderLayout.NORTH);
        followingList = new JList<>(followingModel);
        followingPanel.add(new JScrollPane(followingList), BorderLayout.CENTER);
        centerPanel.add(followingPanel);

        // Newsfeed list
        JPanel feedPanel = new JPanel(new BorderLayout());
        feedPanel.add(new JLabel("Newsfeed:"), BorderLayout.NORTH);
        newsfeedList = new JList<>(feedModel);
        feedPanel.add(new JScrollPane(newsfeedList), BorderLayout.CENTER);
        centerPanel.add(feedPanel);

        add(centerPanel, BorderLayout.CENTER);

        // tweet message + post //
        JPanel tweetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tweetPanel.add(new JLabel("Tweet Message:"));
        tweetField = new JTextField(20);
        tweetPanel.add(tweetField);
        postButton = new JButton("Post Tweet");
        tweetPanel.add(postButton);
        add(tweetPanel, BorderLayout.SOUTH);

        // Listeners
        hookLListeners();
    }

    /*
     * Method: hookLListeners
     * Hooks up the listeners for the buttons.
     */
    private void hookLListeners() {
        followButton.addActionListener(e -> onFollowUser());
        postButton.addActionListener(e -> { onPostTweet(); });
    }

    /*
     * Method: onFollowUser
     * Handles the follow user button click event.
     */
    private void onFollowUser() {
        // Clear the text field
        String targetId = followUserField.getText().trim();
        // Check if the user ID is empty
        if (targetId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID cannot be empty.");
            return;
        }

        // Find the user component with the given ID
        UserComponent comp = AdminControlPanel.instance().find(targetId);
        if (!(comp instanceof User target)) {
            JOptionPane.showMessageDialog(this, "User not found.");
            return;
        }

        // Follow the target user
        user.follow(target);
        followUserField.setText("");
        refresh();
    }

    /*
     * Method: onPostTweet
     * Handles the post tweet button click event.
     */ 
    private void onPostTweet() {
        // Clear the text field
        String msg = tweetField.getText().trim();
        // Check if the tweet message is empty
        if (msg.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tweet message cannot be empty.");
            return;
        }

        // Post the tweet
        user.postTweet(msg);
        tweetField.setText("");
        refresh();
    }

    /*
     * Method: refresh
     * Refreshes the view with the latest data.
     */
    private void refresh() {
        // Clear the following list
        followingModel.clear();

        // Clear the newsfeed list
        for (User u : user.getFollowings()) {
            followingModel.addElement(u.getId());
        }

        // Add the following users to the list
        feedModel.clear();

        // Add the newsfeed messages to the list    
        for (String msg : user.getNewsFeed()) {
            feedModel.addElement(msg);
        }
    }

    /*
     * Method: update
     * Updates the view with the latest data.
     */
    @Override
    public void update(Subject subject, Object arg) {
        refresh();
    }
}
