import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class BankApp {
    public static void main(String[] args) {
        // TEMP: Remove this logic check during Task J-13 cleanup.
        BankAccount account = new BankAccount();

        account.deposit(100_000L);
        account.withdraw(200_000L);

        System.out.println("TEMP balance: " + account.getBalance());
        System.out.println("TEMP transactions: " + account.getTransactions());

        boolean rejected = !account.withdraw(99_999_999L);
        System.out.println("TEMP overdraft rejected: " + rejected);
        System.out.println("TEMP balance after overdraft: " + account.getBalance());
        System.out.println("TEMP transactions after overdraft: " + account.getTransactions());

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Simple Bank");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 680);
            frame.setLocationRelativeTo(null);

            JLabel label = new JLabel("Setup OK", SwingConstants.CENTER);
            frame.add(label);

            frame.setVisible(true);
        });
    }
}
