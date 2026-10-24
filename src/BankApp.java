import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BankApp {
    public static void main(String[] args) {
        Theme.applyGlobalDefaults();

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
            frame.getContentPane().setBackground(Theme.BACKGROUND);

            // TEMP: Remove this component demo during Task J-13 cleanup.
            RoundedPanel card = new RoundedPanel(Theme.SURFACE, Theme.RADIUS_CARD, true);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(Theme.SP_24, Theme.SP_24, Theme.SP_24, Theme.SP_24));
            card.setPreferredSize(new Dimension(560, 420));

            ViewHeader header = new ViewHeader("Component Demo", "Visual preview of Task J-05 styled components");
            header.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(header);
            card.add(Box.createVerticalStrut(Theme.SP_16));

            AmountField amountField = new AmountField();
            amountField.setAccessibleName("Amount to deposit");
            amountField.setAlignmentX(Component.LEFT_ALIGNMENT);
            amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, Theme.FIELD_HEIGHT));
            card.add(amountField);
            card.add(Box.createVerticalStrut(Theme.SP_16));

            JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, Theme.SP_16, 0));
            buttonRow.setOpaque(false);
            buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            AppButton depositButton = new AppButton("Deposit", true);
            AppButton clearButton = new AppButton("Clear", false);
            buttonRow.add(depositButton);
            buttonRow.add(clearButton);
            card.add(buttonRow);
            card.add(Box.createVerticalStrut(Theme.SP_16));

            MessageBox errorBox = new MessageBox();
            errorBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            errorBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            errorBox.showError(MoneyUtil.ERROR_INVALID_NUMBER);
            card.add(errorBox);
            card.add(Box.createVerticalStrut(Theme.SP_16));

            MessageBox successBox = new MessageBox();
            successBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            successBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            successBox.showSuccess("Deposit successful. New balance: \u20B16,000.00");
            card.add(successBox);

            // Interactive demo wiring
            clearButton.addActionListener(e -> {
                amountField.setText("");
                amountField.setErrorState(false);
            });
            depositButton.addActionListener(e -> {
                if (amountField.getText().trim().isEmpty()) {
                    amountField.setErrorState(true);
                } else {
                    amountField.setErrorState(false);
                }
            });
            amountField.addEnterListener(e -> depositButton.doClick());
            amountField.addChangeListener(() -> amountField.setErrorState(false));

            JPanel centerWrapper = new JPanel(new GridBagLayout());
            centerWrapper.setOpaque(false);
            centerWrapper.add(card);

            frame.add(centerWrapper, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}
