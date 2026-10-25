import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class NavItem extends JButton {
    private static final long serialVersionUID = 1L;

    private final int number;

    @SuppressWarnings("this-escape")
    public NavItem(int number, String label) {
        super(label);
        this.number = number;

        setFont(Theme.fontBody());
        setForeground(Theme.NAV_TEXT);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setHorizontalAlignment(SwingConstants.LEFT);
        setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH, Theme.NAV_ITEM_HEIGHT));
        setMinimumSize(new Dimension(Theme.SIDEBAR_WIDTH, Theme.NAV_ITEM_HEIGHT));
        setMaximumSize(new Dimension(Theme.SIDEBAR_WIDTH, Theme.NAV_ITEM_HEIGHT));
        getAccessibleContext().setAccessibleName(number + " " + label);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        boolean selected = isSelected();
        if (selected) {
            g2.setColor(Theme.PRIMARY);
            g2.fillRoundRect(0, 0, width, height, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);
            g2.setColor(Theme.MINT);
            g2.fillRect(0, 0, Theme.SP_4, height);
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(255, 255, 255, 32));
            g2.fillRoundRect(0, 0, width, height, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);
        }

        int badgeSize = 24;
        int badgeX = Theme.SP_16;
        int badgeY = (height - badgeSize) / 2;
        g2.setColor(selected ? new Color(255, 255, 255, 48) : new Color(255, 255, 255, 24));
        g2.fillOval(badgeX, badgeY, badgeSize, badgeSize);

        String numberText = Integer.toString(number);
        g2.setFont(Theme.fontBodyBold());
        FontMetrics badgeMetrics = g2.getFontMetrics();
        g2.setColor(selected ? Theme.ON_PRIMARY : Theme.NAV_TEXT);
        g2.drawString(numberText,
                badgeX + (badgeSize - badgeMetrics.stringWidth(numberText)) / 2,
                badgeY + (badgeSize - badgeMetrics.getHeight()) / 2 + badgeMetrics.getAscent());

        String label = getText();
        if (label != null) {
            g2.setFont(selected ? Theme.fontBodyBold() : Theme.fontBody());
            g2.setColor(selected ? Theme.ON_PRIMARY : Theme.NAV_TEXT);
            g2.drawString(label, badgeX + badgeSize + Theme.SP_8,
                    (height - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent());
        }

        if (hasFocus()) {
            g2.setColor(Theme.MINT);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(1, 1, width - 3, height - 3, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);
        }
        g2.dispose();
    }
}
