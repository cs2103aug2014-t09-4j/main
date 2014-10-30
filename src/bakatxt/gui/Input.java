package bakatxt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JTextField;
// TODO tons of things
class Input extends JTextField implements BakaAnimator {

    private Shape shape;
    private final Color _baseColor;
    private final Point _baseLocation;

    public Input() {
        setOpaque(false);
        setFont(new Font("Helvetica Neue",Font.PLAIN, 24));
        setBackground(UIHelper.GRAY_LIGHT);
        setForeground(UIHelper.PRESET_COLOR_DEFAULT);
        setCaretColor(UIHelper.PRESET_COLOR_DEFAULT);
        setSelectedTextColor(UIHelper.PRESET_COLOR_DEFAULT);
        setSelectionColor(new Color(0,0,0,150));
        setFocusTraversalKeysEnabled(false);

        _baseColor = UIHelper.GRAY_LIGHT;
        _baseLocation = new Point(2 * UIHelper.BORDER, 2 * UIHelper.BORDER);
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
