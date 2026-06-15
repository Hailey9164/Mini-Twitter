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
 */

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AdminControlPanel extends JFrame {

    /** The singleton instance of the AdminControlPanel */
    private static AdminControlPanel pointer;

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

    private AdminControlPanel() {
        super("Admin Control Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLocationRelativeTo(null);

        registry.put(root.getId(), root);

        buildUI();
    }

    /** The root user group of the system */
    private final UserGroup root = new UserGroup("Root");

    private final Map<String, UserComponent> registry = new HashMap<>();

    private JTree tree; 
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootNode;

    private JTextField userIdField;
    private JTextField groupIdField;

    private JButton addUserButton;
    private JButton addGroupButton;
    private JButton openUserViewButton;
    private JButton showUserTotalButton;
    private JButton showGroupTotalButton;
    private JButton showMessagesTotalButton;
    private JButton showPositivePercentageButton;

    public void addUser(User user) {
        registry.put(user.getId(), user);
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Tree //
        rootNode = new DefaultMutableTreeNode(root);
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
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

        // Stats Buttons
        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        showUserTotalButton = new JButton("Show User Total");
        showGroupTotalButton = new JButton("Show Group Total");
        showMessagesTotalButton = new JButton("Show Messages Total");
        showPositivePercentageButton = new JButton("Show Positive Percentage");
        statsPanel.add(showUserTotalButton);
        statsPanel.add(showGroupTotalButton);
        statsPanel.add(showMessagesTotalButton);
        statsPanel.add(showPositivePercentageButton);
    
        JPanel statsWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsWrapper.add(statsPanel);
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(statsWrapper);

        // Listeners
        hookListeners();
    }

    private void hookListeners() {
        addUserButton.addActionListener(e -> onAddUser());
        addGroupButton.addActionListener(e -> onAddGroup());
        openUserViewButton.addActionListener(e -> onOpenUserView());

        showUserTotalButton.addActionListener(e -> onShowStats("users"));
        showGroupTotalButton.addActionListener(e -> onShowStats("groups"));
        showMessagesTotalButton.addActionListener(e -> onShowStats("messages"));
        showPositivePercentageButton.addActionListener(e -> onShowStats("positive"));
    }

    private void onAddUser() {
        String id = userIdField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID cannot be empty.");
            return;
        }
        if (registry.containsKey(id)){
            JOptionPane.showMessageDialog(this, "User ID already exists.");
            return;
        }

        UserGroup parentGroup = setSelectedGroupOrRoot();
        User user = new User(id);
        parentGroup.add(user);
        registry.put(id, user);

        // add to tree
        DefaultMutableTreeNode parentNode = findTreeNode(parentGroup.getId());
        if ( parentNode == null) {
            parentNode = rootNode;
        }
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(id);
        parentNode.add(userNode);
        treeModel.reload(parentNode);

        userIdField.setText("");
    }

    private void onAddGroup() {
        String id = groupIdField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Group ID cannot be empty.");
            return;
        }
        if (registry.containsKey(id)){
            JOptionPane.showMessageDialog(this, "Group ID already exists.");
            return;
        }

        UserGroup parentGroup = getSelectedGroupOrRoot();
        UserGroup group = new UserGroup(id);
        parentGroup.add(group);
        registry.put(id, group);

        UserGroup parentGroup = getSelectedGroupOrRoot();
        UserGroup group = new UserGroup(id);
        parentGroup.add(group);
        registry.put(id, group);

        // add to tree
        DefaultMutableTreeNode parentNode = findTreeNode(parentGroup.getId());
        if ( parentNode == null) {
            parentNode = rootNode;
        }
        DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(id);
        parentNode.add(groupNode);
        treeModel.reload(parentNode);

        groupIdField.setText("");
    }

    private void onOpenUserView() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return root;
        }

        String id = node.getUserObject().toString();
        UserComponent comp = registry.get(id);
        if (comp instanceof UserGroup group) {
            return group;
        }
        return root;
    }

    private DefaultMutableTreeNode findTreeNode(String id) {
        return findTreeNodRecursive(rootNode, id);
    }

    private DefaultMutableTreeNode findTreeNodRecursive(DefaultMutableTreeNode currrent, String id) {
        if current.getUserObject().toString().equals(id) {
            return current;
        }
        for (int i = 0; i < current.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) current.getChildAt(i);
            DefaultMutableTreeNode result = findTreeNodRecursive(child, id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public UserComponent find(String id) {
        return registry.get(id);
    }

    private void openUserView(User user) {
        new UserView(user).setVisible(true);
    }
}