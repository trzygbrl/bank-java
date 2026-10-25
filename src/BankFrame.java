import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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

        showView(VIEW_BALANCE);
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
    }

    public void handleDeposit() {
        showView(VIEW_DEPOSIT);
    }

    public void handleWithdraw() {
        showView(VIEW_WITHDRAW);
    }

    public void handleViewHistory() {
        showView(VIEW_HISTORY);
    }

    public void confirmExit() {
        Object[] options = {"Exit", "Stay"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Thank you for banking with us. Are you sure you want to exit?",
                "Exit Simple Bank",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        if (choice == 0) {
            System.exit(0);
        }
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
        divider.setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH - Theme.SP_32, 1));
        divider.setMaximumSize(new Dimension(Theme.SIDEBAR_WIDTH - Theme.SP_32, 1));
        divider.setAlignmentX(CENTER_ALIGNMENT);
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

        contentCards.add(createPlaceholder("Check Balance", "Your account at a glance"), VIEW_BALANCE);
        contentCards.add(createPlaceholder("Deposit Money", "Add funds to your account"), VIEW_DEPOSIT);
        contentCards.add(createPlaceholder("Withdraw Money", "Take funds from your account"), VIEW_WITHDRAW);
        contentCards.add(createPlaceholder(
                "Transaction History", "All successful deposits and withdrawals"), VIEW_HISTORY);
        return contentCards;
    }

    private JPanel createPlaceholder(String title, String subtitle) {
        JPanel view = new JPanel(new BorderLayout());
        view.setBackground(Theme.BACKGROUND);
        view.add(new ViewHeader(title, subtitle), BorderLayout.NORTH);
        return view;
    }
}
