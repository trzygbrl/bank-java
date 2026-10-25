import javax.swing.SwingUtilities;

public class BankApp {
    public static void main(String[] args) {
        Theme.applyGlobalDefaults();
        SwingUtilities.invokeLater(() -> new BankFrame().setVisible(true));
    }
}
