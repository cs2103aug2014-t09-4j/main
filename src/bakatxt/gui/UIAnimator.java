//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * This class holds methods for animations used in BakaTxt
 */
class UIAnimator {
    // Maximum length of time in miliseconds the animations should be
    private static final int TIME_LENGTH = 640;
    // delay between animation frames
    private static final int DELAY = 20;
    // number of iterations the input box should be changing color
    private static final int ITERATIONS_FLASH = TIME_LENGTH / DELAY;
    /**
     * number of times the input box shakes, '4' is the number of times the delay
     * is used in the following method
     * @see {@link #shakeOneIteration(Point, int)}
     */
    private static final int ITERATIONS_SHAKE = TIME_LENGTH / DELAY / 4;
    /**
     *  in typical RGB, the value of each color ranges from 0-255, this is just
     *  to signify the 255 value
     */
    private static final float MAX_COLOR = 255;
    // the coponent we are animating
    private static JComponent _component;

    public UIAnimator(JComponent component) {
        _component = component;
    }

    /**
     * Vibrates the component
     *
     * @param initialLocation
     *        is the initial location the component is in
     */
    protected void shakeComponent(final Point initialLocation) {
        for (int i = 0; i < ITERATIONS_SHAKE; i++) {
            shakeOneIteration(initialLocation, ITERATIONS_SHAKE - i);
        }
    }

    /**
     * Flashes the component's color
     */
    protected void flashComponent() {
        if(!isEqual(UIHelper.GRAY_FLASH, _component.getBackground())) {

            float red = getColorAsFloat(UIHelper.GRAY_FLASH.getRed());
            float green = getColorAsFloat(UIHelper.GRAY_FLASH.getGreen());
            float blue = getColorAsFloat(UIHelper.GRAY_FLASH.getBlue());

            float redChange = getDelta(_component.getBackground().getRed(),
                                       UIHelper.GRAY_FLASH.getRed());
            float greenChange = getDelta(_component.getBackground().getGreen(),
                                         UIHelper.GRAY_FLASH.getGreen());
            float blueChange = getDelta(_component.getBackground().getBlue(),
                                        UIHelper.GRAY_FLASH.getBlue());

            setColor(UIHelper.GRAY_FLASH);

            for (int i = 0; i < ITERATIONS_FLASH; i++) {
                red += redChange;
                green += greenChange;
                blue += blueChange;
                setColor(new Color(red, green, blue));
                waitAWhile();
            }
            setColor(UIHelper.GRAY_LIGHT);
        }
    }

    /**
     * Shake the component right, then back to initial, then left, then back to initial
     * again.
     *
     * @param initialLocation
     *        is the initial location of the component to be shaked
     * @param movementDelay
     *        is the delay between shakes
     * @param xMovement
     *        is the amount to move the component by
     */
    private static void shakeOneIteration(final Point initialLocation, int xMovement) {
        moveBox(new Point(initialLocation.x + xMovement, initialLocation.y));
        waitAWhile();
        moveBox(initialLocation);
        waitAWhile();
        moveBox(new Point(initialLocation.x - xMovement, initialLocation.y));
        waitAWhile();
        moveBox(initialLocation);
        waitAWhile();
    }

    /**
     * Moves the input box to the set location
     *
     * @param p
     *        is the Point to move the box to
     */
    private static void moveBox(final Point p) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _component.setLocation(p);
            }
        });
    }

    /**
     * @param colorOne
     *        The first color to be compared
     * @param colorTwo
     *        the second color to be compared
     * @return true if the colors are equal
     */
    private static boolean isEqual(Color colorOne, Color colorTwo) {
        return colorOne.equals(colorTwo);
    }

    /**
     * gets a color as a fraction over 255
     *
     * @param color
     *        is the int we are converting to a fractional float
     * @return the float of the color
     */
    private static float getColorAsFloat(int color) {
        return color / MAX_COLOR;
    }

    /**
     * gets the rate of change needed to have a smooth animation
     *
     * @param initial
     *        The initial and final color
     * @param animated
     *        The flash color
     * @return the delta value
     */
    private static float getDelta(int initial, int animated) {
        return (initial - animated) / MAX_COLOR / ITERATIONS_FLASH;
    }

    /**
     * sets the color of the component
     *
     * @param color
     *        the color to be set to
     */
    private static void setColor(Color color) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _component.setBackground(color);
            }
        });
    }

    /**
     * Wait DELAY miliseconds before the next action
     */
    private static void waitAWhile() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
