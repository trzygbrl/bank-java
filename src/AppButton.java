import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ButtonModel;
import javax.swing.JButton;

/**
 * Custom styled button supporting PRIMARY (filled lavender) and OUTLINED styles,
 * rounded corners, hover/pressed state feedback, and keyboard focus indicator.
 */
public class AppButton extends JButton {
    private static final long serialVersionUID = 1L;

    private final boolean primary;

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
    }

    public boolean isPrimary() {
        return primary;
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(Theme.fontBodyBold());
        int textWidth = fm.stringWidth(getText() != null ? getText() : "");
        int width = Math.max(100, textWidth + Theme.SP_24 * 2);
        return new Dimension(width, Theme.BUTTON_HEIGHT);
    }

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

    private static Color blendWithWhite(Color c, float ratio) {
        int r = Math.min(255, (int) (c.getRed() + (255 - c.getRed()) * ratio));
        int g = Math.min(255, (int) (c.getGreen() + (255 - c.getGreen()) * ratio));
        int b = Math.min(255, (int) (c.getBlue() + (255 - c.getBlue()) * ratio));
        return new Color(r, g, b);
    }
}
