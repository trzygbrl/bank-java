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

    public String getTitle() {
        return titleLabel.getText();
    }

    public String getSubtitle() {
        return subtitleLabel.getText();
    }
}
