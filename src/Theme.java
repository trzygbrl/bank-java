import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import javax.swing.UIManager;

/**
 * Central design tokens, color palette, spacing scale, font factories,
 * and global Swing defaults for the Simple Bank application.
 */
public final class Theme {

    // 9.2 Color Palette (Exact Hex Values from PRD)
    public static final Color PRIMARY = new Color(0x5B4B8A);
    public static final Color PRIMARY_DARK = new Color(0x433670);
    public static final Color PRIMARY_DEEP = new Color(0x2E2552);
    public static final Color LAVENDER = new Color(0xE9E4F7);
    public static final Color LAVENDER_MIST = new Color(0xF3F0FA);
    public static final Color BACKGROUND = new Color(0xF5FFFA);
    public static final Color SURFACE = new Color(0xFFFFFF);
    public static final Color MINT = new Color(0xBFE8D4);
    public static final Color SUCCESS = new Color(0x2E7D5B);
    public static final Color SUCCESS_BG = new Color(0xE3F5EC);
    public static final Color ERROR = new Color(0xB3261E);
    public static final Color ERROR_BG = new Color(0xFDECEA);
    public static final Color WITHDRAWAL = new Color(0xA8324A);
    public static final Color TEXT_PRIMARY = new Color(0x2B2540);
    public static final Color TEXT_MUTED = new Color(0x6B6585);
    public static final Color BORDER = new Color(0xD9D3EC);
    public static final Color ON_PRIMARY = new Color(0xFFFFFF);
    public static final Color NAV_TEXT = new Color(0xE4DEF5);

    // 9.3 Spacing Scale
    public static final int SP_4 = 4;
    public static final int SP_8 = 8;
    public static final int SP_16 = 16;
    public static final int SP_24 = 24;
    public static final int SP_32 = 32;

    // 9.3 Corner Radius
    public static final int RADIUS_CARD = 12;
    public static final int RADIUS_CONTROL = 8;

    // 9.3 Component Heights and Widths
    public static final int BUTTON_HEIGHT = 44;
    public static final int FIELD_HEIGHT = 44;
    public static final int NAV_ITEM_HEIGHT = 48;
    public static final int TABLE_ROW_HEIGHT = 40;
    public static final int SIDEBAR_WIDTH = 240;
    public static final int MAX_CARD_WIDTH = 720;

    // Font Family Selection
    private static final String FONT_FAMILY = detectFontFamily();

    private static String detectFontFamily() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] families = ge.getAvailableFontFamilyNames();
            for (String family : families) {
                if ("Segoe UI".equalsIgnoreCase(family)) {
                    return "Segoe UI";
                }
            }
        } catch (Throwable t) {
            // Gracefully fall back to SansSerif
        }
        return Font.SANS_SERIF;
    }

    private Theme() {
        // Prevent instantiation of utility class
    }

    // Font Factory Methods
    public static Font fontBrand() {
        return new Font(FONT_FAMILY, Font.BOLD, 22);
    }

    public static Font fontViewTitle() {
        return new Font(FONT_FAMILY, Font.BOLD, 24);
    }

    public static Font fontSubtitle() {
        return new Font(FONT_FAMILY, Font.PLAIN, 14);
    }

    public static Font fontCardTitle() {
        return new Font(FONT_FAMILY, Font.BOLD, 18);
    }

    public static Font fontBody() {
        return new Font(FONT_FAMILY, Font.PLAIN, 14);
    }

    public static Font fontBodyBold() {
        return new Font(FONT_FAMILY, Font.BOLD, 14);
    }

    public static Font fontBalance() {
        return new Font(FONT_FAMILY, Font.BOLD, 40);
    }

    /**
     * Configures UIManager defaults so standard Swing components use the custom theme palette,
     * eliminating default Metal or OS colors for panels, dialogs, inputs, selections, and tooltips.
     */
    public static void applyGlobalDefaults() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("OptionPane.messageFont", fontBody());
        UIManager.put("OptionPane.buttonFont", fontBodyBold());
        UIManager.put("ScrollBar.thumb", LAVENDER);
        UIManager.put("ScrollBar.track", BACKGROUND);
        UIManager.put("ScrollBar.thumbHighlight", LAVENDER_MIST);
        UIManager.put("ScrollBar.thumbDarkShadow", BORDER);
        UIManager.put("ScrollBar.thumbShadow", BORDER);
        UIManager.put("ToolTip.background", LAVENDER_MIST);
        UIManager.put("ToolTip.foreground", TEXT_PRIMARY);
        UIManager.put("ToolTip.font", fontBody());
        UIManager.put("TextField.selectionBackground", LAVENDER);
        UIManager.put("TextField.selectionForeground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
        UIManager.put("TextArea.selectionBackground", LAVENDER);
        UIManager.put("TextArea.selectionForeground", TEXT_PRIMARY);
        UIManager.put("Table.selectionBackground", LAVENDER);
        UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.background", LAVENDER);
        UIManager.put("TableHeader.foreground", PRIMARY_DEEP);
        UIManager.put("TableHeader.font", fontBodyBold());
    }

    /**
     * Enables geometric antialiasing, text antialiasing, stroke normalization,
     * and high-quality rendering hints on the supplied Graphics2D context for High-DPI displays.
     */
    public static void enableAntialiasing(Graphics2D g) {
        if (g == null) {
            return;
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
    }
}
