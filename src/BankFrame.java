import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Main application window for Simple Bank.
 * Manages the fixed sidebar, view switching via CardLayout, keyboard shortcuts (Ctrl+1 to Ctrl+5),
 * focus management, and a themed Exit confirmation dialog.
 */
@SuppressWarnings("serial")
public class BankFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final String VIEW_BALANCE = "BALANCE";
    private static final String VIEW_DEPOSIT = "DEPOSIT";
    private static final String VIEW_WITHDRAW = "WITHDRAW";
    private static final String VIEW_HISTORY = "HISTORY";

    private final BankAccount bankAccount;
    private final NavItem[] navItems;
    private final CardLayout cardLayout;
    private final JPanel contentCards;
    private final BalanceView balanceView;
    private final DepositView depositView;
    private final WithdrawView withdrawView;
    private final HistoryView historyView;
    private final List<Refreshable> refreshableViews;

    @SuppressWarnings("this-escape")
    public BankFrame() {
        super("Simple Bank");
        bankAccount = new BankAccount();
        navItems = new NavItem[] {
                new NavItem(1, "Check Balance"),
                new NavItem(2, "Deposit Money"),
                new NavItem(3, "Withdraw Money"),
                new NavItem(4, "View Transaction History"),
                new NavItem(5, "Exit")
        };
        balanceView = new BalanceView(bankAccount);
        depositView = new DepositView(bankAccount, this::refreshAll);
        withdrawView = new WithdrawView(bankAccount, this::refreshAll);
        historyView = new HistoryView(bankAccount);
        refreshableViews = new ArrayList<>();
        refreshableViews.add(balanceView);
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1000, 680);
        setMinimumSize(new Dimension(860, 560));
        setLocationRelativeTo(null);

        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                confirmExit();
            }
        });

        setupKeyboardShortcuts();
        handleCheckBalance();
    }

    /**
     * Registers Ctrl+1 to Ctrl+5 keyboard shortcuts on the root pane
     * so they activate even when child components (like amount text fields) have focus.
     */
    private void setupKeyboardShortcuts() {
        JRootPane root = getRootPane();
        InputMap inputMap = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = root.getActionMap();

        int menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        registerShortcut(inputMap, actionMap, KeyEvent.VK_1, menuMask, "openCheckBalance", this::handleCheckBalance);
        registerShortcut(inputMap, actionMap, KeyEvent.VK_2, menuMask, "openDeposit", this::handleDeposit);
        registerShortcut(inputMap, actionMap, KeyEvent.VK_3, menuMask, "openWithdraw", this::handleWithdraw);
        registerShortcut(inputMap, actionMap, KeyEvent.VK_4, menuMask, "openHistory", this::handleViewHistory);
        registerShortcut(inputMap, actionMap, KeyEvent.VK_5, menuMask, "openExit", this::confirmExit);

        // Also register explicit CTRL_DOWN_MASK in case menuMask differs (e.g. macOS Cmd)
        if (menuMask != InputEvent.CTRL_DOWN_MASK) {
            registerShortcut(inputMap, actionMap, KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK, "openCheckBalanceCtrl", this::handleCheckBalance);
            registerShortcut(inputMap, actionMap, KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK, "openDepositCtrl", this::handleDeposit);
            registerShortcut(inputMap, actionMap, KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK, "openWithdrawCtrl", this::handleWithdraw);
            registerShortcut(inputMap, actionMap, KeyEvent.VK_4, InputEvent.CTRL_DOWN_MASK, "openHistoryCtrl", this::handleViewHistory);
            registerShortcut(inputMap, actionMap, KeyEvent.VK_5, InputEvent.CTRL_DOWN_MASK, "openExitCtrl", this::confirmExit);
        }

        // Support Numpad 1..5 as well
        registerShortcut(inputMap, actionMap, KeyEvent.VK_NUMPAD1, menuMask, "openCheckBalanceNum", this::handleCheckBalance);
        registerShortcut(inputMap, actionMap, KeyEvent.VK_NUMPAD2, menuMask, "openDepositNum", this::handleDeposit);
        registerShortcut(inputMap, actionMap, KeyEvent.VK_NUMPAD3, menuMask, "openWithdrawNum", this::handleWithdraw);
        registerShortcut(inputMap, actionMap, KeyEvent.VK_NUMPAD4, menuMask, "openHistoryNum", this::handleViewHistory);
        registerShortcut(inputMap, actionMap, KeyEvent.VK_NUMPAD5, menuMask, "openExitNum", this::confirmExit);
    }

    private void registerShortcut(InputMap inputMap, ActionMap actionMap, int keyCode, int modifiers, String key, Runnable action) {
        inputMap.put(KeyStroke.getKeyStroke(keyCode, modifiers), key);
        actionMap.put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    public void showView(String view) {
        int selectedIndex;
        switch (view) {
            case VIEW_BALANCE:
                selectedIndex = 0;
                break;
            case VIEW_DEPOSIT:
                selectedIndex = 1;
                break;
            case VIEW_WITHDRAW:
                selectedIndex = 2;
                break;
            case VIEW_HISTORY:
                selectedIndex = 3;
                break;
            default:
                throw new IllegalArgumentException("Unknown view: " + view);
        }

        cardLayout.show(contentCards, view);
        for (int i = 0; i < 4; i++) {
            navItems[i].setSelected(i == selectedIndex);
        }
    }

    public void handleCheckBalance() {
        showView(VIEW_BALANCE);
        balanceView.refresh();
    }

    public void refreshAll() {
        for (Refreshable view : refreshableViews) {
            view.refresh();
        }
    }

    public void handleDeposit() {
        showView(VIEW_DEPOSIT);
        depositView.refresh();
        depositView.focusAmountField();
    }

    public void handleWithdraw() {
        showView(VIEW_WITHDRAW);
        withdrawView.refresh();
        withdrawView.focusAmountField();
    }

    /** Opens the Transaction History view and refreshes its table from the current account state. */
    public void handleViewHistory() {
        showView(VIEW_HISTORY);
        historyView.refresh();
    }

    /** Package-private test hooks for automated smoke testing. */
    BankAccount getBankAccount() {
        return bankAccount;
    }

    DepositView getDepositView() {
        return depositView;
    }

    WithdrawView getWithdrawView() {
        return withdrawView;
    }

    HistoryView getHistoryView() {
        return historyView;
    }

    BalanceView getBalanceView() {
        return balanceView;
    }

    /**
     * Displays a fully themed modal Exit confirmation dialog with SURFACE background,
     * primary Exit and outlined Stay buttons, Stay focused as default, and Esc returning to app.
     */
    public void confirmExit() {
        JDialog dialog = new JDialog(this, "Exit Simple Bank", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setBackground(Theme.SURFACE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(
                Theme.SP_24, Theme.SP_32, Theme.SP_24, Theme.SP_32));

        JLabel msgLabel = new JLabel("Thank you for banking with us. Are you sure you want to exit?");
        msgLabel.setFont(Theme.fontBody());
        msgLabel.setForeground(Theme.TEXT_PRIMARY);
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(msgLabel);
        content.add(Box.createVerticalStrut(Theme.SP_24));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, Theme.SP_16, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        AppButton exitButton = new AppButton("Exit", true);
        AppButton stayButton = new AppButton("Stay", false);

        exitButton.addActionListener(event -> System.exit(0));
        stayButton.addActionListener(event -> dialog.dispose());

        buttonRow.add(exitButton);
        buttonRow.add(stayButton);
        content.add(buttonRow);

        dialog.setContentPane(content);
        dialog.getRootPane().setDefaultButton(stayButton);

        // Esc key closes the dialog without exiting (returns to app)
        KeyStroke escStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(escStroke, "dismissDialog");
        dialog.getRootPane().getActionMap()
                .put("dismissDialog", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        SwingUtilities.invokeLater(stayButton::requestFocusInWindow);
        dialog.setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Theme.PRIMARY_DARK);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH, 0));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBorder(BorderFactory.createEmptyBorder(Theme.SP_24, Theme.SP_24, Theme.SP_32, Theme.SP_16));
        brand.setAlignmentX(LEFT_ALIGNMENT);
        brand.setMaximumSize(new Dimension(Theme.SIDEBAR_WIDTH, Integer.MAX_VALUE));

        JLabel brandName = new JLabel("Simple Bank");
        brandName.setFont(Theme.fontBrand());
        brandName.setForeground(Theme.ON_PRIMARY);
        JLabel caption = new JLabel("Personal Banking");
        caption.setFont(Theme.fontSubtitle().deriveFont(Font.PLAIN, 12f));
        caption.setForeground(Theme.NAV_TEXT);
        brand.add(brandName);
        brand.add(Box.createVerticalStrut(Theme.SP_4));
        brand.add(caption);
        sidebar.add(brand);

        navItems[0].addActionListener(event -> handleCheckBalance());
        navItems[1].addActionListener(event -> handleDeposit());
        navItems[2].addActionListener(event -> handleWithdraw());
        navItems[3].addActionListener(event -> handleViewHistory());
        navItems[4].addActionListener(event -> confirmExit());
        for (int i = 0; i < 4; i++) {
            sidebar.add(navItems[i]);
        }

        sidebar.add(Box.createVerticalGlue());
        JPanel divider = new JPanel();
        divider.setOpaque(true);
        divider.setBackground(Theme.BORDER);
        divider.setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH, 1));
        divider.setMinimumSize(new Dimension(Theme.SIDEBAR_WIDTH, 1));
        divider.setMaximumSize(new Dimension(Theme.SIDEBAR_WIDTH, 1));
        divider.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(divider);
        sidebar.add(Box.createVerticalStrut(Theme.SP_8));
        sidebar.add(navItems[4]);
        sidebar.add(Box.createVerticalStrut(Theme.SP_16));
        return sidebar;
    }

    private JPanel buildContent() {
        contentCards.setBackground(Theme.BACKGROUND);
        contentCards.setBorder(BorderFactory.createEmptyBorder(
                Theme.SP_32, Theme.SP_32, Theme.SP_32, Theme.SP_32));

        contentCards.add(balanceView, VIEW_BALANCE);
        contentCards.add(depositView, VIEW_DEPOSIT);
        contentCards.add(withdrawView, VIEW_WITHDRAW);
        contentCards.add(historyView, VIEW_HISTORY);
        return contentCards;
    }

    private JPanel createPlaceholder(String title, String subtitle) {
        JPanel view = new JPanel(new BorderLayout());
        view.setBackground(Theme.BACKGROUND);
        view.add(new ViewHeader(title, subtitle), BorderLayout.NORTH);
        return view;
    }
}
