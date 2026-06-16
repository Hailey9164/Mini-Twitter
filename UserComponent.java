/* Hailey Campbell 
 * CSC 3560
 * 6/15/2026
 * Abstract class: UserComponent
 * This abstract class is a abstract class for User (leaf) and UserGroup (composite). It uses 
 * the composite design pattern to allow for a hierarchical structure of users and 
 * user groups.
 */

public abstract class UserComponent {
    // Every user or group has an unique ID
    protected String id;

    /* Method: userComponent
     * This method is used to initialize the unique ID of the user or group.
     * Constructor to initialize the unique ID
     */

    public UserComponent(String id) {
        this.id = id;
    }   

    /* Method: getId
     * This method is used to get the unique ID of the user or group.
     */
    public String getId() {
        return id;
    }

    /* Method: add
     * This method is used to add a component to the user or group.
     * Composite method: override to add a component in UserGroup
     */
    public void add(UserComponent component) {
        throw new UnsupportedOperationException("Adding to a leaf is not supported");
    }

    /* Method: getChild
     * This method is used to get a child component from the user or group.
     * Composite method: override to get a child component in UserGroup
     */
    public UserComponent getChild(int index) {
        throw new UnsupportedOperationException("Leaf has no children");
    }

    /* Method: getChildren
     * This method is used to get the list of child components from the user or group.
     * Composite method: override to get the list of child components in UserGroup
     */
    public java.util.List<UserComponent> getChildren() {
        throw new UnsupportedOperationException("Leaf has no children");
    }

    /* Method: accept
     * This method is used to accept a visitor for the visitor pattern.
     */
    public abstract void accept(Visitor visitor);
}