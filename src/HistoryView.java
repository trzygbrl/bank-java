import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * View 4 — Transaction History.
 * Displays all successful deposits and withdrawals in a themed table,
 * or a centered "No transactions yet." message when the list is empty.
 * Implements Refreshable so BankFrame can call refresh() each time the view is opened.
 */
@SuppressWarnings("serial")
public class HistoryView extends JPanel implements Refreshable {

    // Card names used by the inner CardLayout to switch between states
    private static final String CARD_TABLE = "TABLE";
    private static final String CARD_EMPTY = "EMPTY";

    // Column indices matching the DefaultTableModel columns
    private static final int COL_NO     = 0;
    private static final int COL_TYPE   = 1;
    private static final int COL_AMOUNT = 2;
    private static final int COL_BAL    = 3;

    private final BankAccount account;

    // Caption label updated to "{n} transaction(s)"
    private final JLabel captionLabel;

    // Table model: non-editable, rebuilt on every refresh()
    private final DefaultTableModel tableModel;

    // Table that displays transaction rows
    private final JTable table;

    // CardLayout controlling empty vs. table state inside the card
    private final CardLayout innerCardLayout;

    // Panel that holds the inner card layout
    private final JPanel innerCards;

    /**
     * Constructs the history view for the given account.
     * Layout: ViewHeader → RoundedPanel card (caption + CardLayout[TABLE | EMPTY]).
     */
    @SuppressWarnings("this-escape")
    public HistoryView(BankAccount account) {
        this.account = Objects.requireNonNull(account, "account");
        setOpaque(false);
        setLayout(new BorderLayout(0, Theme.SP_24));

        // ── Header ──────────────────────────────────────────────────────────
        ViewHeader header = new ViewHeader(
                "Transaction History",
                "All successful deposits and withdrawals");
        add(header, BorderLayout.NORTH);

        // ── Outer card panel (fills available width and height) ─────────────
        RoundedPanel card = new RoundedPanel(Theme.SURFACE, Theme.RADIUS_CARD, true);
        card.setLayout(new BorderLayout(0, Theme.SP_16));
        card.setBorder(BorderFactory.createEmptyBorder(
                Theme.SP_24, Theme.SP_24, Theme.SP_24, Theme.SP_24));

        // Caption: "{n} transaction(s)"
        captionLabel = new JLabel("0 transaction(s)");
        captionLabel.setFont(Theme.fontBody());
        captionLabel.setForeground(Theme.TEXT_MUTED);
        card.add(captionLabel, BorderLayout.NORTH);

        // ── Table model ─────────────────────────────────────────────────────
        tableModel = new DefaultTableModel(
                new Object[]{"No.", "Type", "Amount", "Balance After"}, 0) {
            /** All cells are read-only. */
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        // ── JTable ──────────────────────────────────────────────────────────
        table = new JTable(tableModel);
        table.setFont(Theme.fontBody());
        table.setRowHeight(Theme.TABLE_ROW_HEIGHT);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setReorderingAllowed(false);
        table.getAccessibleContext().setAccessibleName("Transaction History");

        // Disable default Metal blue selection; use LAVENDER instead
        table.setSelectionBackground(Theme.LAVENDER);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);

        // One-pixel BORDER line under each row via row-lines trick
        table.setShowHorizontalLines(true);
        table.setGridColor(Theme.BORDER);

        // Apply custom header renderer to all columns
        applyHeaderRenderer();

        // Apply custom body renderer to all columns
        table.setDefaultRenderer(Object.class, new BodyRenderer());

        // Column-specific alignment overrides are handled inside BodyRenderer

        // ── JScrollPane ─────────────────────────────────────────────────────
        JScrollPane scrollPane = buildScrollPane();

        // ── Empty state panel ────────────────────────────────────────────────
        JPanel emptyPanel = buildEmptyPanel();

        // ── Inner CardLayout ─────────────────────────────────────────────────
        innerCardLayout = new CardLayout();
        innerCards = new JPanel(innerCardLayout);
        innerCards.setOpaque(false);
        innerCards.add(scrollPane, CARD_TABLE);
        innerCards.add(emptyPanel, CARD_EMPTY);

        card.add(innerCards, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);

        // Populate on construction so an early peek at the panel looks correct
        refresh();
    }

    // ── Private builders ───────────────────────────────────────────────────

    /**
     * Builds the JScrollPane that wraps the table.
     * No border, SURFACE viewport, themed scrollbars, header always visible.
     */
    private JScrollPane buildScrollPane() {
        JScrollPane scroll = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.SURFACE);
        scroll.setBackground(Theme.SURFACE);
        // Ensure table fills the full viewport height (no gap below last row)
        table.setFillsViewportHeight(true);

