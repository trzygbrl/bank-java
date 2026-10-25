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
 * View 2 — Deposit Money.
 * Provides the amount input field, real-time balance chip, inline feedback,
 * and Deposit/Clear actions, with a 720px max card width and keyboard navigation.
 */
@SuppressWarnings("serial")
public class DepositView extends JPanel implements Refreshable {
    private final BankAccount account;
    private final Runnable onBalanceChanged;
    private final JLabel availableBalanceLabel;
    private final AmountField amountField;
    private final MessageBox messageBox;

    /**
     * Constructs the Deposit Money view.
     *
     * @param account the bank account to perform deposits against
     * @param onBalanceChanged callback invoked when a successful deposit modifies account balance
     */
    @SuppressWarnings("this-escape")
    public DepositView(BankAccount account, Runnable onBalanceChanged) {
        this.account = Objects.requireNonNull(account, "account");
        this.onBalanceChanged = Objects.requireNonNull(onBalanceChanged, "onBalanceChanged");
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ViewHeader header = new ViewHeader("Deposit Money", "Add funds to your account");
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

        JLabel title = new JLabel("Deposit details");
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
        amountField.setAccessibleName("Amount to deposit");
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
        AppButton depositButton = new AppButton("Deposit", true);
        AppButton clearButton = new AppButton("Clear", false);
        depositButton.getAccessibleContext().setAccessibleName("Deposit");
        clearButton.getAccessibleContext().setAccessibleName("Clear");
        buttonRow.add(depositButton);
        buttonRow.add(Box.createHorizontalStrut(Theme.SP_16));
        buttonRow.add(clearButton);
        card.add(buttonRow);

        add(card);
        add(Box.createVerticalGlue());

        depositButton.addActionListener(event -> handleDeposit());
        clearButton.addActionListener(event -> clearForm());
        amountField.addEnterListener(event -> handleDeposit());
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

    /**
     * Puts keyboard focus in the amount field and selects any existing text.
     */
    public void focusAmountField() {
        SwingUtilities.invokeLater(amountField::selectAllAndFocus);
    }

    /**
     * Validates input amount, credits the bank account on success, updates UI feedback, and alerts listeners.
     */
    private void handleDeposit() {
        AmountResult result = MoneyUtil.validateAmount(amountField.getText());
        if (!result.isValid()) {
            messageBox.showError(result.getErrorMessage());
            amountField.setErrorState(true);
            amountField.selectAllAndFocus();
            return;
        }

        account.deposit(result.getCentavos());
        amountField.setText("");
        amountField.setErrorState(false);
        updateAvailableBalance();
        messageBox.showSuccess("Deposit successful. New balance: "
                + MoneyUtil.formatMoney(account.getBalance()));
        onBalanceChanged.run();
    }

    /**
     * Clears the input field and feedback messages, resetting the error state and requesting focus.
     */
    private void clearForm() {
        amountField.setText("");
        messageBox.clear();
        amountField.setErrorState(false);
        amountField.selectAllAndFocus();
    }

    /**
     * Refreshes the available balance chip and clears old feedback messages.
     */
    @Override
    public void refresh() {
        updateAvailableBalance();
        messageBox.clear();
    }

    /**
     * Updates the available balance chip text with the current account balance.
     */
    private void updateAvailableBalance() {
        availableBalanceLabel.setText("Available balance "
                + MoneyUtil.formatMoney(account.getBalance()));
    }

    /**
     * Test helper to set the amount field text.
     *
     * @param text the text to set
     */
    void setAmountText(String text) {
        amountField.setText(text);
    }

    /**
     * Test helper to retrieve the current amount field text.
     *
     * @return the text currently in the amount field
     */
    String getAmountText() {
        return amountField.getText();
    }

    /**
     * Test helper to programmatically trigger the deposit action.
     */
    void submitDeposit() {
        handleDeposit();
    }

    /**
     * Test helper to retrieve the feedback message text.
     *
     * @return the current message text
     */
    String getMessageText() {
        return messageBox.getMessageText();
    }

    /**
     * Test helper to check if the amount field is in an error state.
     *
     * @return true if in error state, false otherwise
     */
    boolean isErrorState() {
        return amountField.isErrorState();
    }
}
