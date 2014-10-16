package bakatxt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JTextArea;

class FormattedText extends JTextArea {

    public FormattedText(String s, int fontType, int fontSize, Color fontColor) {
        setOpaque(false);
        setFont(new Font("Helvetica Neue", fontType, fontSize));
        setBackground(UIHelper.TRANSPARENT);
        setForeground(fontColor);
        setEditable(false);
        setLineWrap(false);
        setText(s);
    }

    public FormattedText(String s, int fontType, int fontSize, Color fontColor, boolean isLineWrap) {
        setOpaque(false);
        setFont(new Font("Helvetica Neue", fontType, fontSize));
        setBackground(UIHelper.TRANSPARENT);
        setForeground(fontColor);
        setEditable(false);
        setLineWrap(isLineWrap);
        setWrapStyleWord(true);
        setText(s);
    }

    protected void updateContents(String s) {
        setText(s);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }
}
