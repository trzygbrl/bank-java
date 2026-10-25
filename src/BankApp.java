/*
 * Simple Bank
 * Group: <group name>
 *
 * Main entry point for the Simple Bank Swing desktop banking application.
 */
import javax.swing.SwingUtilities;

/**
 * Application launcher class for Simple Bank.
 */
public class BankApp {

    /**
     * Main method that configures global theme styling and opens the main window on the EDT.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Theme.applyGlobalDefaults();
        SwingUtilities.invokeLater(() -> new BankFrame().setVisible(true));
    }
}
