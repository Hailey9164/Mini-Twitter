/* Hailey Campbell 
 * CSC 3560
 * 6/15/2026
 * Class: AdminControlPanel
 * Main GUI window for the admin control panel.
 * Manages:
 * - adding users and user groups
 * - displaying the tree structure of users and user groups
 * - opening UserView windows
 * - User group management
 * - Statistics display via Visitor
 * 
 * Design Pattern: Singleton, Composite, Visitor
 */

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

public class AdminControlPanel extends JFrame {

    /** The singleton instance of the AdminControlPanel */
    private static AdminControlPanel pointer;


    /*
     * This method is used to get the singleton instance of the AdminControlPanel.
     */
    public static AdminControlPanel instance(){
        if (pointer == null){
            synchronized (AdminControlPanel.class) {
                if (pointer == null) {
                    pointer = new AdminControlPanel();
                }
            }
        }
        return pointer;
    }

    /*
     * This method is used to initialize the AdminControlPanel.
     */
    private AdminControlPanel() {
        super("Admin Control Panel");

        // 1. Create the Swing tree node for the root
        rootNode = new DefaultMutableTreeNode(root);

        // 2. Create the tree model
        treeModel = new DefaultTreeModel(rootNode);

        // 3. Create the JTree
        tree = new JTree(treeModel);  
        tree.setShowsRootHandles(true);   
        tree.setRootVisible(true);

        // 4. Create the tree
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLocationRelativeTo(null);

        // 5. Add the root to the registry
        registry.put(root.getId(), root);

        // 6. Build the UI
        buildUI();
    }

    /** The root user group of the system */
    private final UserGroup root = new UserGroup("Root");

    /** A map to store all user components by their ID */
    private final Map<String, UserComponent> registry = new HashMap<>();


    /** The JTree component for displaying the user hierarchy */
    private final JTree tree; 
    private final DefaultTreeModel treeModel;
    private final DefaultMutableTreeNode rootNode;


    /** Text fields for user and group IDs */
    private JTextField userIdField;
    private JTextField groupIdField;

    /** Buttons for user and group management */
    private JButton addUserButton;
    private JButton addGroupButton;
    private JButton openUserViewButton;
    private JButton showUserTotalButton;
    private JButton showGroupTotalButton;
    private JButton showMessagesTotalButton;
    private JButton showPositivePercentageButton;
    private JButton showLastUpdatedUserButton;
    private JButton verifyIDButton;


    /**
     * Method: addUser
     * 
     * Adds a user to the registry.
     * user: The user to add.
     */
    public void addUser(User user) {
        registry.put(user.getId(), user);
    }

    /*
     * Method: buildUI
     * 
     * Builds the user interface for the admin control panel.
     */
    private void buildUI() {
        // Set the layout for the main window
        setLayout(new BorderLayout());

        // Tree //
        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setPreferredSize(new Dimension(250, 0));
        add(treeScroll, BorderLayout.WEST);

        // Controls //
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        add(rightPanel, BorderLayout.CENTER);

        // Row: User id + Add User button
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel("User ID:"));
        userIdField = new JTextField(15);
        userPanel.add(userIdField);
        addUserButton = new JButton("Add User");
        userPanel.add(addUserButton);
        rightPanel.add(userPanel);

        // Row: Group id + Add Group button
        JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        groupPanel.add(new JLabel("Group ID:"));
        groupIdField = new JTextField(15);
        groupPanel.add(groupIdField);
        addGroupButton = new JButton("Add Group");
        groupPanel.add(addGroupButton);
        rightPanel.add(groupPanel);

        // Row: Open User View button
        JPanel openPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        openUserViewButton = new JButton("Open User View");
        openPanel.add(openUserViewButton);
        rightPanel.add(openPanel);

        // Row: Verify ID button and find last updated user button
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel verifyPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        verifyPanel.setMaximumSize(new Dimension(300, 120));
        verifyIDButton = new JButton("Verify IDs");
        showLastUpdatedUserButton = new JButton("Find Last Updated User");
        verifyPanel.add(verifyIDButton);
        verifyPanel.add(showLastUpdatedUserButton);

        rightPanel.add(verifyPanel);


