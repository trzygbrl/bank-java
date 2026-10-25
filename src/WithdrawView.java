import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class WithdrawView extends JPanel implements Refreshable {
    private final BankAccount account;
    private final Runnable onBalanceChanged;
    private final JLabel availableBalanceLabel;
    private final AmountField amountField;
    private final MessageBox messageBox;

    @SuppressWarnings("this-escape")
    public WithdrawView(BankAccount account, Runnable onBalanceChanged) {
        this.account = Objects.requireNonNull(account, "account");
        this.onBalanceChanged = Objects.requireNonNull(onBalanceChanged, "onBalanceChanged");
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ViewHeader header = new ViewHeader("Withdraw Money", "Take funds from your account");
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(header);
        add(Box.createVerticalStrut(Theme.SP_24));

        RoundedPanel card = new RoundedPanel(Theme.SURFACE, Theme.RADIUS_CARD, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(
                Theme.SP_24, Theme.SP_24, Theme.SP_24, Theme.SP_24));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Withdrawal details");
        title.setFont(Theme.fontCardTitle());
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(Theme.SP_16));

        RoundedPanel balanceChip = new RoundedPanel(Theme.LAVENDER_MIST, Theme.RADIUS_CONTROL, false);
        balanceChip.setLayout(new FlowLayout(FlowLayout.LEFT, Theme.SP_16, Theme.SP_8));
        availableBalanceLabel = new JLabel();
        availableBalanceLabel.setFont(Theme.fontBody());
        availableBalanceLabel.setForeground(Theme.TEXT_PRIMARY);
        balanceChip.add(availableBalanceLabel);
        balanceChip.setAlignmentX(Component.LEFT_ALIGNMENT);
        balanceChip.setMaximumSize(new Dimension(Integer.MAX_VALUE, balanceChip.getPreferredSize().height));
        card.add(balanceChip);
        card.add(Box.createVerticalStrut(Theme.SP_16));

        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setFont(Theme.fontBody());
        amountLabel.setForeground(Theme.TEXT_PRIMARY);
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(amountLabel);
        card.add(Box.createVerticalStrut(Theme.SP_8));

        amountField = new AmountField();
        amountField.setAccessibleName("Amount to withdraw");
        amountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, Theme.FIELD_HEIGHT));
        card.add(amountField);
        card.add(Box.createVerticalStrut(Theme.SP_8));

        messageBox = new MessageBox();
        messageBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        card.add(messageBox);
        card.add(Box.createVerticalStrut(Theme.SP_16));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, Theme.SP_16, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        AppButton withdrawButton = new AppButton("Withdraw", true);
        AppButton clearButton = new AppButton("Clear", false);
        buttonRow.add(withdrawButton);
        buttonRow.add(clearButton);
        card.add(buttonRow);

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
        add(card);
        add(Box.createVerticalGlue());

        withdrawButton.addActionListener(event -> handleWithdraw());
        clearButton.addActionListener(event -> clearForm());
        amountField.addEnterListener(event -> handleWithdraw());
        amountField.addChangeListener(() -> {
            messageBox.clear();
            amountField.setErrorState(false);
        });
        refresh();
    }

    private void handleWithdraw() {
        AmountResult result = MoneyUtil.validateAmount(amountField.getText());
        if (!result.isValid()) {
            messageBox.showError(result.errorMessage());
            amountField.setErrorState(true);
            amountField.selectAllAndFocus();
            return;
        }

        if (!account.withdraw(result.centavos())) {
            String errorMessage = "Insufficient funds. Your balance is "
                    + MoneyUtil.formatMoney(account.getBalance())
                    + "; you cannot withdraw "
                    + MoneyUtil.formatMoney(result.centavos()) + ".";
            messageBox.showError(errorMessage);
            amountField.setErrorState(true);
            return;
        }

        amountField.setText("");
        amountField.setErrorState(false);
        updateAvailableBalance();
        messageBox.showSuccess("Withdrawal successful. New balance: "
                + MoneyUtil.formatMoney(account.getBalance()));
        onBalanceChanged.run();
    }

    private void clearForm() {
        amountField.setText("");
        messageBox.clear();
        amountField.setErrorState(false);
        amountField.selectAllAndFocus();
    }

    @Override
    public void refresh() {
        updateAvailableBalance();
        messageBox.clear();
    }

    private void updateAvailableBalance() {
        availableBalanceLabel.setText("Available balance "
                + MoneyUtil.formatMoney(account.getBalance()));
    }
}