        // Theme the scrollbar to remove default Metal colors
        scroll.getVerticalScrollBar().setBackground(Theme.BACKGROUND);
        scroll.getVerticalScrollBar().setForeground(Theme.LAVENDER);
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor    = Theme.LAVENDER;
                trackColor    = Theme.BACKGROUND;
                thumbHighlightColor = Theme.LAVENDER_MIST;
                thumbLightShadowColor = Theme.BORDER;
                thumbDarkShadowColor  = Theme.BORDER;
            }
            @Override
            protected javax.swing.JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            protected javax.swing.JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            /** Returns a zero-size button so no arrow buttons are rendered. */
            private javax.swing.JButton createZeroButton() {
                javax.swing.JButton btn = new javax.swing.JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                btn.setMinimumSize(new Dimension(0, 0));
                btn.setMaximumSize(new Dimension(0, 0));
                return btn;
            }
        });

        // Preferred size: fill available height; minimum meaningful height
        scroll.setPreferredSize(new Dimension(0, 400));
        return scroll;
    }

    /**
     * Builds the empty-state panel shown when there are no transactions.
     * Centered "No transactions yet." text in TEXT_MUTED — no table, no header row.
     */
    private JPanel buildEmptyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel label = new JLabel("No transactions yet.", SwingConstants.CENTER);
        label.setFont(Theme.fontBody());
        label.setForeground(Theme.TEXT_MUTED);
        panel.add(label, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(0, 120));
        return panel;
    }

    /**
     * Installs a custom header renderer on every table column.
     * LAVENDER background, bold PRIMARY_DEEP text, height 40, alignment matches column.
     */
    private void applyHeaderRenderer() {
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.LAVENDER);
        header.setForeground(Theme.PRIMARY_DEEP);
        header.setFont(Theme.fontBodyBold());
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, Theme.TABLE_ROW_HEIGHT));
        header.setReorderingAllowed(false);

        // One renderer shared across columns — alignment is per-column
        TableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tbl, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        tbl, value, isSelected, hasFocus, row, col);
                lbl.setBackground(Theme.LAVENDER);
                lbl.setForeground(Theme.PRIMARY_DEEP);
                lbl.setFont(Theme.fontBodyBold());
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                        BorderFactory.createEmptyBorder(0, 12, 0, 12)));

                // Alignment: No. and Type → left; Amount and Balance After → right
                int modelCol = tbl.convertColumnIndexToModel(col);
                if (modelCol == COL_AMOUNT || modelCol == COL_BAL) {
                    lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    lbl.setHorizontalAlignment(SwingConstants.LEFT);
                }
                return lbl;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }

    // ── Refreshable ────────────────────────────────────────────────────────

    /**
     * Clears the table model and repopulates it from account.getTransactions().
     * Updates the caption and switches between TABLE and EMPTY cards.
     * Called by BankFrame.handleViewHistory() each time the view is opened.
     */
    @Override
    public void refresh() {
        // Remove all existing rows without modifying the account or the list
        tableModel.setRowCount(0);

        List<Transaction> txList = account.getTransactions();
        int n = txList.size();

        // Explicit for-loop over the unmodifiable list (PRD 11: loop requirement)
        for (int i = 0; i < n; i++) {
            Transaction tx = txList.get(i);
            tableModel.addRow(new Object[]{
                i + 1,
                tx.type().getLabel(),
                MoneyUtil.formatMoney(tx.amountCentavos()),
                MoneyUtil.formatMoney(tx.balanceAfterCentavos())
            });
        }

        // Update caption text
        captionLabel.setText(n + " transaction(s)");

        // Switch card: show EMPTY when no transactions, TABLE otherwise
        if (n == 0) {
            innerCardLayout.show(innerCards, CARD_EMPTY);
        } else {
            innerCardLayout.show(innerCards, CARD_TABLE);
        }
    }

    /** Package-private test hooks for automated smoke testing. */
    int getTableRowCount() {
        return tableModel.getRowCount();
    }

    Object getCellValue(int row, int col) {
        return tableModel.getValueAt(row, col);
    }

    String getCaptionText() {
        return captionLabel.getText();
    }

    // ── Inner renderer ─────────────────────────────────────────────────────

    /**
     * Custom cell renderer for table body rows.
     * Alternates SURFACE and LAVENDER_MIST backgrounds.
     * No. and Type are left-aligned; Amount and Balance After are right-aligned.
     * Type text is colored SUCCESS for "Deposit" and WITHDRAWAL for "Withdrawal".
     * All cells have 12 px horizontal padding.
     */
    private static final class BodyRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable tbl, Object value, boolean isSelected,
                boolean hasFocus, int row, int col) {

            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    tbl, value, isSelected, hasFocus, row, col);

            // Font: body font from Theme
            lbl.setFont(Theme.fontBody());

            // Row background: alternate SURFACE / LAVENDER_MIST; LAVENDER when selected
            Color bg = isSelected
                    ? Theme.LAVENDER
                    : (row % 2 == 0 ? Theme.SURFACE : Theme.LAVENDER_MIST);
            lbl.setBackground(bg);
            lbl.setOpaque(true);

            // No., Amount, and Balance After always use TEXT_PRIMARY.
            // Type uses SUCCESS for "Deposit" and WITHDRAWAL for "Withdrawal".
            // TEXT_PRIMARY is set first so DefaultTableCellRenderer's internal
            // unselectedForeground cache is reset before any conditional override.
            int modelCol = tbl.convertColumnIndexToModel(col);
            lbl.setForeground(Theme.TEXT_PRIMARY);
            if (modelCol == COL_TYPE && value != null) {
                String typeText = value.toString();
                if ("Deposit".equals(typeText)) {
                    lbl.setForeground(Theme.SUCCESS);
                } else if ("Withdrawal".equals(typeText)) {
                    lbl.setForeground(Theme.WITHDRAWAL);
                }
            }

            // Alignment: Amount and Balance After → right; others → left
            if (modelCol == COL_AMOUNT || modelCol == COL_BAL) {
                lbl.setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
            }

            // 12 px horizontal padding + 1 px BORDER line at the bottom of each row
            lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                    BorderFactory.createEmptyBorder(0, 12, 0, 12)));

            return lbl;
        }
    }
}
