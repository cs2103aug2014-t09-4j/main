//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JTextField;

/**
 * This class sets the input box for BakaUI
 *
 */
class Input extends JTextField implements BakaAnimator {

    /**
     * Needed to read mouse clicks on the input box
     */
    private Shape shape;

    /**
     * Color of the input box
     */
    private final Color _baseColor;

    /**
     * Relative location of the input box
     */
    private final Point _baseLocation;

    public Input() {
        setOpaque(false);
        setFont(new Font("Helvetica Neue",Font.PLAIN, 24));
        setBackground(UIHelper.INPUT_COLOR);
        setForeground(UIHelper.PRESET_COLOR_DEFAULT);
        setCaretColor(UIHelper.PRESET_COLOR_DEFAULT);
        setSelectedTextColor(UIHelper.PRESET_COLOR_DEFAULT);
        setSelectionColor(new Color(0,0,0,150));
        setFocusTraversalKeysEnabled(false);

        _baseColor = UIHelper.INPUT_COLOR;
        _baseLocation = new Point(2 * UIHelper.BORDER, 2 * UIHelper.BORDER);
    }

    /**
     * Paint the input box
     */
    @Override
    protected void paintComponent(Graphics g) {
        UIHelper.paintRoundedRectangle(g, getBackground(), getWidth(), getHeight());
        super.paintComponent(g);
    }

    /**
     * Make the border of the input box transparent
     */
    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }

    /**
     * Read mouse clicks on the input box
     */
    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(),
                    UIHelper.WINDOW_ROUNDNESS, UIHelper.WINDOW_ROUNDNESS);
        }
        return shape.contains(x, y);
    }

    @Override
    public Color getColor() {
        return _baseColor;
    }

    @Override
    public void setColor(Color newColor) {
        setBackground(newColor);
    }

    @Override
    public Point getPoint() {
        return _baseLocation;
    }

    @Override
    public void setPoint(Point newLocation) {
        setLocation(newLocation);
    }
}
