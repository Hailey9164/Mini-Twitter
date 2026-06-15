/* Hailey Campbell 
 * CSC 3560
 * 6/15/2026
 * Class: Visitor
 * This class is a visitor in the visitor design pattern, representing an object that can visit other objects.
 */

public interface Visitor {
    /* Method: visitUser
     * This method is called when the visitor visits a user.
     */
    void visitUser(User user);

    /* Method: visitUserGroup
     * This method is called when the visitor visits a user group.
     */
    void visitUserGroup(UserGroup group);
}