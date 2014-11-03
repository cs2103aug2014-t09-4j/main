//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
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
        setOpaque(false);
        setFont(new Font(_typeface, theme.getFontType(),
                         theme.getFontSize()));
        setBackground(UIHelper.TRANSPARENT);
        Color color = theme.getColor();
        setForeground(color);
        setEditable(false);
        setLineWrap(false);
        setText(s);
    }

    public FormattedText(String s, BakaTheme theme, boolean isLineWrap) {
        setOpaque(false);
        setFont(new Font(_typeface, theme.getFontType(),
                         theme.getFontSize()));
        setBackground(UIHelper.TRANSPARENT);
        setForeground(theme.getColor());
        setEditable(false);
        setLineWrap(isLineWrap);
        setWrapStyleWord(true);
        setText(s);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }
}
