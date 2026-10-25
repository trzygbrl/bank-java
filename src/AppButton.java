import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.KeyStroke;

/**
 * Custom styled button supporting PRIMARY (filled lavender) and OUTLINED styles,
 * rounded corners, hover/pressed state feedback, Enter/Space keyboard activation,
 * and keyboard focus indicator.
 */
public class AppButton extends JButton {
    private static final long serialVersionUID = 1L;

    private final boolean primary;

    /**
     * Constructs an AppButton with the specified label and style.
     *
     * @param text button label text
     * @param primary true for primary filled style, false for outlined style
     */
    @SuppressWarnings("this-escape")
    public AppButton(String text, boolean primary) {
        super(text);
        this.primary = primary;

        setFont(Theme.fontBodyBold());
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setFocusable(true);

        // Accessible name must be equal to its label (UI-13)
        getAccessibleContext().setAccessibleName(text);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });

        // Activate with Enter key as well as Space
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "pressOnEnter");
        getActionMap().put("pressOnEnter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClick();
            }
        });
    }

    /**
     * Checks if this button uses the primary filled style.
     *
     * @return true if primary style, false if outlined
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * Calculates the preferred size based on text width and standard padding.
     *
     * @return preferred button dimension
     */
    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(Theme.fontBodyBold());
        int textWidth = fm.stringWidth(getText() != null ? getText() : "");
        int width = Math.max(100, textWidth + Theme.SP_24 * 2);
        return new Dimension(width, Theme.BUTTON_HEIGHT);
    }

    /**
     * Custom painting for background fills, borders, hover/press feedback, and focus ring.
     *
     * @param g graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.enableAntialiasing(g2);

        int w = getWidth();
        int h = getHeight();

        ButtonModel model = getModel();
        boolean isPressed = model.isArmed() && model.isPressed();
        boolean isHover = model.isRollover();

        // Insets to leave space for the 2px focus ring
        int btnX = 2;
        int btnY = 2;
        int btnW = w - 4;
        int btnH = h - 4;

        // Background color calculation
        Color bg;
        if (primary) {
            if (isPressed) {
                bg = Theme.PRIMARY_DARK;
            } else if (isHover) {
                bg = blendWithWhite(Theme.PRIMARY, 0.10f);
            } else {
                bg = Theme.PRIMARY;
            }
        } else {
            if (isPressed) {
                bg = Theme.LAVENDER_MIST;
            } else if (isHover) {
                bg = Theme.LAVENDER;
            } else {
                bg = Theme.SURFACE;
            }
        }

        // Fill background
        g2.setColor(bg);
        g2.fillRoundRect(btnX, btnY, btnW, btnH, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);

        // Outlined border
        if (!primary) {
            g2.setColor(Theme.PRIMARY);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(btnX, btnY, btnW - 1, btnH - 1, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);
        }

        // 2px Focus ring
        if (hasFocus()) {
            g2.setColor(Theme.PRIMARY);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, Theme.RADIUS_CONTROL + 2, Theme.RADIUS_CONTROL + 2);
        }

        // Draw centered text
        g2.setFont(Theme.fontBodyBold());
        g2.setColor(primary ? Theme.ON_PRIMARY : Theme.PRIMARY);
        FontMetrics fm = g2.getFontMetrics();
        int textX = (w - fm.stringWidth(getText())) / 2;
        int textY = (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }

    /**
     * Blends a base color with white by a specified ratio for hover state feedback.
     *
     * @param c base color
     * @param ratio ratio of white to blend (0.0 to 1.0)
     * @return blended color
     */
    private static Color blendWithWhite(Color c, float ratio) {
        int r = Math.min(255, (int) (c.getRed() + (255 - c.getRed()) * ratio));
        int g = Math.min(255, (int) (c.getGreen() + (255 - c.getGreen()) * ratio));
        int b = Math.min(255, (int) (c.getBlue() + (255 - c.getBlue()) * ratio));
        return new Color(r, g, b);
    }
}
