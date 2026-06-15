/* Hailey Campbell 
 * CSC 3560
 * 6/15/2026
 * Class: UserGroup
 * This class is a composite in the composite design pattern, representing 
 * a group of users.
 */

import java.util.*;

public class UserGroup extends UserComponent {
    // List of child components
    private final List<UserComponent> children = new ArrayList<>();

    /* Method: UserGroup
     * This method is used to initialize the user group with an ID using the parent class constructor.
     */
    public UserGroup(String id) {
        super(id);
    }

    /* Method: add
     * This method is used to add a child component to the user group.
     */
    @Override
    public void add(UserComponent component) {
        children.add(component);
    }

    /* Method: remove
     * This method is used to remove a child component from the user group.
     */
    @Override
    public List<UserComponent> getChildren() {
        return children;
    }

    /* Method: accept
     * This method is used to accept a visitor and visit the user group and its children.
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visitUserGroup(this);
        for (UserComponent child : children) {
            child.accept(visitor);
        }
    }
}