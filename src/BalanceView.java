import java.awt.Component;
import java.awt.Dimension;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BalanceView extends JPanel implements Refreshable {
    private final BankAccount account;
    private final JLabel balanceLabel;
    private final JLabel captionLabel;
    private boolean firstDisplay = true;

    @SuppressWarnings("this-escape")
    public BalanceView(BankAccount account) {
        this.account = Objects.requireNonNull(account, "account");
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ViewHeader header = new ViewHeader("Check Balance", "Your account at a glance");
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(header);
        add(Box.createVerticalStrut(Theme.SP_24));

        RoundedPanel balanceCard = new RoundedPanel(Theme.PRIMARY, Theme.RADIUS_CARD, false);
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setBorder(BorderFactory.createEmptyBorder(
                Theme.SP_32, Theme.SP_32, Theme.SP_32, Theme.SP_32));
        balanceCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        balanceCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel balanceTitle = new JLabel("Current balance");
        balanceTitle.setFont(Theme.fontBody());
        balanceTitle.setForeground(Theme.NAV_TEXT);
        balanceTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        balanceLabel = new JLabel();
        balanceLabel.setFont(Theme.fontBalance());
        balanceLabel.setForeground(Theme.ON_PRIMARY);
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        balanceCard.add(balanceTitle);
        balanceCard.add(Box.createVerticalStrut(Theme.SP_8));
        balanceCard.add(balanceLabel);
        balanceCard.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, balanceCard.getPreferredSize().height));
        add(balanceCard);
        add(Box.createVerticalStrut(Theme.SP_24));

        captionLabel = new JLabel();
        captionLabel.setFont(Theme.fontBody());
        captionLabel.setForeground(Theme.TEXT_MUTED);
        captionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(captionLabel);
        add(Box.createVerticalGlue());
    }

    @Override
    public void refresh() {
        String formattedBalance = MoneyUtil.formatMoney(account.getBalance());
        balanceLabel.setText(formattedBalance);
        if (firstDisplay) {
            captionLabel.setText("Account initialized. Current balance: " + formattedBalance);
            firstDisplay = false;
        } else {
            captionLabel.setText("Current balance: " + formattedBalance);
        }
    }
}
