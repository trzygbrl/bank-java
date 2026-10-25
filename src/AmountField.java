import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Reusable single-line amount input field with a fixed currency symbol prefix,
 * placeholder hint text, focus ring, and error state border feedback.
 */
public class AmountField extends JPanel {
    private static final long serialVersionUID = 1L;

    private final JTextField textField;
    private boolean errorState = false;

    /**
     * Constructs the AmountField with currency symbol prefix and hint text.
     */
    @SuppressWarnings("this-escape")
    public AmountField() {
        setLayout(new BorderLayout(Theme.SP_8, 0));
        setOpaque(false);
        setFocusable(false);
        setBorder(BorderFactory.createEmptyBorder(0, Theme.SP_16, 0, Theme.SP_16));

        // Fixed Currency Prefix Label
        JLabel prefixLabel = new JLabel(MoneyUtil.CURRENCY_SYMBOL);
        prefixLabel.setFont(Theme.fontBodyBold());
        prefixLabel.setForeground(Theme.TEXT_PRIMARY);
        prefixLabel.setFocusable(false);
        add(prefixLabel, BorderLayout.WEST);

        // Inner Borderless Text Field with Placeholder Hint
        textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    Theme.enableAntialiasing(g2);
                    g2.setColor(Theme.TEXT_MUTED);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString("0.00", getInsets().left, y);
                    g2.dispose();
                }
            }
        };

        textField.setBorder(null);
        textField.setOpaque(false);
        textField.setFont(Theme.fontBody());
        textField.setForeground(Theme.TEXT_PRIMARY);
        textField.setCaretColor(Theme.TEXT_PRIMARY);
        textField.setSelectionColor(Theme.LAVENDER);
        textField.setSelectedTextColor(Theme.TEXT_PRIMARY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                textField.requestFocusInWindow();
            }
        });

        add(textField, BorderLayout.CENTER);
    }

    /**
     * Sets or clears the error state border rendering.
     *
     * @param errorState true to show error border, false for normal/focus border
     */
    public void setErrorState(boolean errorState) {
        this.errorState = errorState;
        repaint();
    }

    /**
     * Returns whether the field is currently rendered in an error state.
     *
     * @return true if in error state, false otherwise
     */
    public boolean isErrorState() {
        return errorState;
    }

    /**
     * Returns the text entered in the underlying text field.
     *
     * @return the entered amount text
     */
    public String getText() {
        return textField.getText();
    }

    /**
     * Sets the text of the underlying text field and repaints.
     *
     * @param text new text to display
     */
    public void setText(String text) {
        textField.setText(text);
        repaint();
    }

    /**
     * Requests keyboard focus for the inner text field.
     *
     * @return true if focus was successfully requested, false otherwise
     */
    @Override
    public boolean requestFocusInWindow() {
        return textField.requestFocusInWindow();
    }

    /**
     * Requests focus directly on the underlying JTextField.
     */
    public void focusField() {
        textField.requestFocusInWindow();
    }

    /**
     * Selects all text in the field and requests focus.
     */
    public void selectAllAndFocus() {
        textField.requestFocusInWindow();
        SwingUtilities.invokeLater(textField::selectAll);
    }

    /**
     * Adds an action listener that fires when the user presses Enter in the field.
     *
     * @param listener ActionListener to execute on Enter
     */
    public void addEnterListener(ActionListener listener) {
        textField.addActionListener(listener);
    }

    /**
     * Adds a listener that fires whenever the document text changes.
     *
     * @param listener Runnable callback invoked on insert, remove, or change
     */
    public void addChangeListener(Runnable listener) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                listener.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                listener.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                listener.run();
            }
        });
    }

    /**
     * Sets the accessible name on both this panel and the inner text field.
     *
     * @param name accessible description name
     */
    public void setAccessibleName(String name) {
        getAccessibleContext().setAccessibleName(name);
        textField.getAccessibleContext().setAccessibleName(name);
    }

    /**
     * Returns preferred size constrained to Theme.FIELD_HEIGHT.
     *
     * @return preferred dimension
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, Theme.FIELD_HEIGHT);
    }

    /**
     * Returns minimum size constrained to Theme.FIELD_HEIGHT.
     *
     * @return minimum dimension
     */
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(super.getMinimumSize().width, Theme.FIELD_HEIGHT);
    }

    /**
     * Custom painting for surface fill, focus ring, and error border.
     *
     * @param g graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.enableAntialiasing(g2);

        int w = getWidth();
        int h = getHeight();

        // Surface Fill
        g2.setColor(Theme.SURFACE);
        g2.fillRoundRect(1, 1, w - 2, h - 2, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);

        // Border: Error takes precedence over focus
        if (errorState) {
            g2.setColor(Theme.ERROR);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);
        } else if (textField.hasFocus()) {
            g2.setColor(Theme.PRIMARY);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);
        } else {
            g2.setColor(Theme.BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, Theme.RADIUS_CONTROL, Theme.RADIUS_CONTROL);
        }

        g2.dispose();
    }
}
