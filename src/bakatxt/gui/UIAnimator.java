//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Point;

import javax.swing.SwingUtilities;

/**
 * This class holds methods for animations used in BakaTxt
 */
class UIAnimator {

    /**
     * Maximum length of time in miliseconds the animations should be
     */
    private static final int TIME_LENGTH = 640;

    /**
     * delay between animation frames
     */
    private static final int DELAY = 20;

    /**
     * number of iterations the input box should be changing color
     */
    private static final int ITERATIONS_FLASH = TIME_LENGTH / DELAY;

    /**
     * number of times the input box shakes, '4' is the number of times the delay
     * is used in the following method
     * @see {@link #shakeOneIteration(Point, int)}
     */
    private static final int ITERATIONS_SHAKE = TIME_LENGTH / DELAY / 4;

    /**
     * in typical RGB, the value of each color ranges from 0-255, this is just
     * to signify the 255 value
     */
    private static final float MAX_COLOR = 255;

    /**
     * the amount of change in color per iteration
     */
    private static final float DELTA = (float)0.0030637255;

    /**
     *  the component we are animating
     */
    private static BakaAnimator _component;

    public UIAnimator(BakaAnimator component) {
        _component = component;
    }

    /**
     * Vibrates the component
     */
    protected void shakeComponent() {
        for (int i = 0; i < ITERATIONS_SHAKE; i++) {
            shakeOneIteration(_component.getPoint(), ITERATIONS_SHAKE - i);
        }
    }

    /**
     * Flashes the component's color, flash does not animate if isExceedMaximumColor
     * is triggered.
     */
    protected void flashComponent() {

        float red = getColorAsFloat(_component.getColor().getRed());
        float green = getColorAsFloat(_component.getColor().getGreen());
        float blue = getColorAsFloat(_component.getColor().getBlue());

        if(!isExceedMaximumColor(red, green, blue)) {
            for (int i = ITERATIONS_FLASH; i > 0; i--) {
                float extraColor = DELTA * i;
                setColor(new Color(red + extraColor,
                                   green + extraColor,
                                   blue + extraColor));
                waitAWhile();
            }
            setColor(_component.getColor());
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
                _component.setPoint(p);
            }
        });
    }

    /**
     * Ensure that the 3 components of color does not exceed 1 during the animation
     *
     * @param red
     *        red component of light
     * @param green
     *        green component of light
     * @param blue
     *        blue component of light
     * @return true if any color exceeds 1
     */
    private static boolean isExceedMaximumColor(float red, float green, float blue) {
        return isExceedMaximumColor(red) ||
               isExceedMaximumColor(green) ||
               isExceedMaximumColor(blue);
    }

    /**
     * Ensure that the component of color does not exceed 1 during the animation
     *
     * @param color
     *        the color we are checking
     * @return true if that component exceeds 1
     */
    private static boolean isExceedMaximumColor(float color) {
        return (DELTA * ITERATIONS_FLASH + color) >= 1;
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
     * sets the color of the component
     *
     * @param color
     *        the color to be set to
     */
    private static void setColor(Color color) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _component.setColor(color);
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
