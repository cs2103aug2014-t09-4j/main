//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.RoundRectangle2D;

/**
 * Helper methods and variables for the GUI
 *
 */
public class UIHelper {
    /**
     * Get the size of the screen BakaTxt is on
     */
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit()
                                                .getScreenSize();

    /**
     * The location of BakaTxt relative to the top of the screen
     */
    public static final int WINDOW_OFFSET_TOP = SCREEN_SIZE.height/6;

    /**
     * The width of BakaTxt
     */
    public static final int WINDOW_X = 650;

    /**
     * The height of BakaTxt
     */
    public static final int WINDOW_Y = SCREEN_SIZE.height - 2 * WINDOW_OFFSET_TOP;

    /**
     * The width and height of BakaTxt as a Dimension object
     */
    public static final Dimension WINDOW_SIZE = new Dimension(WINDOW_X, WINDOW_Y);

    /**
     * The location of BakaTxt on the screen
     */
    public static final Point WINDOW_LOCATION = new Point(SCREEN_SIZE.width / 2 -
                                                          WINDOW_X / 2,
                                                          WINDOW_OFFSET_TOP);

    /**
     * Standard roundedness variable used for most rounded corners in BakaTxt
     */
    protected static final int WINDOW_ROUNDNESS = 20;

    /**
     * Standard (half of) width variable used for borders in BakaTxt
     */
    protected static final int BORDER = 4;

    /**
     * Location of the input box in BakaTxt
     */
    public static final Point INPUT_LOCATION = new Point(BORDER * 2, BORDER * 2);

    /**
     * A transparent color
     */
    protected static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    /**
     * Draw a rectangle with rounded edges
     *
     * @param g
     *        abstract base class for all graphics contexts
     * @param background
     *        color to paint
     * @param width
     *        width of component
     * @param height
     *        height of component
     */
    protected static void paintRoundedRectangle(Graphics g, Color background,
                                                int width, int height) {
        paintRoundedRectangle(g, background, 0, 0, width, height, WINDOW_ROUNDNESS);
    }

    /**
     * Draw a rectangle with rounded edges with roundness specification
     *
     * @param g
     *        abstract base class for all graphics contexts
     * @param background
     *        color to paint
     * @param startX
     *        start X location to paint the rectangle
     * @param startY
     *        start Y location to paint the rectangle
     * @param width
     *        width of component
     * @param height
     *        height of component
     * @param round
     *        specify the radius of the round corners
     */
    protected static void paintRoundedRectangle(Graphics g, Color background,
                                                int startX, int startY,
                                                int width, int height, int round) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHints(UIHelper.antiAlias());
        g2d.setColor(background);
        g2d.fill(new RoundRectangle2D.Double(startX, startY, width, height,
                                             round, round));
        g2d.dispose();
    }

    /**
     * Provide RenderingHints for anti-aliasing
     *
     * @return RenderingHints for anti-aliasing
     */
    protected static RenderingHints antiAlias() {
        RenderingHints qualityHints =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                   RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        return qualityHints;
    }
}
