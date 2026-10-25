import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JPanel;

/**
 * Reusable JPanel with rounded corners, custom fill, and an optional soft shadow and border.
 */
public class RoundedPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color fill;
    private final int radius;
    private final boolean drawShadowAndBorder;

    private static final int SHADOW_LEFT = 6;
    private static final int SHADOW_RIGHT = 6;
    private static final int SHADOW_TOP = 4;
    private static final int SHADOW_BOTTOM = 8;

    /**
     * Constructs a RoundedPanel with specified background fill, corner radius, and optional shadow/border.
     *
     * @param fill the panel background fill color
     * @param radius corner radius in pixels
     * @param drawShadowAndBorder true to render soft shadow and 1px border outline, false for flat fill
     */
    @SuppressWarnings("this-escape")
    public RoundedPanel(Color fill, int radius, boolean drawShadowAndBorder) {
        this.fill = fill;
        this.radius = radius;
        this.drawShadowAndBorder = drawShadowAndBorder;
        setOpaque(false);
    }

    /**
     * Computes component insets, including padding space for shadow spread when enabled.
     *
     * @return insets of the panel
     */
    @Override
    public Insets getInsets() {
        Insets base = super.getInsets();
        if (drawShadowAndBorder) {
            return new Insets(
                base.top + SHADOW_TOP,
                base.left + SHADOW_LEFT,
                base.bottom + SHADOW_BOTTOM,
                base.right + SHADOW_RIGHT
            );
        }
        return base;
    }

    /**
     * Custom painting for soft shadow, rounded background fill, and border outline.
     *
     * @param g graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.enableAntialiasing(g2);

        int x = drawShadowAndBorder ? SHADOW_LEFT : 0;
        int y = drawShadowAndBorder ? SHADOW_TOP : 0;
        int w = getWidth() - (drawShadowAndBorder ? (SHADOW_LEFT + SHADOW_RIGHT) : 0);
        int h = getHeight() - (drawShadowAndBorder ? (SHADOW_TOP + SHADOW_BOTTOM) : 0);

        if (w > 0 && h > 0) {
            if (drawShadowAndBorder) {
                // Soft shadow approximation (~10% black, ~12px spread, 2px downward offset)
                for (int i = 4; i >= 1; i--) {
                    int alpha = (int) (255 * (0.02f * (5 - i)));
                    g2.setColor(new Color(0, 0, 0, Math.min(25, alpha)));
                    g2.fillRoundRect(
                        x - i,
                        y - i + 2,
                        w + 2 * i,
                        h + 2 * i,
                        radius + 2 * i,
                        radius + 2 * i
                    );
                }
            }

            // Panel fill
            g2.setColor(fill != null ? fill : Theme.SURFACE);
            g2.fillRoundRect(x, y, w, h, radius, radius);

            // Outline border
            if (drawShadowAndBorder) {
                g2.setColor(Theme.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            }
        }

        g2.dispose();
    }
}
