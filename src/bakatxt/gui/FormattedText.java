//@author A0116538A

package bakatxt.gui;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JTextArea;

import bakatxt.gui.look.BakaTheme;
import bakatxt.gui.look.ThemeReader;
import bakatxt.gui.look.UIHelper;

/**
 * This class is used for each of the text elements
 *
 */
@SuppressWarnings("boxing")
class FormattedText extends JTextArea {

    public FormattedText(String s, BakaTheme theme) {
        this(s, theme, false);
    }

    public FormattedText(String s, BakaTheme theme, boolean isLineWrap) {
        setOpaque(false);
        setTheme(theme);
        setBackground(UIHelper.TRANSPARENT);
        setEditable(false);
        setLineWrap(isLineWrap);
        setWrapStyleWord(true);
        setText(s);
    }

    /**
     * Refresh the colors and typeface when a new theme is set
     *
     * @param theme
     *        is the new theme set
     */
    protected void setTheme(BakaTheme theme) {
        setFont(new Font(ThemeReader.getTypeface(), theme.getFontType(),
                         theme.getFontSize()));
        setForeground(theme.getColor());
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }
}
