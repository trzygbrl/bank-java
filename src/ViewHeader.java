import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Standard header component displaying a view title and subtitle.
 */
public class ViewHeader extends JPanel {
    private static final long serialVersionUID = 1L;

    private final JLabel titleLabel;
    private final JLabel subtitleLabel;

    /**
     * Constructs a ViewHeader with title and subtitle labels.
     *
     * @param title the view title string
     * @param subtitle the view subtitle string
     */
    @SuppressWarnings("this-escape")
    public ViewHeader(String title, String subtitle) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        titleLabel = new JLabel(title != null ? title : "");
        titleLabel.setFont(Theme.fontViewTitle());
        titleLabel.setForeground(Theme.PRIMARY_DEEP);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        subtitleLabel = new JLabel(subtitle != null ? subtitle : "");
        subtitleLabel.setFont(Theme.fontSubtitle());
        subtitleLabel.setForeground(Theme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(titleLabel);
        add(Box.createVerticalStrut(Theme.SP_4));
        add(subtitleLabel);
    }

    /**
     * Returns the title text.
     *
     * @return current title string
     */
    public String getTitle() {
        return titleLabel.getText();
    }

    /**
     * Returns the subtitle text.
     *
     * @return current subtitle string
     */
    public String getSubtitle() {
        return subtitleLabel.getText();
    }
}
