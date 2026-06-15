/* Hailey Campbell 
 * CSC 3560
 * 6/15/2026
 * Class: Driver
 * This class is the main entry point for the application.
 * It initializes the user and displays the user view.
 */

public class Driver {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            AdminControlPanel.instance().setVisible(true);
        });
    }
}