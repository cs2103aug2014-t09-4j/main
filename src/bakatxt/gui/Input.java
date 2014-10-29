package bakatxt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JTextField;
// TODO tons of things
class Input extends JTextField {

    private Shape shape;

    public Input() {
        setOpaque(false);
        setFont(new Font("Helvetica Neue",Font.PLAIN, 24));
        setBackground(UIHelper.GRAY_LIGHT);
        setForeground(UIHelper.PRESET_COLOR_DEFAULT);
        setCaretColor(UIHelper.PRESET_COLOR_DEFAULT);
        setSelectedTextColor(UIHelper.PRESET_COLOR_DEFAULT);
        setSelectionColor(new Color(0,0,0,150));
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        UIHelper.paintRoundedRectangle(g, getBackground(), getWidth(), getHeight());
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }

    // for mouse events
    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(),
                    UIHelper.WINDOW_ROUNDNESS, UIHelper.WINDOW_ROUNDNESS);
        }
        return shape.contains(x, y);
    }
}
