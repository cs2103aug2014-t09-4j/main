//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JTextField;

import bakatxt.gui.look.BakaAnimator;
import bakatxt.gui.look.BakaTheme;
import bakatxt.gui.look.ThemeReader;
import bakatxt.gui.look.UIHelper;

/**
 * This class sets the input box for BakaUI
 *
 */
public class Input extends JTextField implements BakaAnimator {

    /**
     * Needed to read mouse clicks on the input box
     */
    private Shape shape;

    /**
     * Color of the input box
     */
    private Color _baseColor;

    /**
     * Relative location of the input box
     */
    private final Point _baseLocation;

    private BakaTheme _theme;

    public Input() {
        setOpaque(false);
        setTheme();
        setFocusTraversalKeysEnabled(false);

        _baseLocation = new Point(2 * UIHelper.BORDER, 2 * UIHelper.BORDER);
    }

    /**
     * refresh the colors and the font when a new theme is set
     */
    @SuppressWarnings("boxing")
    protected void setTheme() {
        _theme = ThemeReader.getInteractTheme();
        setFont(new Font(ThemeReader.getTypeface(), _theme.getFontType(),
                         _theme.getFontSize()));
        setBackground(ThemeReader.getInputColor());
        setForeground(_theme.getColor());
        setCaretColor(_theme.getColor());
        setSelectedTextColor(_theme.getColor());
        setSelectionColor(ThemeReader.getSelectionColor());
        _baseColor = ThemeReader.getInputColor();
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

    /**
     * @see {@link bakatxt.gui.look.BakaAnimator}
     */
    @Override
    public Color getColor() {
        return _baseColor;
    }

    /**
     * @see {@link bakatxt.gui.look.BakaAnimator}
     */
    @Override
    public void setColor(Color newColor) {
        setBackground(newColor);
    }

    /**
     * @see {@link bakatxt.gui.look.BakaAnimator}
     */
    @Override
    public Point getPoint() {
        return _baseLocation;
    }

    /**
     * @see {@link bakatxt.gui.look.BakaAnimator}
     */
    @Override
    public void setPoint(Point newLocation) {
        setLocation(newLocation);
    }
}
