//@author A0116538A

package bakatxt.gui;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JTextArea;

import bakatxt.gui.theme.BakaTheme;
import bakatxt.gui.theme.ThemeReader;

/**
 * This class is used for each of the text elements
 *
 */
@SuppressWarnings("boxing")
class FormattedText extends JTextArea {

    /**
     * get the typeface we are using for the UI
     */
    private String _typeface = ThemeReader.getTypeface();

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

    protected void setTheme(BakaTheme theme) {
        setFont(new Font(_typeface, theme.getFontType(),
                theme.getFontSize()));
        setForeground(theme.getColor());
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }
}
