import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * View 3 — Withdraw Money.
 * Provides the amount input field, real-time balance chip, inline feedback,
 * and Withdraw/Clear actions, with a 720px max card width and keyboard navigation.
 */
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

        // Card with maximum width of 720px, left-aligned on wide windows (UI-5, TC-25)
        RoundedPanel card = new RoundedPanel(Theme.SURFACE, Theme.RADIUS_CARD, true) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Theme.MAX_CARD_WIDTH, getPreferredSize().height);
            }
        };
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

        RoundedPanel balanceChip = new RoundedPanel(Theme.LAVENDER_MIST, Theme.RADIUS_CONTROL, false) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(d.width, Math.max(36, d.height));
            }

            @Override
            public Dimension getMinimumSize() {
                Dimension d = super.getMinimumSize();
                return new Dimension(d.width, Math.max(36, d.height));
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        balanceChip.setLayout(new FlowLayout(FlowLayout.LEFT, Theme.SP_16, Theme.SP_8));
        availableBalanceLabel = new JLabel();
        availableBalanceLabel.setFont(Theme.fontBody());
        availableBalanceLabel.setForeground(Theme.TEXT_PRIMARY);
        updateAvailableBalance();
        balanceChip.add(availableBalanceLabel);
        balanceChip.setAlignmentX(Component.LEFT_ALIGNMENT);
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

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, Theme.BUTTON_HEIGHT));
        AppButton withdrawButton = new AppButton("Withdraw", true);
        AppButton clearButton = new AppButton("Clear", false);
        withdrawButton.getAccessibleContext().setAccessibleName("Withdraw");
        clearButton.getAccessibleContext().setAccessibleName("Clear");
        buttonRow.add(withdrawButton);
        buttonRow.add(Box.createHorizontalStrut(Theme.SP_16));
        buttonRow.add(clearButton);
        card.add(buttonRow);

        add(card);
        add(Box.createVerticalGlue());

        withdrawButton.addActionListener(event -> handleWithdraw());
        clearButton.addActionListener(event -> clearForm());
        amountField.addEnterListener(event -> handleWithdraw());
        amountField.addChangeListener(() -> {
            messageBox.clear();
            amountField.setErrorState(false);
        });

        // Automatically focus amount field when view is opened / becomes visible
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                focusAmountField();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });

        refresh();
    }

    /** Puts keyboard focus in the amount field and selects any existing text. */
    public void focusAmountField() {
        SwingUtilities.invokeLater(amountField::selectAllAndFocus);
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
            amountField.selectAllAndFocus();
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

    /** Package-private test hooks for automated smoke testing. */
    void setAmountText(String text) {
        amountField.setText(text);
    }

    String getAmountText() {
        return amountField.getText();
    }

    void submitWithdraw() {
        handleWithdraw();
    }

    String getMessageText() {
        return messageBox.getMessageText();
    }

    boolean isErrorState() {
        return amountField.isErrorState();
    }
}