        // Stats Buttons
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        statsPanel.setMaximumSize(new Dimension(300, 120));
        showUserTotalButton = new JButton("Show User Total");
        showGroupTotalButton = new JButton("Show Group Total");
        showMessagesTotalButton = new JButton("Show Messages Total");
        showPositivePercentageButton = new JButton("Show Positive Percentage");
        statsPanel.add(showUserTotalButton);
        statsPanel.add(showGroupTotalButton);
        statsPanel.add(showMessagesTotalButton);
        statsPanel.add(showPositivePercentageButton);
    
        rightPanel.add(statsPanel);

        // Set the cell renderer for the tree
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(
                    JTree tree, Object value, boolean sel, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {

                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object obj = node.getUserObject();

                if (obj instanceof UserGroup) {
                    setIcon(getDefaultOpenIcon());   // folder icon
                } else {
                    setIcon(getDefaultLeafIcon());   // file icon
                }

                return this;
            }
        });

        // Listeners
        hookListeners();
    }

    /*
     * Method: hookListeners
     * Hooks up the action listeners for the buttons.
     */
    private void hookListeners() {
        addUserButton.addActionListener(e -> onAddUser());
        addGroupButton.addActionListener(e -> onAddGroup());
        openUserViewButton.addActionListener(e -> onOpenUserView());

        showUserTotalButton.addActionListener(e -> onShowStats("user-total"));
        showGroupTotalButton.addActionListener(e -> onShowStats("group-total"));
        showMessagesTotalButton.addActionListener(e -> onShowStats("message-total"));
        showPositivePercentageButton.addActionListener(e -> onShowStats("positive-percent"));
        verifyIDButton.addActionListener(e -> onVerifyIDs());
        showLastUpdatedUserButton.addActionListener(e -> onShowLastUpdatedUser());
    }

    /*
     * Method: onAddUser
     * Adds a new user to the system.
     */
    private void onAddUser() {
        // Get the user ID from the text field
        String id = userIdField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID cannot be empty.");
            return;
        }
        if (registry.containsKey(id)){
            JOptionPane.showMessageDialog(this, "User ID already exists.");
            return;
        }

        // Create the new user and add it to the parent group
        UserGroup parentGroup = getSelectedGroupOrRoot();
        User user = new User(id);
        parentGroup.add(user);
        registry.put(id, user);

        // Find the parent node in the tree
        DefaultMutableTreeNode parentNode = findTreeNode(parentGroup);
        if ( parentNode == null) {
            parentNode = rootNode;
        }
        // Add the user node to the tree
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user);
        treeModel.insertNodeInto(userNode, parentNode, parentNode.getChildCount());
        tree.scrollPathToVisible(new javax.swing.tree.TreePath(userNode.getPath()));

        userIdField.setText("");
    }

    /*
     * Method: onAddGroup
     * Adds a new group to the system.
     */
    private void onAddGroup() {
        // Get the group ID from the text field
        String id = groupIdField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Group ID cannot be empty.");
            return;
        }
        if (registry.containsKey(id)) {
            JOptionPane.showMessageDialog(this, "Group ID already exists.");
            return;
        }

        // 1. Get the selected group (or root)
        UserGroup parentGroup = getSelectedGroupOrRoot();

        // 2. Create the new group
        UserGroup group = new UserGroup(id);
        parentGroup.add(group);
        registry.put(id, group);

        // 3. Find the parent node in the tree
        DefaultMutableTreeNode parentNode = findTreeNode(parentGroup);
        if (parentNode == null) {
            parentNode = rootNode;
        }

        // 4. Create a tree node for the *actual UserGroup object*
        DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(group);

        // 5. Insert the group node into the tree
        treeModel.insertNodeInto(groupNode, parentNode, parentNode.getChildCount());
        treeModel.reload(parentNode);
        tree.expandPath(new javax.swing.tree.TreePath(parentNode.getPath()));
        tree.setSelectionPath(new javax.swing.tree.TreePath(groupNode.getPath()));
        tree.scrollPathToVisible(new javax.swing.tree.TreePath(groupNode.getPath()));

        groupIdField.setText("");
    }

    /*
     * Method: onOpenUserView
     * Opens the user view for the selected user.
     */
    private void onOpenUserView() {
        // Get the selected user from the tree
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            JOptionPane.showMessageDialog(this, "Select a user in the tree.");
            return;
        }

        // Check if the selected node is a user
        Object obj = node.getUserObject();
        if (!(obj instanceof User user)) {
            JOptionPane.showMessageDialog(this, "Selected node is not a user.");
            return;
        }
        openUserView(user);
    }

    /*
     * Method: onShowStats
     * Shows the statistics for the selected type.
     */
    private void onShowStats(String type) {
        // Get the selected type from the combo box
        StatsVisitor visitor = new StatsVisitor();
        root.accept(visitor);

        // Show the statistics based on the selected type
        if (type.equals("user-total")) {
            JOptionPane.showMessageDialog(this,
                "Total Users: " + visitor.getUserCount());
        }
        else if (type.equals("group-total")) {
            JOptionPane.showMessageDialog(this,
                "Total Groups: " + visitor.getGroupCount());
        }
        else if (type.equals("message-total")) {
            JOptionPane.showMessageDialog(this,
                "Total Messages: " + visitor.getTweetCount());
        }
        else if (type.equals("positive-percent")) {
            JOptionPane.showMessageDialog(this,
                "Positive Percentage: " + visitor.getPositivePercentage() + "%");
        }
        else {
            JOptionPane.showMessageDialog(this,
                "Unknown stats type: " + type);
        }
    }

    /*
     * Method: getSelectedGroupOrRoot
     * Gets the selected group or the root if no group is selected.
     */
    private UserGroup getSelectedGroupOrRoot() {
        // Get the selected group or the root if no group is selected.
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) return root;

        // Check if the selected node is a group
        Object obj = node.getUserObject();
        if (obj instanceof UserGroup group) {
            return group;
        }
        return root;
    }

    /*
     * Method: findTreeNode
     * Finds the tree node for the given user component.
     */
    private DefaultMutableTreeNode findTreeNode(UserComponent target) {
        // Find the tree node for the given user component.
        return findTreeNodeRecursive(rootNode, target);
        }

    /*
     * Method: findTreeNodeRecursive
     * Recursively finds the tree node for the given user component.
     */
    private DefaultMutableTreeNode findTreeNodeRecursive(DefaultMutableTreeNode current, UserComponent target) {
        // Recursively finds the tree node for the given user component.
        if (current.getUserObject() == target) {
            return current;
        }

        // Check each child node recursively.
        for (int i = 0; i < current.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) current.getChildAt(i);
            DefaultMutableTreeNode result = findTreeNodeRecursive(child, target);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /*
     * Method: find
     * Finds the user component with the given ID.
     */
    public UserComponent find(String id) {
        return registry.get(id);
    }

    /*
     * Method: openUserView
     * Opens the user view for the given user.
     */
    private void openUserView(User user) {
        new UserView(user).setVisible(true);
    }

    /*
     * Method: onVerifyIDs
     * Verifies that all user and group IDs are unique and do not contain spaces.
     */
    private void onVerifyIDs() {
        // Use a set to track seen IDs
        Set<String> seen = new HashSet<>();
        boolean hasDuplicate = false;
        boolean hasSpace = false;

        // Iterate through all user components in the registry
        for (UserComponent comp : registry.values()) {
            String id = comp.getId();

            // Check for spaces
            if (id.contains(" ")) {
                hasSpace = true;
            }

            // Check for duplicates
            if (!seen.add(id)) {
                hasDuplicate = true;
            }
        }

        // Build result message
        if (!hasDuplicate && !hasSpace) {
            JOptionPane.showMessageDialog(this, "All IDs are valid.");
        } else {
            StringBuilder sb = new StringBuilder("Invalid IDs found:\n");
            if (hasDuplicate) sb.append("- Duplicate IDs detected\n");
            if (hasSpace) sb.append("- IDs containing spaces detected\n");
            JOptionPane.showMessageDialog(this, sb.toString());
        }
    }

    /*
     * Method: onShowLastUpdatedUser
     * Finds and displays the last updated user.
     */
    private void onShowLastUpdatedUser() {
        User latestUser = null;
        long latestTime = -1;

        // Loop through all components in the registry
        for (UserComponent comp : registry.values()) {
            if (comp instanceof User user) {
                long t = user.getLastUpdateTime();
                if (t > latestTime) {
                    latestTime = t;
                    latestUser = user;
                }
            }
        }

        // Show result
        if (latestUser == null) {
            JOptionPane.showMessageDialog(this, "No users found.");
        } else {
            JOptionPane.showMessageDialog(this,
                "Last Updated User: " + latestUser.getId());
        }
    }
}