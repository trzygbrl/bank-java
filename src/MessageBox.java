import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Reusable feedback message box supporting error and success states, multiline text wrapping,
 * rounded borders, and a stable reserved height (approx. 48px) to prevent layout jumping.
 */
public class MessageBox extends JPanel {
    private static final long serialVersionUID = 1L;
    private enum MessageType {
        NONE,
        ERROR,
        SUCCESS
    }

    private MessageType messageType = MessageType.NONE;
    private final JTextArea textArea;

    /**
     * Constructs a MessageBox with initial empty state and reserved height.
     */
    @SuppressWarnings("this-escape")
    public MessageBox() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setFocusable(false);
        setBorder(BorderFactory.createEmptyBorder(Theme.SP_8, Theme.SP_16, Theme.SP_8, Theme.SP_16));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(Theme.fontBody());
        textArea.setSelectionColor(Theme.LAVENDER);
        textArea.setSelectedTextColor(Theme.TEXT_PRIMARY);

        getAccessibleContext().setAccessibleName("Feedback message");
        getAccessibleContext().setAccessibleDescription("");

        add(textArea, BorderLayout.CENTER);
    }

    /**
     * Displays an error message with error background, border, and foreground colors.
     *
     * @param message error text to display
     */
    public void showError(String message) {
        messageType = MessageType.ERROR;
        String text = message != null ? message : "";
        textArea.setText(text);
        textArea.setForeground(Theme.ERROR);
        getAccessibleContext().setAccessibleDescription(text);
        textArea.getAccessibleContext().setAccessibleDescription(text);
        revalidate();
        repaint();
    }

    /**
     * Displays a success message with success background, border, and foreground colors.
     *
     * @param message success text to display
     */
    public void showSuccess(String message) {
        messageType = MessageType.SUCCESS;
        String text = message != null ? message : "";
        textArea.setText(text);
        textArea.setForeground(Theme.SUCCESS);
        getAccessibleContext().setAccessibleDescription(text);
        textArea.getAccessibleContext().setAccessibleDescription(text);
        revalidate();
        repaint();
    }

    /**
     * Clears any active message and hides the message box styling.
     */
    public void clear() {
        messageType = MessageType.NONE;
        textArea.setText("");
        getAccessibleContext().setAccessibleDescription("");
        textArea.getAccessibleContext().setAccessibleDescription("");
        revalidate();
        repaint();
    }

    /**
     * Package-private test hook returning current feedback message text.
     *
     * @return current feedback text
     */
    String getMessageText() {
        return textArea.getText();
    }

    /**
     * Returns preferred size enforcing minimum height of 48px to prevent layout shift.
     *
     * @return preferred dimension
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension base = super.getPreferredSize();
        return new Dimension(base.width, Math.max(48, base.height));
    }

    /**
     * Returns minimum size enforcing minimum height of 48px to prevent layout shift.
     *
     * @return minimum dimension
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension base = super.getMinimumSize();
        return new Dimension(base.width, Math.max(48, base.height));
    }

    /**
     * Custom painting for rounded background and 1px border.
     *
     * @param g graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (messageType == MessageType.NONE || textArea.getText().isEmpty()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        Theme.enableAntialiasing(g2);

        int w = getWidth();
        int h = getHeight();

        Color bg = (messageType == MessageType.ERROR) ? Theme.ERROR_BG : Theme.SUCCESS_BG;
        Color border = (messageType == MessageType.ERROR) ? Theme.ERROR : Theme.SUCCESS;

        // Background fill
        g2.setColor(bg);
        g2.fillRoundRect(1, 1, w - 2, h - 2, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);

        // Border outline
        g2.setColor(border);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(1, 1, w - 3, h - 3, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);

        g2.dispose();
    }
}
